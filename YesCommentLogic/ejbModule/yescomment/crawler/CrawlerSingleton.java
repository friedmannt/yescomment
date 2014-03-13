package yescomment.crawler;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import yescomment.crawler.nocommentdetector.NoCommentDetector;
import yescomment.crawler.nocommentdetector.NoCommentDetectorService;
import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ArticleCommentPermission;
import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;

/**
 * Singleton, doing the crawl
 * 
 * @author Friedmann Tamás
 * 
 */
@Singleton
@Startup
public class CrawlerSingleton {

	@EJB
	ArticleManager articleManager;

	@Resource
	TimerService timerService;
	private static Logger LOGGER = Logger.getLogger("CrawlerSingleton");

	private List<CrawlerConfig> crawlerConfigs;

	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;

	

	

	/**
	 * Initializes document builder and factory
	 */
	@PostConstruct
	public void readConfig() {

		try {
			// init dom documentbuilder and factory
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			readCrawlerConfigs();
			
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears document builder and factory
	 */
	@PreDestroy
	public void clearConfig() {

		
			crawlerConfigs = null;
			documentBuilder = null;
			documentBuilderFactory = null;
			
			
		
	}

	/**
	 * Reads crawler config from a file
	 * 
	 * @throws JAXBException
	 */

	private void readCrawlerConfigs() throws JAXBException {

		// read from xml configuration

		String filePath = Thread.currentThread().getContextClassLoader().getResource("crawlerconfigs.xml").getFile();
		/* String filePath = "crawlerconfig.xml"; */
		crawlerConfigs = CawlerConfigXMLHandler.unmarshal(new File(filePath));

		LOGGER.info(String.format("Read crawler configs at startup : %s", crawlerConfigs));
	}

	
	
	public void crawl() {
		

			Calendar cal = Calendar.getInstance();
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			int currentMinute = cal.get(Calendar.MINUTE);

			LOGGER.info(String.format("Started crawling for hour %d minute %d",currentHour,currentMinute));
			List<CrawlerConfig> crawlerConfigsForCurrentHourAndMinute = getCrawlerConfigsForHourAndMinute(currentHour, currentMinute);
			for (CrawlerConfig crawlerConfig : crawlerConfigsForCurrentHourAndMinute) {
				if (crawlerConfig.getEnabled()) {
					crawlRss(crawlerConfig);
				}
			}
			LOGGER.info(String.format("Finished crawling for hour %d minute %d",currentHour,currentMinute));
		
	}

	/**
	 * Crawls for one crawler config
	 * 
	 * @param crawlerConfig
	 */
	private void crawlRss(CrawlerConfig crawlerConfig) {
		NoCommentDetector noCommentDetector = NoCommentDetectorService.getNoCommentDetector(crawlerConfig);

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
						crawlOneRSSItem(rssItem, noCommentDetector);
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
				crawlerTimerInfo.setNoCommentDetector(noCommentDetector);
				 timerService.createSingleActionTimer(crawlerTimerInfo.getDuration(), new TimerConfig(crawlerTimerInfo, false));
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
				crawlOneRSSItem(firstRssItem, crawlerTimerInfo.getNoCommentDetector());

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
	 * @param noCommentDetector
	 * @throws IOException
	 */
	private void crawlOneRSSItem(RSSItem rssItem, NoCommentDetector noCommentDetector) throws IOException {

		// we have to check, whether article exists, assumes rss link is final,
		// no unnecessary url params are added
		if (articleManager.getArticleByURL(rssItem.getLink()) == null) {
			LOGGER.finest(String.format("Crawling started for url: %s", rssItem.getLink()));
			ArticleInfo newArticleInfo = URLUtil.getArticleInfoFromURL(rssItem.getLink(), rssItem, noCommentDetector);
			// we should check, whether the final article url is
			// unique
			Article articleWithSameURL = articleManager.getArticleByURL(newArticleInfo.getFinalURL());
			if (articleWithSameURL == null) {
				// we should check, if comments are disabled. If no result, we
				// assume comments are disabled
				if (newArticleInfo.getArticleCommentPermission() == null || newArticleInfo.getArticleCommentPermission() == ArticleCommentPermission.NOT_ALLOWED) {

					Article article = articleManager.createArticleFromArticleInfo(newArticleInfo);
					 articleManager.create(article);
				}
			}
			LOGGER.finest(String.format("Crawling finished for url: %s", rssItem.getLink()));
		}

	}

	private List<CrawlerConfig> getCrawlerConfigsForHourAndMinute(final int hour, final int minute) {
		List<CrawlerConfig> crawlerConfigsForHourAndMinute = new ArrayList<CrawlerConfig>();
		for (CrawlerConfig crawlerConfig : crawlerConfigs) {
			if (crawlerConfig.getHour() == hour && crawlerConfig.getMinute() == minute) {
				crawlerConfigsForHourAndMinute.add(crawlerConfig);
			}
		}
		return crawlerConfigsForHourAndMinute;
	}

	/**
	 * TimerInfo class, for storing duration (delay for next timer), and rss
	 * items that should be read, and a possible nocommentdetector, which can be
	 * used for comment permission detection
	 * 
	 * @author Friedmann Tamás
	 * 
	 */
	private static class CrawlerTimerInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long duration;
		private List<RSSItem> rssItems;
		private NoCommentDetector noCommentDetector;

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

		public NoCommentDetector getNoCommentDetector() {
			return noCommentDetector;
		}

		public void setNoCommentDetector(NoCommentDetector noCommentDetector) {
			this.noCommentDetector = noCommentDetector;
		}

	}

}
