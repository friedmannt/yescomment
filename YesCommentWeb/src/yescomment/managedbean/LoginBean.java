package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {



	private Long viewedArticleId;

	public Long getViewedArticleId() {
		return viewedArticleId;
	}

	public void setViewedArticleId(Long viewedArticleId) {
		this.viewedArticleId = viewedArticleId;
	}

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String testCaptcha() {
		
		FacesContext.getCurrentInstance()
		.getExternalContext().getSessionMap().put("login_username", userName);
		return "/viewarticle.xhtml?faces-redirect=true&id="+viewedArticleId;
	}

	

	@PostConstruct
	public void getViewedArticleIdFromFlash() {
		viewedArticleId = (Long) FacesContext.getCurrentInstance()
				.getExternalContext().getFlash()
				.get("viewed_article_before_login");
	
	}

}
