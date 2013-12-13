package yescomment.crawler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="crawlerconfig")
public class CrawlerConfig {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String rssUrl;
	@XmlAttribute
	private int minute;//fetches rss only if minute in hour is this
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRssUrl() {
		return rssUrl;
	}
	public void setRssUrl(String rssUrl) {
		this.rssUrl = rssUrl;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	@Override
	public String toString() {
		return "CrawlerConfig [name=" + name + ", rssUrl=" + rssUrl
				+ ", minute=" + minute + "]";
	}
	
}
