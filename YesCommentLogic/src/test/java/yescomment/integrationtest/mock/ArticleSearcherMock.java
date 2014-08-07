package yescomment.integrationtest.mock;

import javax.persistence.EntityManager;

import yescomment.persistence.search.ArticleSearcher;

public class ArticleSearcherMock extends ArticleSearcher {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public ArticleSearcherMock(EntityManager em) {
		this.em=em;
	}
	
	


	

}
