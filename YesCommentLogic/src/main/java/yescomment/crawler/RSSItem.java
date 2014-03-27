package yescomment.crawler;

import java.io.Serializable;
import java.util.Date;
/**
 * One rss item, has many more attributes, now only link and pubdate are stored, content is not stored
 * @author Friedmann Tamás
 *
 */
public class RSSItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String link;
	private Date pubDate;
	private String title;
	private String description;
	
	
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
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "RSSItem [link=" + link + ", pubDate=" + pubDate + ", title=" + title + "]";
	}
	
}
