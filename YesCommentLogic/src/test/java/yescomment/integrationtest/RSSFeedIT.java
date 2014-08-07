package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.integrationtest.mock.RSSFeedCreatorMock;
import yescomment.integrationtest.mock.RecommendedArticlesRetrieverMock;
import yescomment.model.Article;
import yescomment.recommended.RecommendationAscpect;
import yescomment.util.AnonymusUserName;

import com.sun.syndication.feed.synd.SyndFeed;

public class RSSFeedIT extends AbstractPersistenceIT {

	RSSFeedCreatorMock rssFeedCreatorMock;
	RecommendedArticlesRetrieverMock recommendedArticlesRetriever;

	@Before
	public void initMocks() {
		super.initMocks();
		recommendedArticlesRetriever = new RecommendedArticlesRetrieverMock();
		recommendedArticlesRetriever.setArticleManager(articleManager);
		rssFeedCreatorMock = new RSSFeedCreatorMock();
		rssFeedCreatorMock.setArticleManager(articleManager);
		rssFeedCreatorMock.setRecommendedArticlesRetriever(recommendedArticlesRetriever);
	}

	@After
	public void clearMocks() {
		removeAllArticles();
		recommendedArticlesRetriever = null;
		rssFeedCreatorMock = null;
		super.clearMocks();
	}

	@Test
	public void testRSSOfRecommendedArticles() throws IOException {
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
			SyndFeed syndFeed = rssFeedCreatorMock.getRSSFeedOfRecommendationAspect(RecommendationAscpect.LASTCOMMENTED, "urlBase");
			assertEquals(4, syndFeed.getEntries().size());
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
	public void testRSSOfArticleComments() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Article article = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://daringfireball.net/thetalkshow/")));
			for (int i = 1; i <= 100; i++) {
				commentManager.addCommentToArticle(article, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
			}

			SyndFeed syndFeed = rssFeedCreatorMock.getRSSFeedOfArticlesComments(article.getId(), "urlBase");
			assertEquals(100, syndFeed.getEntries().size());
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}

	}

}
