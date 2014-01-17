package yescomment.managedbean;

import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UserSessionBean {

	
	private String loginUserName;

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	
	private Set<Long> votedCommentIds=new HashSet<Long>();

	public Set<Long> getVotedCommentIds() {
		return votedCommentIds;
	}

	public void setVotedCommentIds(Set<Long> votedCommentIds) {
		this.votedCommentIds = votedCommentIds;
	}

	
	
	
	
}
