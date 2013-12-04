package yescomment.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import yescomment.model.Article;
import yescomment.util.AllKeywordsSingletonLocal;
import yescomment.util.LatestArticlesSingletonLocal;

@Stateless
public class ArticleManager extends AbstractEntityManager<Article>  {


	@PersistenceContext(unitName = "YesCommentModel")
	private EntityManager em;
	
	@EJB
	AllKeywordsSingletonLocal allKeywordsSingleton;
	
	@EJB
	LatestArticlesSingletonLocal latestArticlesSingleton;
	
	public ArticleManager() {
		super(Article.class);

	}
	
	
	public Article getArticleByURL(String url) {
		if (url == null) {
			throw new IllegalArgumentException();
		} else {
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Article> q = cb.createQuery(Article.class);
			Root<Article> r = q.from(Article.class);

			List<Predicate> predicateList = new ArrayList<Predicate>();

			predicateList.add(cb.equal(r.get("url"), url));
			Predicate[] predicates = new Predicate[predicateList.size()];
			predicateList.toArray(predicates);
			q.where(predicates);
			TypedQuery<Article> query = em.createQuery(q);

			List<Article> articles = query.getResultList();
			if (articles.isEmpty()) {
				return null;
			} else if (articles.size() == 1) {
				return articles.get(0);
			} else
				throw new IllegalStateException();

		}
	}
	
	public List<Article> getLatestArticles(int maxResults) {
	
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> q = cb.createQuery(Article.class);
		Root<Article> r = q.from(Article.class);
		q.orderBy(cb.desc(r.<Date> get("registrationDate")));
		TypedQuery<Article> query = em.createQuery(q);
		query.setMaxResults(maxResults);
		return query.getResultList();

	}

	@Override
	protected void notifyEntityCreation(Article entity) {
		latestArticlesSingleton.articleCreated(entity);
		allKeywordsSingleton.articleCreated(entity);
	}

	@Override
	protected void notifyEntityModification(Article entity) {
		// doing nothing

	}

	@Override
	protected void notifyEntityDeletion(Article entity) {
		latestArticlesSingleton.articleDeleted(entity);
		allKeywordsSingleton.articleDeleted(entity);

	}
	
	public List<String> getAllKeywords(boolean distinct) {
		return getAllKeywords(null, distinct);
	}
	
	public List<String> getAllKeywords(String keywordStartFilter,
			boolean distinct) {

		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> q = cb.createQuery(String.class);
		Root<Article> r = q.from(Article.class);
		Join<Article, String> j = r.join("keywords");
		if (keywordStartFilter != null) {
			q.where(cb.like(j, keywordStartFilter + "%"));
		}
		q.select(j);
		q.distinct(distinct);
		TypedQuery<String> query = em.createQuery(q);

		return query.getResultList();
	}
	
	public List<Article> getArticlesWithKeyword(String keyword) {

		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> q = cb.createQuery(Article.class);
		Root<Article> r = q.from(Article.class);
		Join<Article, String> j = r.join("keywords");

		q.where(cb.like(j, keyword));

		TypedQuery<Article> query = em.createQuery(q);

		return query.getResultList();

	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
