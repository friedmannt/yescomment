package yescomment.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
	/*@Lob openshift:postgresql cannot handle*/
	private String commentText;
	
	
	
	public static final int MAX_AUTHOR_SIZE=16;
	@Column(nullable=false,length = MAX_AUTHOR_SIZE)
	@Size(max = MAX_AUTHOR_SIZE)
	@NotNull
	private String author;
	
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	private Comment replyOf;
	
	
	private Integer upVoteCount=0;
	
	private Integer downVoteCount=0;
	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "comment", fetch = FetchType.LAZY)
	private Set<Vote> votes;

	/**
	 * Hidden comment is not visible for users
	 */
	@NotNull
	public Boolean hidden=false;
	
	
	
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

	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Comment getReplyOf() {
		return replyOf;
	}

	public void setReplyOf(Comment replyOf) {
		this.replyOf = replyOf;
	}

	
	public Integer getUpVoteCount() {
		return upVoteCount;
	}

	public void setUpVoteCount(Integer upVoteCount) {
		this.upVoteCount = upVoteCount;
	}

	public Integer getDownVoteCount() {
		return downVoteCount;
	}

	public void setDownVoteCount(Integer downVoteCount) {
		this.downVoteCount = downVoteCount;
	}

	

	public Set<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

}
