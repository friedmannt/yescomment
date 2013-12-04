package yescomment.util;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;

/**
 * Singleton, holding the latest articles. If articles are created or deleted, it is notified
 * @author Friedmann Tamás
 *
 */
@Singleton
public class LatestArticlesSingleton implements  LatestArticlesSingletonLocal{

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
		latestArticles = articleManager.getLatestArticles(10);
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
