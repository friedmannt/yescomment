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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
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
public class CrawlerSingleton {

	@EJB
	ArticleManager articleManager;

	@Resource
	TimerService timerService;
	private static Logger LOGGER = Logger.getLogger("CrawlerSingleton");

	private List<CrawlerConfig> crawlerConfigs = new ArrayList<CrawlerConfig>();

	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;

	@PostConstruct
	public void readCrawlerConfigs() {
		// init dom documentbuilder and factory
		documentBuilderFactory = DocumentBuilderFactory.newInstance();

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {

			pce.printStackTrace();
		}
		// read from xml configuration
		try {
			String filePath = Thread.currentThread().getContextClassLoader()
					.getResource("crawlerconfig.xml").getFile();
			/*String filePath = "crawlerconfig.xml"; */
			crawlerConfigs = CawlerConfigXMLHandler
					.unmarshal(new File(filePath));
		} catch (JAXBException je) {
			je.printStackTrace();
		}
		LOGGER.info(String.format("Read crawler configs at startup : %s",
				crawlerConfigs));
	}

	/**
	 * assumes it runs every minute
	 */
	@Schedule(hour = "*", minute = "*", second = "0", persistent = false)
	public void crawl() {
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

	private void crawlRss(CrawlerConfig crawlerConfig) {
		// getting rss items from rss urls
		List<RSSItem> rssItems = null;
		try {
			rssItems = RSSLinkRetriever.getItemsFromRSS(
					crawlerConfig.getRssUrl(), documentBuilder);
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

			if (crawlerConfig.getDelaySec() == null
					|| crawlerConfig.getDelaySec() == 0)
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

				CrawlerTimerInfo crawlerTimerInfo = new CrawlerTimerInfo();
				crawlerTimerInfo
						.setDuration(1000 * crawlerConfig.getDelaySec());
				crawlerTimerInfo.setRssItems(rssItems);
				Timer timer = timerService.createSingleActionTimer(
						crawlerTimerInfo.getDuration(), new TimerConfig(
								crawlerTimerInfo, false));
			}
		}

	}

	@Timeout
	public void crawlFirstRssItem(Timer timer) {
		System.out.println("Timeout");
		CrawlerTimerInfo crawlerTimerInfo = (CrawlerTimerInfo) timer.getInfo();
		List<RSSItem> rssItems = crawlerTimerInfo.getRssItems();
		if (!rssItems.isEmpty()) {

			RSSItem firstRssItem = rssItems.remove(0);
			try {
				System.out.println(firstRssItem);
				crawlOneRSSItem(firstRssItem);

			} catch (IOException e) {

				e.printStackTrace();
			}
			if (!rssItems.isEmpty()) {

				timerService.createSingleActionTimer(crawlerTimerInfo
						.getDuration(),
						new TimerConfig(crawlerTimerInfo, false));
			}

		}

	}

	private void crawlOneRSSItem(RSSItem rssItem) throws IOException {
		// we have to check, whether article exists
		if (articleManager.getArticleByURL(rssItem.getLink()) == null) {

			ArticleInfo newArticleInfo = URLUtil.getArticleInfoFromURL(rssItem
					.getLink());
			newArticleInfo.setCreateDate(rssItem.getPubDate());
			// we should check, whether the final article url is
			// unique
			Article articleWithSameURL = articleManager
					.getArticleByURL(newArticleInfo.getFinalURL());
			if (articleWithSameURL == null) {
				Article article = articleManager
						.createArticleFromArticleInfo(newArticleInfo);
				Article savedArticle = articleManager.create(article);
			}

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

	static class CrawlerTimerInfo implements Serializable {
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
