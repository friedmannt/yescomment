package yescomment.crawler;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
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
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.URLUtil;
import yescomment.util.URLUtil.ArticleInfo;

/**
 * Singleton, doing the crawl
 * 
 * @author Friedmann Tamás
 * 
 */
@Singleton
@Startup
public class CrawlerSingleton implements CrawlerSingletonMBean {

	@EJB
	ArticleManager articleManager;

	@Resource
	TimerService timerService;
	private static Logger LOGGER = Logger.getLogger("CrawlerSingleton");

	private List<CrawlerConfig> crawlerConfigs;

	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;

	private AtomicBoolean crawlerRuns = new AtomicBoolean(false);

	@Override
	public void startCrawler()  {
		
			crawlerRuns.set(true);
	}

	@Override
	public void stopCrawler() {
		
		crawlerRuns.set(false);

	}

	@Override
	public boolean isCrawlerRunning() {

		return crawlerRuns.get();
	}

	/**
	 * Initializes document builder and factory Registers singleton as mbean
	 */
	@PostConstruct
	public void readConfigAndRegisterMBean() {

		try {
			// init dom documentbuilder and factory
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			readCrawlerConfigs();
			ManagementFactory.getPlatformMBeanServer().registerMBean(this, new ObjectName("hu.yescomment", this.getClass().getSimpleName(), this.getClass().getSimpleName()));
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {

			e.printStackTrace();
		} catch (MBeanRegistrationException e) {

			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {

			e.printStackTrace();
		} catch (MalformedObjectNameException e) {

			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears document builder and factory UnRegisters singleton as mbean
	 */
	@PreDestroy
	public void clearCrawlerConfigAndUnregisterMBean() {

		try {
			documentBuilder = null;
			documentBuilderFactory = null;
			crawlerConfigs = null;
			ManagementFactory.getPlatformMBeanServer().unregisterMBean(new ObjectName("hu.yescomment", this.getClass().getSimpleName(), this.getClass().getSimpleName()));
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads crawler config from a file
	 * 
	 * @throws JAXBException
	 */

	private void readCrawlerConfigs() throws JAXBException {

		// read from xml configuration

		String filePath = Thread.currentThread().getContextClassLoader().getResource("crawlerconfig.xml").getFile();
		/* String filePath = "crawlerconfig.xml"; */
		crawlerConfigs = CawlerConfigXMLHandler.unmarshal(new File(filePath));

		LOGGER.info(String.format("Read crawler configs at startup : %s", crawlerConfigs));
	}

	/**
	 * crawl runs every minute, crawls rss if enabled, and set for current
	 * minute
	 */
	@Schedule(hour = "*", minute = "*", second = "0", persistent = false)
	public void crawl() {
		if (crawlerRuns.get()) {

			Calendar cal = Calendar.getInstance();
			int currentMinute = cal.get(Calendar.MINUTE);

			LOGGER.info("Started crawling for minute " + currentMinute);
			List<CrawlerConfig> crawlerConfigsForCurrentMinute = getCrawlerConfigsForMinute(currentMinute);
			for (CrawlerConfig crawlerConfig : crawlerConfigsForCurrentMinute) {
				if (crawlerConfig.getEnabled()) {
					crawlRss(crawlerConfig);
				}
			}
			LOGGER.info("Finished crawling for minute " + currentMinute);
		}
	}

	/**
	 * Crawls for one crawler config
	 * 
	 * @param crawlerConfig
	 */
	private void crawlRss(CrawlerConfig crawlerConfig) {
		// getting rss items from rss urls
		List<RSSItem> rssItems = null;
		try {
			rssItems = RSSLinkRetriever.getItemsFromRSS(crawlerConfig.getRssUrl(), documentBuilder);
		} catch (DOMException e) {

			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		}
		if (rssItems != null) {

			if (crawlerConfig.getDelaySec() == null || crawlerConfig.getDelaySec() == 0)
				// no delay, one loop gets all rss items
				for (RSSItem rssItem : rssItems) {

					try {
						crawlOneRSSItem(rssItem);
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
			else {
				// delay, one rss item at a time
				// rssitems are stored in the timer info
				CrawlerTimerInfo crawlerTimerInfo = new CrawlerTimerInfo();
				crawlerTimerInfo.setDuration(1000 * crawlerConfig.getDelaySec());
				crawlerTimerInfo.setRssItems(rssItems);
				Timer timer = timerService.createSingleActionTimer(crawlerTimerInfo.getDuration(), new TimerConfig(crawlerTimerInfo, false));
			}
		}

	}

	/**
	 * Crawls the first rss item, stored in the timer info, and removes it from
	 * the timer info
	 * 
	 * @param timer
	 */
	@Timeout
	public void crawlFirstRssItem(Timer timer) {

		CrawlerTimerInfo crawlerTimerInfo = (CrawlerTimerInfo) timer.getInfo();
		List<RSSItem> rssItems = crawlerTimerInfo.getRssItems();
		if (!rssItems.isEmpty()) {

			RSSItem firstRssItem = rssItems.remove(0);
			try {
				crawlOneRSSItem(firstRssItem);

			} catch (IOException e) {

				e.printStackTrace();
			}
			if (!rssItems.isEmpty()) {

				timerService.createSingleActionTimer(crawlerTimerInfo.getDuration(), new TimerConfig(crawlerTimerInfo, false));
			}

		}

	}

	/**
	 * Crawls one rss item Reads articleinfo, saves article
	 * 
	 * @param rssItem
	 * @throws IOException
	 */
	private void crawlOneRSSItem(RSSItem rssItem) throws IOException {

		// we have to check, whether article exists, assumes rss link is final,
		// no unnecessary url params are added
		if (articleManager.getArticleByURL(rssItem.getLink()) == null) {
			LOGGER.finest(String.format("Crawling started for url: %s", rssItem.getLink()));
			ArticleInfo newArticleInfo = URLUtil.getArticleInfoFromURL(rssItem.getLink());
			// we should check, whether the final article url is
			// unique
			Article articleWithSameURL = articleManager.getArticleByURL(newArticleInfo.getFinalURL());
			if (articleWithSameURL == null) {
				Article article = articleManager.createArticleFromArticleInfo(newArticleInfo);
				Article savedArticle = articleManager.create(article);
			}
			LOGGER.finest(String.format("Crawling finished for url: %s", rssItem.getLink()));
		}

	}

	private List<CrawlerConfig> getCrawlerConfigsForMinute(int minute) {
		List<CrawlerConfig> crawlerConfigsForMinute = new ArrayList<CrawlerConfig>();
		for (CrawlerConfig crawlerConfig : crawlerConfigs) {
			if (crawlerConfig.getMinute() == minute) {
				crawlerConfigsForMinute.add(crawlerConfig);
			}
		}
		return crawlerConfigsForMinute;
	}

	/**
	 * TimerInfo class, for storing duration (delay for next timer), and rss
	 * items that should be read
	 * 
	 * @author Friedmann Tamás
	 * 
	 */
	private static class CrawlerTimerInfo implements Serializable {
		private long duration;
		private List<RSSItem> rssItems;

		public long getDuration() {
			return duration;
		}

		public void setDuration(long duration) {
			this.duration = duration;
		}

		public List<RSSItem> getRssItems() {
			return rssItems;
		}

		public void setRssItems(List<RSSItem> rssItems) {
			this.rssItems = rssItems;
		}

	}

}
