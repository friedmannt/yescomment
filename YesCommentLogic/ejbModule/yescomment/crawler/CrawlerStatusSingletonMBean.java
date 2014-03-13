package yescomment.crawler;

import java.util.Date;


public interface CrawlerStatusSingletonMBean {
	
	public void startCrawler();
	
	public void stopCrawler();
	
	public boolean isCrawlerRunning();
	
	public Date getNextTimeout();

}
