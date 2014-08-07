package yescomment.integrationtest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import yescomment.integrationtest.mock.AllKeywordsSingletonMock;
import yescomment.integrationtest.mock.ArticleManagerMock;
import yescomment.integrationtest.mock.ArticleSearcherMock;
import yescomment.integrationtest.mock.CommentManagerMock;
import yescomment.integrationtest.mock.VoteManagerMock;
import yescomment.model.Article;

public abstract class AbstractPersistenceIT {

	/** The factory that produces entity manager. */
	static EntityManagerFactory emf;
	/** The entity manager that persists and queries the DB. */
	static EntityManager em;

	@BeforeClass
	public static void initTestFixture() throws Exception {
		// Get the entity manager for the tests.
		emf = Persistence.createEntityManagerFactory("YesCommentTestPU");
		em = emf.createEntityManager();

	}

	/**
	 * Cleans up the session.
	 */
	@AfterClass
	public static void closeTestFixture() {
		em.close();
		emf.close();
	}

	ArticleManagerMock articleManager;
	CommentManagerMock commentManager;
	VoteManagerMock voteManager;
	AllKeywordsSingletonMock allKeywordsSingleton;
	
	ArticleSearcherMock articleSearcher;

	@Before
	public void initMocks() {
		articleManager = new ArticleManagerMock(em);
		commentManager = new CommentManagerMock(em);
		voteManager = new VoteManagerMock(em);
		allKeywordsSingleton = new AllKeywordsSingletonMock();
		
		articleSearcher = new ArticleSearcherMock(em);

		articleManager.setAllKeywordsSingleton(allKeywordsSingleton);
		commentManager.setArticleManager(articleManager);
		voteManager.setCommentManager(commentManager);
		allKeywordsSingleton.setArticleManager(articleManager);
		allKeywordsSingleton.populateKeywords();
	}

	@After
	public void clearMocks() {
		removeAllArticles();
		articleManager = null;
		commentManager = null;
		voteManager = null;
		allKeywordsSingleton = null;
		
		articleSearcher = null;
	}

	protected void removeAllArticles() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			List<String> allArticleIds = articleManager.findAllIds();
			for (String id : allArticleIds) {
				Article article = articleManager.find(id);
				articleManager.remove(article);//cascades to comment and vote
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

}
