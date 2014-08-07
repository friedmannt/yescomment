package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.integrationtest.mock.RecommendedArticlesRetrieverMock;
import yescomment.model.Article;
import yescomment.recommended.RecommendationAscpect;
import yescomment.util.AnonymusUserName;

public class RecommendedArticlesIT extends AbstractPersistenceIT {

	RecommendedArticlesRetrieverMock recommendedArticlesRetriever;

	@Before
	public void initMocks() {
		super.initMocks();
		recommendedArticlesRetriever=new RecommendedArticlesRetrieverMock();
		recommendedArticlesRetriever.setArticleManager(articleManager);
	}

	@After
	public void clearMocks() {
		
		recommendedArticlesRetriever=null;
		super.clearMocks();
	}
	
	
	@Test
	public void testArticleRecommendation() throws IOException {
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
				List<Article> result =recommendedArticlesRetriever.retrieveRecommendedArticles(RecommendationAscpect.LASTCOMMENTED);
				assertEquals(result.get(0).getUrl(), "http://handras.hu/?p=8968");
			}
			{
				List<Article> result =recommendedArticlesRetriever.retrieveRecommendedArticles(RecommendationAscpect.LATEST);
				assertEquals(result.get(0).getUrl(), "http://handras.hu/?p=8965");
			}
			{
				List<Article> result =recommendedArticlesRetriever.retrieveRecommendedArticles(RecommendationAscpect.MOSTCOMMENTED);
				assertEquals(result.get(0).getUrl(), "http://daringfireball.net/thetalkshow/");
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


}
