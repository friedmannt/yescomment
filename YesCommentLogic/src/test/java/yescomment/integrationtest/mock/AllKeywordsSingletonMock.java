package yescomment.integrationtest.mock;

import yescomment.keyword.AllKeywordsSingleton;
import yescomment.persistence.ArticleManager;

public class AllKeywordsSingletonMock extends AllKeywordsSingleton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager=articleManager;
	}
	

	
	

}
