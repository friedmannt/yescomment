package yescomment.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import yescomment.keyword.AllKeywordsSingleton;
import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.util.ArticleInfo;
import yescomment.util.ArticleRedundantCommentDataUtil;
import yescomment.util.StringUtil;

@Stateless
public class ArticleManager extends AbstractEntityManager<Article> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "YesCommentModel")
	protected EntityManager em;

	@EJB
	protected AllKeywordsSingleton allKeywordsSingleton;

	public ArticleManager() {
		super(Article.class);

	}

	/**
	 * Gets article with given URL
	 * @param url
	 * @return article or null
	 */
	public Article getArticleByURL(@NotNull final String url) {
		

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Article> q = cb.createQuery(Article.class);
			Root<Article> r = q.from(Article.class);

			List<Predicate> predicateList = new ArrayList<Predicate>();

			predicateList.add(cb.equal(r.<String>get("url"), url));
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

	@Override
	protected void notifyEntityCreation(Article entity) {
		//keyword housekeeping
		allKeywordsSingleton.articleCreated(entity);

	}

	
	@Override
	protected void notifyEntityDeletion(Article entity) {
		//keyword housekeeping
		allKeywordsSingleton.articleDeleted(entity);

	}

	public List<String> getAllKeywords(boolean distinct) {
		return getAllKeywords(null, distinct);
	}

	public List<String> getAllKeywords(@NotNull final String keywordStartFilter, boolean distinct) {

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

	/**
	 * Gets articles, which have the given exact keyword and given languagecode
	 * @param keyword
	 * @param languageCode
	 * @return the list of articles
	 */
	public List<Article> getArticlesWithKeyword(@NotNull final String keyword,final String languageCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> q = cb.createQuery(Article.class);
		Root<Article> r = q.from(Article.class);
		Join<Article, String> j = r.join("keywords");
		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(cb.like(cb.lower(j), keyword.toLowerCase()));
		if (languageCode!=null) {
			predicateList.add(cb.equal(r.<String>get("language"), languageCode));
		}

		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		q.where(predicates);
		TypedQuery<Article> query = em.createQuery(q);

		return query.getResultList();

	}
	
	/**
	 * Gets articles on given date, not caring about time portion
	 * @param date : the createDate of the article
	 * @return
	 */
	public List<Article> getArticlesOnDate(@NotNull final Date date) {
		Calendar dateStartCal=Calendar.getInstance();
		dateStartCal.setTime(date);
		dateStartCal.set(Calendar.HOUR_OF_DAY, 0);
		dateStartCal.set(Calendar.MINUTE, 0);
		dateStartCal.set(Calendar.SECOND, 0);
		dateStartCal.set(Calendar.MILLISECOND, 0);
		
		Calendar dateEndCal=Calendar.getInstance();
		dateEndCal.setTime(dateStartCal.getTime());
		dateEndCal.add(Calendar.DATE, 1);
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> q = cb.createQuery(Article.class);
		Root<Article> r = q.from(Article.class);
		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(cb.greaterThanOrEqualTo(r.<Date>get("createDate"), dateStartCal.getTime()));
		predicateList.add(cb.lessThan(r.<Date>get("createDate"), dateEndCal.getTime()));
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		q.where(predicates);

		TypedQuery<Article> query = em.createQuery(q);

		return query.getResultList();

	}


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Article createArticleFromArticleInfo(@NotNull final ArticleInfo articleInfo) {
		Article article = new Article();
	
		article.setUrl(articleInfo.getFinalURL());

		if (articleInfo.getTitle() != null) {
			article.setTitle(articleInfo.getTitle());
		} else {
			article.setTitle("");
		}
		if (articleInfo.getImageURL() != null) {
			article.setImageurl(articleInfo.getImageURL());
		} else {
			article.setImageurl("resources/images/defaultarticleimage.svg");
		}
		article.setDescription(articleInfo.getDescription()==null?null:StringUtil.maximizeStringLength(articleInfo.getDescription(), Article.MAX_DESCRIPTION_SIZE));
		article.setLanguage(articleInfo.getLanguage());
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

		ArticleRedundantCommentDataUtil.validateRedundantCommentData(article);
		return article;
	}

	/**
	 * openshift:JBOSS beacause of jboss, entity graph should be used to read comments
	 */
	@Override
	public Article find(final String id) {
		EntityGraph<Article> articleWithComments = em.createEntityGraph(Article.class);
		articleWithComments.addAttributeNodes("comments");
		Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.loadgraph", articleWithComments);
		return em.find(Article.class, id, props);

	}

	/**
	 * Merges two articles. All comments will be moved from the merge source to the merge target article. Merge source will be deleted
	 * @param mergedArticleId
	 * @param mergeTargetArticleId
	 */
	public void mergeArticlesComments(@NotNull final String mergeSourceArticleId, @NotNull final String mergeTargetArticleId) {
		assert !mergeSourceArticleId.equals(mergeTargetArticleId);
		Article sourceArticle=find(mergeSourceArticleId);
		Article targetArticle=find(mergeTargetArticleId);
		if (sourceArticle!=null&&targetArticle!=null) {
			for (Comment comment:sourceArticle.getComments()) {
				comment.setArticle(targetArticle);
				targetArticle.getComments().add(comment);
			}
			ArticleRedundantCommentDataUtil.validateRedundantCommentData(targetArticle);
			
			sourceArticle.getComments().clear();
			remove(sourceArticle);
			
			
		}
		
	}

	/**
	 * Revalidates comment count last comment date based on articles comments
	 * @param validatedArticleId
	 */
	public void validateArticleCommentData(@NotNull final String validatedArticleId) {
			Article article=find(validatedArticleId);
			ArticleRedundantCommentDataUtil.validateRedundantCommentData(article);
		
	}

	/**
	 * Returns articles with no language set
	 * @return
	 */
	public List<Article> getArticlesWithoutLanguage() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> q = cb.createQuery(Article.class);
		Root<Article> r = q.from(Article.class);
		

		q.where(cb.isNull(r.<String>get("language")));

		TypedQuery<Article> query = em.createQuery(q);

		return query.getResultList();

	}
	
}
