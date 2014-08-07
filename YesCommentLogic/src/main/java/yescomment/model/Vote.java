package yescomment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import yescomment.util.VoteDirection;

/**
 * Entity implementation class for Entity: Comment
 * 
 */
@Entity
@Table(name="yescomment_vote")
public class Vote extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	@ManyToOne(optional = false)
	private Comment comment;
	
	public static final int MAX_USERNAME_SIZE=16;
	@Column(nullable=false,length = MAX_USERNAME_SIZE)
	@Size(max = MAX_USERNAME_SIZE)
	@NotNull
	private String userName;

	@Enumerated(EnumType.STRING)
	private VoteDirection voteDirection;

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public VoteDirection getVoteDirection() {
		return voteDirection;
	}

	public void setVoteDirection(VoteDirection voteDirection) {
		this.voteDirection = voteDirection;
	}
	
	
	
}
