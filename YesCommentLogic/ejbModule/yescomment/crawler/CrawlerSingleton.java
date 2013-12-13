package yescomment.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBException;

/**
 * Singleton, doing the crawl
 * @author Friedmann Tamás
 *
 */
@Singleton
@Startup
public class CrawlerSingleton {
	
	private static Logger LOGGER = Logger.getLogger("CrawlerSingleton");

	private List<CrawlerConfig> crawlerConfigs=new ArrayList<CrawlerConfig>();
	
	
	@PostConstruct
	public void readCrawlerConfigs() {
		//read from xml configuration
		try {
		    String filePath =Thread.currentThread().getContextClassLoader().getResource("crawlerconfig.xml").getFile();  
            crawlerConfigs = CawlerConfigXMLHandler.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
		LOGGER.info(String.format("Read crawler configs at startup : %s", crawlerConfigs));
	}

	/**
	 * assumes it runs every minute
	 */
	@Schedule(hour="*", minute="*", second="0",persistent=false)
	public void crawl() {
		Calendar cal=Calendar.getInstance();
		int currentMinute=cal.get(Calendar.MINUTE);
		
		LOGGER.info("Started crawling for minute "+currentMinute);
		List<CrawlerConfig> crawlerConfigsForCurrentMinute=getCrawlerConfigsForMinute(currentMinute);
		for (CrawlerConfig crawlerConfig:crawlerConfigsForCurrentMinute) {
			crawlRss(crawlerConfig.getRssUrl());
		}
		LOGGER.info("Finished crawling for minute "+currentMinute);
	}

	private void crawlRss(String rssUrl) {
		// TODO Auto-generated method stub
		
	}

	private List<CrawlerConfig> getCrawlerConfigsForMinute(int minute) {
		List<CrawlerConfig> crawlerConfigsForMinute=new ArrayList<CrawlerConfig>();
		for (CrawlerConfig crawlerConfig:crawlerConfigs) {
			if (crawlerConfig.getMinute()==minute) {
				crawlerConfigsForMinute.add(crawlerConfig);
			}
		}
		return crawlerConfigsForMinute;
	}
	
}
