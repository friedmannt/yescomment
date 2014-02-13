package yescomment.crawler;

import javax.xml.bind.JAXBException;

public interface CrawlerSingletonMBean {
	
	public void startCrawler() throws JAXBException;
	
	public void stopCrawler();
	
	public boolean isCrawlerRunning();

}
