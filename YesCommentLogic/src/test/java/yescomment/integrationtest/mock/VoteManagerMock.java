package yescomment.integrationtest.mock;

import javax.persistence.EntityManager;

import yescomment.persistence.CommentManager;
import yescomment.persistence.VoteManager;

public class VoteManagerMock extends VoteManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public VoteManagerMock(EntityManager em) {
		this.em=em;
	}
	
	public void setCommentManager(CommentManager commentManager) {
		this.commentManager=commentManager;
	}
	


	

}
