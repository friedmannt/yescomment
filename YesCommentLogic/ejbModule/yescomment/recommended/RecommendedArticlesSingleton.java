package yescomment.recommended;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.ArticleManager;

/**
 * Singleton,retrieving recommended articles. Does not cache, managable via MBean management
 * 
 * @author Friedmann Tamás
 * 
 */
@Singleton
@Startup
public class RecommendedArticlesSingleton implements RecommendedArticlesSingletonLocal, RecommendedArticlesSingletonMBean {

	
	private int latestArticleLimit;
	
	private int mostCommentedArticleLimit;
	
	
	
	@Override
	public int getLatestArticleLimit() {
		return latestArticleLimit;
	}

	@Override
	public void setLatestArticleLimit(int latestArticleLimit) {
		this.latestArticleLimit = latestArticleLimit;
	}
	@Override
	public int getMostCommentedArticleLimit() {
		return mostCommentedArticleLimit;
	}
	@Override
	public void setMostCommentedArticleLimit(int mostCommentedArticleLimit) {
		this.mostCommentedArticleLimit = mostCommentedArticleLimit;
	}

	@EJB
	ArticleManager articleManager;

	private List<Long> articleIdsOrderedByRegistrationDate;// holds the article
												// the top commented

	@PostConstruct
	public void initializeArticleIdCache() {
		latestArticleLimit=10;//initial
		mostCommentedArticleLimit=10;//initial
	
		try {
			ManagementFactory.getPlatformMBeanServer().registerMBean(this, new ObjectName("hu.yescomment", this.getClass().getSimpleName(), this.getClass().getSimpleName()));
		} catch (InstanceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (MBeanRegistrationException e) {

			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {

			e.printStackTrace();
		} catch (MalformedObjectNameException e) {

			e.printStackTrace();
		}

	}

	@PreDestroy
	public void clearArticleIdCache() {

		try {
			ManagementFactory.getPlatformMBeanServer().unregisterMBean(new ObjectName("hu.yescomment", this.getClass().getSimpleName(), this.getClass().getSimpleName()));
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
	
	}

	
	
	@Override
	public List<Article> retrieveRecommendedArticles(RecommendationAscpect recommendationAscpect) {
		
		switch (recommendationAscpect) {
		case LATEST: {
			return articleManager.getEntitiesOrdered("registrationDate", false, latestArticleLimit);
		}
		case MOSTCOMMENTED: {
			return articleManager.getEntitiesOrdered("commentCount", false, latestArticleLimit);
		}

		default:
			return null;

		}


	}

}