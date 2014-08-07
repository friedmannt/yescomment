package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.model.Article;
import yescomment.persistence.search.ArticleOrder;
import yescomment.persistence.search.ArticleSearchCriteria;
import yescomment.persistence.search.ArticleSearchCriteria.CommentCountRange;
import yescomment.util.AnonymusUserName;
import yescomment.util.ArticleLanguage;

public class DetailedSearchIT extends AbstractPersistenceIT {

	@Test
	public void testArticleSearch() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			{

				Article article = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://daringfireball.net/thetalkshow/")));
				for (int i = 1; i <= 100; i++) {
					commentManager.addCommentToArticle(article, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
				}

			}
			{

				Article article = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8974")));
				for (int i = 1; i <= 50; i++) {
					commentManager.addCommentToArticle(article, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
				}

			}
			{

				Article article = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8968")));
				for (int i = 1; i <= 10; i++) {
					commentManager.addCommentToArticle(article, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
				}

			}
			{

				 articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8965")));
				

			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();

				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(4, results.size());
				assertEquals(100, results.get(0).getCommentCount().intValue());
				assertEquals(50, results.get(1).getCommentCount().intValue());
				assertEquals(10, results.get(2).getCommentCount().intValue());
				assertEquals(0, results.get(3).getCommentCount().intValue());
			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();
				articleSearchCriteria.setCommentCountRange(CommentCountRange.BETWEEN11AND100);
				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(2, results.size());
				
			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();
				articleSearchCriteria.setCommenter("feri");
				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(0, results.size());
				
			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();
				
				articleSearchCriteria.setCreatedAfter(new Date(new Date().getTime()+1000*60*60*24));//add one day
				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(0, results.size());
				
			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();
				articleSearchCriteria.setCreatedBefore(new Date());
				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(4, results.size());
				
			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();
				articleSearchCriteria.setLanguage(ArticleLanguage.HU);
				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(3, results.size());
				
			}
			{
				ArticleSearchCriteria articleSearchCriteria = new ArticleSearchCriteria();
				articleSearchCriteria.setTitle("Daring Fireball: The Talk Show");
				articleSearchCriteria.setSite("daringfireball.net");
				List<Article> results = articleSearcher.searchForArticles(articleSearchCriteria, ArticleOrder.ARTICLE_COMMENT_COUNT, false, 0, Integer.MAX_VALUE);
				assertEquals(1, results.size());
				long count=articleSearcher.countOfSearchedArticles(articleSearchCriteria);
				assertEquals(1l, count);
				
			}
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}

	}

	@Test
	public void testArticleSearchCriteriaEquality() {
		Set<ArticleSearchCriteria> articleSearchCriterias=new HashSet<ArticleSearchCriteria>();
		ArticleSearchCriteria asc1=new ArticleSearchCriteria();
		asc1.setCommentCountRange(CommentCountRange.BETWEEN11AND100);
		asc1.setTitle("valami");
		asc1.setLanguage(ArticleLanguage.HU);
		assertFalse(articleSearchCriterias.contains(asc1));
		articleSearchCriterias.add(asc1);
		ArticleSearchCriteria asc2=new ArticleSearchCriteria();
		asc2.setCommentCountRange(CommentCountRange.BETWEEN11AND100);
		asc2.setTitle("valami más");
		assertFalse(articleSearchCriterias.contains(asc2));
		articleSearchCriterias.add(asc2);
		ArticleSearchCriteria asc3=new ArticleSearchCriteria();
		assertFalse(articleSearchCriterias.contains(asc3));
		articleSearchCriterias.add(asc3);
		ArticleSearchCriteria asc4=new ArticleSearchCriteria();
		asc4.setCommentCountRange(CommentCountRange.BETWEEN11AND100);
		asc4.setTitle("valami");
		asc4.setLanguage(ArticleLanguage.HU);
		assertTrue(articleSearchCriterias.contains(asc4));
		ArticleSearchCriteria asc5=new ArticleSearchCriteria();
		assertTrue(articleSearchCriterias.contains(asc5));
	}
}
