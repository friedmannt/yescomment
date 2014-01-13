package yescomment.crawler;

import java.io.Serializable;
import java.util.Date;

public class RSSItem implements Serializable{

	private String link;
	private Date pubDate;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Date getPubDate() {
		return pubDate;
	}
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	@Override
	public String toString() {
		return "RSSItem [link=" + link + ", pubDate=" + pubDate + "]";
	}
	
}
