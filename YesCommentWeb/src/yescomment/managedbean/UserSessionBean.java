package yescomment.managedbean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UserSessionBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loginUserName;

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	
	private Set<String> votedCommentIds=new HashSet<String>();

	public Set<String> getVotedCommentIds() {
		return votedCommentIds;
	}

	public void setVotedCommentIds(Set<String> votedCommentIds) {
		this.votedCommentIds = votedCommentIds;
	}

	
	
	
	
}
