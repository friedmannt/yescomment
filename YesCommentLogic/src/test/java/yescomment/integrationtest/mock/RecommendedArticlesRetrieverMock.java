package yescomment.integrationtest.mock;

import yescomment.persistence.ArticleManager;
import yescomment.recommended.RecommendedArticlesRetriever;

public class RecommendedArticlesRetrieverMock extends RecommendedArticlesRetriever {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager=articleManager;
	}
	

}
