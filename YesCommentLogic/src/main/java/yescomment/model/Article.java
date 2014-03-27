package yescomment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: Article
 *
 */
@Entity
@Table(name="yescomment_article")
public class Article extends AbstractEntity {

	   
	@Column(nullable=false,length=256)
	@NotNull
	@Size( max=256)
	private String title;
	
	@Column(nullable=true,length=2048)
	@Size( max=2048)
	private String description;
	
	public static final int MAX_URL_SIZE=2048;
	@Column(nullable=false,length=MAX_URL_SIZE,unique=true)
	@NotNull
	@Size(max=MAX_URL_SIZE)
	private String url;
	

	
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true, mappedBy="article",fetch=FetchType.LAZY)
	private List<Comment> comments=new ArrayList<Comment>();

	@Column(nullable=false)
	@NotNull
	private Integer commentCount;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> keywords=new HashSet<String>();
	
	@Column(nullable=true,length=2048)
	@Size(max=2048)
	private String imageurl;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	@NotNull
	private Date registrationDate;
	
	
	
	
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
	
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}


		
}
