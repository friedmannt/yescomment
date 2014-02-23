package yescomment.crawler;


public interface CrawlerSingletonMBean {
	
	public void startCrawler();
	
	public void stopCrawler();
	
	public boolean isCrawlerRunning();

}
