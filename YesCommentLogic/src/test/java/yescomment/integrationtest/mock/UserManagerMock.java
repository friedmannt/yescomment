package yescomment.integrationtest.mock;

import javax.persistence.EntityManager;

import yescomment.persistence.UserManager;

public class UserManagerMock extends UserManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public UserManagerMock(EntityManager em) {
		this.em=em;
	}
	



	

}
