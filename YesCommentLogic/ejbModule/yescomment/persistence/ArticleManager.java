package yescomment.persistence;

import java.util.ArrayList;
import java.util.Arrays;
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

import yescomment.keyword.AllKeywordsSingletonLocal;
import yescomment.model.Article;
import yescomment.util.ArticleInfo;

@Stateless
public class ArticleManager extends AbstractEntityManager<Article> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "YesCommentModel")
	private EntityManager em;

	@EJB
	AllKeywordsSingletonLocal allKeywordsSingleton;
	
	

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
				throw new IllegalStateException("Multiple articles were found with url " + url);

		}
	}

	


	@Override
	protected void notifyEntityCreation(Article entity) {

		allKeywordsSingleton.articleCreated(entity);
		
		
	}

	@Override
	protected void notifyEntityModification(Article entity) {
		// doing nothing

	}

	@Override
	protected void notifyEntityDeletion(Article entity) {

		allKeywordsSingleton.articleDeleted(entity);
	

	}

	public List<String> getAllKeywords(boolean distinct) {
		return getAllKeywords(null, distinct);
	}

	public List<String> getAllKeywords(String keywordStartFilter, boolean distinct) {

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

	public Article createArticleFromArticleInfo(ArticleInfo articleInfo) {
		Article article = new Article();
		article.setRegistrationDate(new Date());
		article.setUrl(articleInfo.getFinalURL());

		if (articleInfo.getTitle() != null) {
			article.setTitle(articleInfo.getTitle());
		} else {
			article.setTitle("");
		}
		if (articleInfo.getImageURL() != null) {
			article.setImageurl(articleInfo.getImageURL());
		} else {
			article.setImageurl("resources/images/defaultarticleimage.png");
		}
		article.setDescription(articleInfo.getDescription());
		List<String> newArticleKeywords = null;
		if (articleInfo.getKeywords() != null && !articleInfo.getKeywords().isEmpty()) {
			newArticleKeywords = new ArrayList<String>();
			List<String> keywords = Arrays.asList(articleInfo.getKeywords().split(","));
			for (String keyword : keywords) {
				keyword = keyword.trim();
				if (!keyword.equals("")) {
					newArticleKeywords.add(keyword);
				}

			}

		}

		if (newArticleKeywords != null) {
			article.getKeywords().addAll(newArticleKeywords);
		}
		
		article.setCommentCount(0);
		return article;
	}

	
	
	

}
