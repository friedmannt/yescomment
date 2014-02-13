package yescomment.util;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;

/**
 * Singleton, holding the latest articles. If articles are created or deleted, it is notified
 * @author Friedmann Tamás
 *
 */
@Singleton
public class LatestArticlesSingleton implements  LatestArticlesSingletonLocal{

	private static final int LATEST_ARTICLE_COUNT = 100;

	@EJB
	ArticleManager articleManager;

	private List<Article> latestArticles;//holds the latest articvles
	
	@Override
	public List<Article> retrieveLatestArticles() {
		if (latestArticles == null) {
			populateLatestArticles();
		}
		return latestArticles;
	}


	private void populateLatestArticles() {
		latestArticles = articleManager.getLatestArticles(LATEST_ARTICLE_COUNT);
	}


	@Override
	public void articleCreated(Article article) {
		populateLatestArticles();
		
	}


	@Override
	public void articleDeleted(Article article) {
		populateLatestArticles();
		
	}
	
	

}
