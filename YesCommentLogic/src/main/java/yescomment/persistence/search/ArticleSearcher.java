package yescomment.persistence.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.NullCoalesceValues;
import yescomment.persistence.search.ArticleSearchCriteria.CommentCountRange;

@Stateless
public class ArticleSearcher implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "YesCommentModel")
	protected EntityManager em;

	public List<Article> searchForArticles(@NotNull final ArticleSearchCriteria articleSearchCriteria, @NotNull final ArticleOrder articleOrder, boolean isAscending,int offset,int limit) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> q = cb.createQuery(Article.class);
		Root<Article> r = q.from(Article.class);
		Predicate[] predicates = createPredicatesForSearchCriteria(articleSearchCriteria, cb, q, r);

		q.where(predicates);
		String orderingAttributeName=articleOrder.getAttributeName();
		Class<?> orderingAttributeClass=articleOrder.getAttributeClass();
		Object nullCoalescedValue=NullCoalesceValues.getCoalescedDefaultValueForClass(orderingAttributeClass);
		if (isAscending) {
			q.orderBy(cb.asc(cb.coalesce(r.get(orderingAttributeName), cb.literal(nullCoalescedValue)) ));
		}
		else {
			q.orderBy(cb.desc(cb.coalesce(r.get(orderingAttributeName), cb.literal(nullCoalescedValue)) ));
		}
	
		
		
		TypedQuery<Article> query = em.createQuery(q).setFirstResult(offset).setMaxResults(limit);
		return query.getResultList();
	}

	
	public Long countOfSearchedArticles(@NotNull final ArticleSearchCriteria articleSearchCriteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Article> r = cq.from(Article.class);
		cq.select(cb.count(r));
		Predicate[] predicates = createPredicatesForSearchCriteria(articleSearchCriteria, cb, cq, r);

		cq.where(predicates);
		return em.createQuery(cq).getSingleResult();
	}
	
	private Predicate[] createPredicatesForSearchCriteria(final ArticleSearchCriteria articleSearchCriteria, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<Article> root) {
		List<Predicate> predicateList = new ArrayList<Predicate>();
		if (articleSearchCriteria.getTitle() != null) {
			final Expression<String> titleExpression = root.<String> get("title");
			predicateList.add(cb.like(cb.lower(titleExpression), "%" + articleSearchCriteria.getTitle().toLowerCase() + "%"));
		}

		if (articleSearchCriteria.getSite() != null) {
			final Expression<String> urlExpression = root.<String> get("url");
			final Expression<Integer> positionAfterSchemaExpression = cb.sum(cb.locate(urlExpression, "://"), cb.literal("://".length()));
			final Expression<String> urlWithoutSchemaExpression = cb.substring(urlExpression, positionAfterSchemaExpression);
			final Expression<Integer> lengthOfSiteExpression = cb.<Integer> selectCase().when(cb.equal(cb.locate(urlWithoutSchemaExpression, "/"), cb.literal(0)), cb.length(urlWithoutSchemaExpression)).otherwise(cb.diff(cb.locate(urlWithoutSchemaExpression, "/"), 1));
			final Expression<String> urlSiteExpression = cb.substring(urlWithoutSchemaExpression, cb.literal(1), lengthOfSiteExpression);
			predicateList.add(cb.equal(cb.lower(urlSiteExpression), articleSearchCriteria.getSite().toLowerCase()));
		}
		if (articleSearchCriteria.getLanguage() != null) {
			final Expression<String> languageExpression = root.<String> get("language");
			predicateList.add(cb.equal(languageExpression, articleSearchCriteria.getLanguage().toString().toLowerCase()));
		}
		if (articleSearchCriteria.getCreatedAfter() != null) {
			Calendar dateStartCal = Calendar.getInstance();
			dateStartCal.setTime(articleSearchCriteria.getCreatedAfter());
			dateStartCal.set(Calendar.HOUR_OF_DAY, 0);
			dateStartCal.set(Calendar.MINUTE, 0);
			dateStartCal.set(Calendar.SECOND, 0);
			dateStartCal.set(Calendar.MILLISECOND, 0);
			final Expression<Date> createDateExpression = root.<Date> get("createDate");
			predicateList.add(cb.greaterThanOrEqualTo(createDateExpression, dateStartCal.getTime()));

		}
		if (articleSearchCriteria.getCreatedBefore() != null) {
			Calendar dateEndCal = Calendar.getInstance();
			dateEndCal.setTime(articleSearchCriteria.getCreatedBefore());
			dateEndCal.set(Calendar.HOUR_OF_DAY, 0);
			dateEndCal.set(Calendar.MINUTE, 0);
			dateEndCal.set(Calendar.SECOND, 0);
			dateEndCal.set(Calendar.MILLISECOND, 0);
			dateEndCal.add(Calendar.DATE, 1);
			final Expression<Date> createDateExpression = root.<Date> get("createDate");
			predicateList.add(cb.lessThan(createDateExpression, dateEndCal.getTime()));
		}
		if (articleSearchCriteria.getCommentCountRange() != null) {
			final Expression<Integer> commentCountExpression = root.<Integer> get("commentCount");
			CommentCountRange commentCountRange=articleSearchCriteria.getCommentCountRange();
			if (commentCountRange.getLowerBound()!=null) {
				predicateList.add(cb.greaterThanOrEqualTo(commentCountExpression, commentCountRange.getLowerBound()));
			}
			if (commentCountRange.getUpperBound()!=null) {
				predicateList.add(cb.lessThanOrEqualTo(commentCountExpression, commentCountRange.getUpperBound()));
			}

		}
		if (articleSearchCriteria.getCommenter()!=null) {
			Subquery<Comment> subquery = cq.subquery(Comment.class); 
			Root<Comment> comment = subquery.from(Comment.class);  
			subquery.select(comment);
			List<Predicate> subQueryPredicateList=new ArrayList<Predicate>();
			subQueryPredicateList.add(cb.like(comment.<String>get("author"), articleSearchCriteria.getCommenter()));
			subQueryPredicateList.add(cb.equal(comment.<Article>get("article"), root));
			Predicate[] subQueryPredicates = new Predicate[subQueryPredicateList.size()];
			subQueryPredicateList.toArray(subQueryPredicates);

			subquery.where(subQueryPredicates);
			predicateList.add(cb.exists(subquery));
		}
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		return predicates;
	}
	
}
