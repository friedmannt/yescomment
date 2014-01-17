package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

	@ManagedProperty(value="#{userSessionBean}")
	private UserSessionBean userSessionBean;

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}
	
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
		userSessionBean.setLoginUserName(userName);
		return "/viewarticle.xhtml?faces-redirect=true&id="+viewedArticleId;
	}

	

	@PostConstruct
	public void getViewedArticleIdFromFlash() {
		viewedArticleId = (Long) FacesContext.getCurrentInstance()
				.getExternalContext().getFlash()
				.get("viewed_article_before_login");
	
	}

}
