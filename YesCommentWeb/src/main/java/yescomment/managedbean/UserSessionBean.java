package yescomment.managedbean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import yescomment.model.Comment;

@ManagedBean
@SessionScoped
public class UserSessionBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private Set<String> votedCommentIds=new HashSet<String>();

	public Set<String> getVotedCommentIds() {
		return votedCommentIds;
	}

	public void setVotedCommentIds(Set<String> votedCommentIds) {
		this.votedCommentIds = votedCommentIds;
	}

	
	public int getMaxAuthorSize() {
		return Comment.MAX_AUTHOR_SIZE;
	}
	
	
	
	
}
