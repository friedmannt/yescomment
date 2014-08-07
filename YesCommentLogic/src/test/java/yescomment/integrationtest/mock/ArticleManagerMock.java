package yescomment.integrationtest.mock;

import javax.persistence.EntityManager;

import yescomment.keyword.AllKeywordsSingleton;
import yescomment.persistence.ArticleManager;

public class ArticleManagerMock extends ArticleManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public ArticleManagerMock(EntityManager em) {
		this.em=em;
	}
	
	public void setAllKeywordsSingleton(AllKeywordsSingleton allKeywordsSingleton) {
		this.allKeywordsSingleton=allKeywordsSingleton;
	}

	

}
