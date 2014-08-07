package yescomment.managedbean;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import yescomment.captcha.CaptchaWrapper;

@ManagedBean
@SessionScoped
public class UserSessionBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	private Date lastCommentDate;


	public Date getLastCommentDate() {
		return lastCommentDate;
	}

	public void setLastCommentDate(Date lastCommentDate) {
		this.lastCommentDate = lastCommentDate;
	}
	
	
	private Date lastArticleImportDate;


	public Date getLastArticleImportDate() {
		return lastArticleImportDate;
	}

	public void setLastArticleImportDate(Date lastArticleImportDate) {
		this.lastArticleImportDate = lastArticleImportDate;
	}


	private Date lastCaptchaAnswerDate;
	
	public Date getLastCaptchaAnswerDate() {
		return lastCaptchaAnswerDate;
	}

	public void setLastCaptchaAnswerDate(Date lastCaptchaAnswerDate) {
		this.lastCaptchaAnswerDate = lastCaptchaAnswerDate;
	}


	//captcha is stored in session
	private CaptchaWrapper captchaWrapper=new CaptchaWrapper();
	
	public CaptchaWrapper getCaptchaWrapper() {
		return captchaWrapper;
	}

	public void setCaptchaWrapper(CaptchaWrapper captchaWrapper) {
		this.captchaWrapper = captchaWrapper;
	}
	
	

	
	
	
	
	
	
	
	
}
