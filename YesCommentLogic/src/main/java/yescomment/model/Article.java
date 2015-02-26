package yescomment.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Article
 * 
 */
@Entity
@Table(name = "yescomment_article")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Article extends AbstractEntity {

	
	
	
	public Article() {
		super();
	}

	@Column(nullable = false, length = 256)
	@NotNull
	@Size(max = 256)
	@XmlElement(required=true)
	private String title;

	public static final int MAX_DESCRIPTION_SIZE=2048;
	@Column(nullable = true, length = MAX_DESCRIPTION_SIZE)
	@Size(max = MAX_DESCRIPTION_SIZE)
	
	private String description;

	public static final int MAX_URL_SIZE = 2048;
	@Column(nullable = false, length = MAX_URL_SIZE, unique = true)
	@NotNull
	@Size(max = MAX_URL_SIZE)
	@XmlElement(required=true)
	private String url;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "article", fetch = FetchType.LAZY)
	private Set<Comment> comments = new HashSet<Comment>();

	@Column(nullable = false)
	@NotNull
	@XmlElement(required=true)
	@Min(0)
	private Integer commentCount=0;
	
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	
	private Date lastCommentDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="yescomment_article_keywords")
	private Set<String> keywords = new HashSet<String>();

	@Column(nullable = true, length = 2048)
	@Size(max = 2048)
	private String imageurl;

	@Column(nullable = true, length = 2)
	@Size(max = 2)
	private String language;

	
	private static final long serialVersionUID = 1L;

	public String getTitle() {
		return this.title;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Date getLastCommentDate() {
		return lastCommentDate;
	}

	public void setLastCommentDate(Date lastCommentDate) {
		this.lastCommentDate = lastCommentDate;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	
	

}
