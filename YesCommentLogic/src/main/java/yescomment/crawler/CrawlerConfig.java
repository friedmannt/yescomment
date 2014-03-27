package yescomment.crawler;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Class for holding crawler configuration
 * @author Friedmann Tamás
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="crawlerconfig")
public class CrawlerConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * RSS is only crawled if enabled
	 */
	@XmlAttribute(required=true)
	@NotNull
	private Boolean enabled;
	/**
	 * Name of the config
	 */
	@XmlAttribute(required=true)
	@NotNull
	private String name;
	
	/**
	 * URL of the rss file
	 */
	@XmlAttribute (required=true)
	@NotNull
	private String rssUrl;
	
	/**
	 * fetches rss only if hour in day is this. Mandatory
	 */
	@XmlAttribute(required=true)
	@NotNull
	@Min(0)
	@Max(23)
	private Integer hour;
	
	/**
	 * fetches rss only if minute in hour is this. Mandatory
	 */
	@XmlAttribute(required=true)
	@NotNull
	@Min(0)
	@Max(59)
	private Integer minute;
	/**
	 * delays between two rss items. If null, there is no delay
	 */
	@XmlAttribute(required=false)
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
	
	
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
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
		return "CrawlerConfig [enabled=" + enabled + ", name=" + name + ", rssUrl=" + rssUrl + ", hour=" + hour + ", minute=" + minute + ", delaySec=" + delaySec + "]";
	}
	
	
	
	
}
