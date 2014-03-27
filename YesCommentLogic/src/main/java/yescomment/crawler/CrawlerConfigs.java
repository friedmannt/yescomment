package yescomment.crawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crawlerconfigs")
public class CrawlerConfigs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CrawlerConfigs() {
	}

	public CrawlerConfigs(List<CrawlerConfig> crawlerConfigs) {
		this.crawlerConfigs = crawlerConfigs;
	}

	@XmlElement(name = "crawlerconfig", type = CrawlerConfig.class)
	private List<CrawlerConfig> crawlerConfigs = new ArrayList<CrawlerConfig>();

	public List<CrawlerConfig> getCrawlerConfigs() {
		return crawlerConfigs;
	}

	public void setCrawlerConfigs(List<CrawlerConfig> crawlerConfigs) {
		this.crawlerConfigs = crawlerConfigs;
	}

	@Override
	public String toString() {
		return "CrawlerConfigs [crawlerConfigs=" + crawlerConfigs + "]";
	}
	
	

}