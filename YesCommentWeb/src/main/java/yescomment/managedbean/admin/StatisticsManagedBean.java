package yescomment.managedbean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.listener.ActiveSessionListener;
import yescomment.persistence.ArticleManager;
import yescomment.persistence.CommentManager;
import yescomment.persistence.NewsManager;
import yescomment.persistence.UserManager;

@ManagedBean
@ViewScoped
@RolesAllowed("yescommentadmin")
public class StatisticsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	ArticleManager articleManager;
	
	@EJB
	CommentManager commentManager;
	
	
	
	@EJB
	UserManager userManager;
	
	@EJB
	NewsManager newsManager;
	private int articleCount;
	private int commentCount;
	private int userCount;
	
	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	@PostConstruct 
	public void init() {
		articleCount=articleManager.count();
		commentCount=commentManager.count();
		userCount=userManager.count();
	}

	public int getActiveSessionCount() {
		return ActiveSessionListener.getActivesessions().size();
	}
	
	public List<ActiveSessionListener.SessionData> getActiveSessionData() {
		return new ArrayList<ActiveSessionListener.SessionData> (ActiveSessionListener.getActivesessions().values());
	}
	
	
}
