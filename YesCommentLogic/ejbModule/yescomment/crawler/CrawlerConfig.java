package yescomment.crawler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="crawlerconfig")
public class CrawlerConfig {

	@XmlAttribute
	private Boolean enabled;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String rssUrl;
	@XmlAttribute
	private Integer minute;//fetches rss only if minute in hour is this
	@XmlAttribute
	private Integer delaySec;//delays between two rss items
	
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
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
	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Integer getDelaySec() {
		return delaySec;
	}
	public void setDelaySec(Integer delaySec) {
		this.delaySec = delaySec;
	}
	@Override
	public String toString() {
		return "CrawlerConfig [name=" + name + ", rssUrl=" + rssUrl
				+ ", minute=" + minute  + ", delaySec=" + delaySec + "]";
	}
	
	
	
}
