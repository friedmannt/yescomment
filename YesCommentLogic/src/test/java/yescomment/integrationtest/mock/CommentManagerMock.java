package yescomment.integrationtest.mock;

import javax.persistence.EntityManager;

import yescomment.persistence.ArticleManager;
import yescomment.persistence.CommentManager;

public class CommentManagerMock extends CommentManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public CommentManagerMock(EntityManager em) {
		this.em=em;
	}
	
	public void setArticleManager(ArticleManager articleManager) {
		this.articleManager=articleManager;
	}
	


	

}
