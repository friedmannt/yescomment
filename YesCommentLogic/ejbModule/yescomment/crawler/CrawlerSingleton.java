package yescomment.crawler;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
			crawlRss(crawlerConfig.getRssUrl());
		}
		LOGGER.info("Finished crawling for minute " + currentMinute);
	}

	private void crawlRss(String rssUrl) {
		try {
			List<String> urls = RSSLinkRetriever.getItemLinksFromRSS(rssUrl,
					documentBuilder);
			for (String url : urls) {

				// we have to check, whether article exists
				if (articleManager.getArticleByURL(url) == null) {

					try {
						ArticleInfo newArticleInfo = URLUtil
								.getArticleInfoFromURL(url);

						// we should check, whether the final article url is
						// unique
						Article articleWithSameURL = articleManager
								.getArticleByURL(newArticleInfo.getFinalURL());
						if (articleWithSameURL == null) {
							Article article = articleManager
									.createArticleFromArticleInfo(newArticleInfo);
							Article savedArticle = articleManager.create(article);
						}

					} catch (IOException e) {

						e.printStackTrace();
					}
				}

			}
		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
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

}
