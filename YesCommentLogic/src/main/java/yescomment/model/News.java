package yescomment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: News
 * 
 */
@Entity
@Table(name = "yescomment_news")

public class News extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 256)
	@NotNull
	@Size(max = 256)
	private String title;
	
	public static final int MAX_NEWS_SIZE=4000;
	@Column(nullable = false, length = MAX_NEWS_SIZE)
	@NotNull
	@Size(max = MAX_NEWS_SIZE)
	private String newsText;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsText() {
		return newsText;
	}

	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}

	
	
	

	
}
