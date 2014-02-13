package yescomment.crawler;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class for holding crawler configuration
 * @author Friedmann Tamás
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="crawlerconfig")
public class CrawlerConfig implements Serializable{

	/**
	 * RSS is only crawled if enabled
	 */
	@XmlAttribute
	@NotNull
	private Boolean enabled;
	/**
	 * Name of the config
	 */
	@XmlAttribute
	private String name;
	
	/**
	 * URL of the rss file
	 */
	@XmlAttribute
	@NotNull
	private String rssUrl;
	/**
	 * fetches rss only if minute in hour is this. Mandatory
	 */
	@XmlAttribute
	@NotNull
	private Integer minute;
	/**
	 * delays between two rss items. If null, there is no delay
	 */
	@XmlAttribute
	private Integer delaySec;
	
	
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
