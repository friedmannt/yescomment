package yescomment.integrationtest.mock;

import yescomment.persistence.ArticleManager;
import yescomment.recommended.RecommendedArticlesRetriever;
import yescomment.rss.RSSFeedCreator;

public class RSSFeedCreatorMock extends RSSFeedCreator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	
	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager=articleManager;
	}
	
	public void setRecommendedArticlesRetriever (RecommendedArticlesRetriever recommendedArticlesRetriever) {
		this.recommendedArticlesRetriever=recommendedArticlesRetriever;
	}


	

}
