package yescomment.crawler;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/**
 * Singleton for managing crawler status
 * 
 * @author Friedmann Tamás
 * 
 */
@Singleton
@Startup
@LocalBean
public class CrawlerStatusSingleton implements CrawlerStatusSingletonMBean {

	private static Logger LOGGER = Logger.getLogger("CrawlerStatusSingleton");

	@Resource
	TimerService timerService;

	Timer timer;

	final TimerConfig tc = new TimerConfig("Crawler timer for every minute", false);

	
	@EJB
	CrawlerSingleton crawlerSingleton;
	
	@Override
	@Lock(LockType.WRITE)
	public void startCrawler() {
		if (!isCrawlerRunning()) {
			LOGGER.info("Crawler start");

			//in every minute
			ScheduleExpression scheduleExpression = new ScheduleExpression();
			scheduleExpression.hour("*");
			scheduleExpression.minute("*");
			scheduleExpression.second(0);

			timer = timerService.createCalendarTimer(scheduleExpression, tc);
			

		}
	}

	@Override
	@Lock(LockType.WRITE)
	public void stopCrawler() {
		if (isCrawlerRunning()) {
			LOGGER.info("Crawler stop");
			timer.cancel();
			timer = null;
			
		}

	}

	@Override
	@Lock(LockType.READ)
	public boolean isCrawlerRunning() {

		return timer != null;
	}

	@Override
	@Lock(LockType.READ)
	public Date getNextTimeout() {

		return timer == null ? null : timer.getNextTimeout();
	}

	@PostConstruct
	public void registerMBean() {

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
	public void unregisterMBean() {

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

	@Timeout()
	public void crawl() {
		crawlerSingleton.crawl();

		

	}

}
