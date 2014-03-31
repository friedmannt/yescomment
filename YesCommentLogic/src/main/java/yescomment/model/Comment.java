package yescomment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: Comment
 * 
 */
@Entity
@Table(name="yescomment_comment")
public class Comment extends AbstractEntity {

	
	@ManyToOne(optional = false)
	private Article article;
	
	public static final int MAX_COMMENT_SIZE=10000;
	@Column(nullable = false, length = MAX_COMMENT_SIZE)
	@NotNull
	@Size(max = MAX_COMMENT_SIZE)
	/*@Lob*/ /*JBOSS does it bad*/
	private String commentText;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@NotNull
	private Date commentDate;
	
	public static final int MAX_AUTHOR_SIZE=64;
	@Column(nullable=false,length = MAX_AUTHOR_SIZE)
	@Size(max = MAX_AUTHOR_SIZE)
	@NotNull
	private String author;
	
	private Integer plusCount=0;
	
	private Integer minusCount=0;
	
	private static final long serialVersionUID = 1L;

	
	public String getCommentText() {
		return this.commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getPlusCount() {
		return plusCount;
	}

	public void setPlusCount(Integer plusCount) {
		this.plusCount = plusCount;
	}

	public Integer getMinusCount() {
		return minusCount;
	}

	public void setMinusCount(Integer minusCount) {
		this.minusCount = minusCount;
	}

}
