package yescomment.integrationtest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import yescomment.integrationtest.mock.UserManagerMock;
import yescomment.model.User;

public class UserPersistenceIT extends AbstractPersistenceIT {

	UserManagerMock userManager;

	@Before
	public void initMocks() {
		super.initMocks();
		userManager = new UserManagerMock(em);
	}

	@After
	public void clearMocks() {
		
		removeAllUsers();
		userManager = null;
		super.clearMocks();
	}
	
	protected void removeAllUsers() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			
			List<String> allUserIds = userManager.findAllIds();
			for (String id : allUserIds) {
				User user = userManager.find(id);
				userManager.remove(user);
			}
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}

	}
	
	
	@Test
	public void testUsers() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			userManager.registerUser("pisti", "pisti");
			User user=userManager.getUserByName("pisti");
			assertNotNull(user);
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}
	
	
	@Test
	public void testMultipleUsers() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			userManager.registerUser("pisti", "pisti");
			userManager.registerUser("laci", "laci");
			userManager.registerUser("nándi", "nándi");
			assertEquals(3, userManager.count());
			List<User> allUsers=userManager.findAll();
			assertEquals(3,allUsers.size());
			List<String> ids= new ArrayList<String>();
			for (User user:allUsers) {
				ids.add(user.getId());
			}
			List<User> allUsersByIds=userManager.find(ids);
			assertEquals(allUsersByIds, allUsers);
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

}
