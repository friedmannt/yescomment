package yescomment.util;

import java.io.Serializable;

public class ArticleInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String finalURL;
	private String imageURL;
	private String title;
	private String description;
	private String keywords;
	private String language;
	
	

	public String getFinalURL() {
		return finalURL;
	}

	public void setFinalURL(String finalURL) {
		this.finalURL = finalURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "ArticleInfo [finalURL=" + finalURL + ", imageURL=" + imageURL + ", title=" + title + ", description=" + description + ", keywords=" + keywords + ", language=" + language + "]";
	}

	
	
	
}
