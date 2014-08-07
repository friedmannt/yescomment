package yescomment.managedbean;

import java.io.Serializable;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import yescomment.model.User;
import yescomment.persistence.UserManager;
import yescomment.util.AnonymusUserName;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@ViewScoped
public class RegistrationManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ManagedProperty(value = "#{userSessionBean}")
	private UserSessionBean userSessionBean;


	
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}



	
	@EJB
	UserManager userManager;

	private String userName;
	
	private String password1;
	
	private String password2;
	
	private boolean registrationSuccessful; 
	
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public boolean isRegistrationSuccessful() {
		return registrationSuccessful;
	}

	public void setRegistrationSuccessful(boolean registrationSuccessful) {
		this.registrationSuccessful = registrationSuccessful;
	}

	
	

	
	

	public void registration() {
		assert(userSessionBean.getCaptchaWrapper().isCaptchaCorrectlyAnswered());
		Locale locale=JSFUtil.getLocale();
		User alreadyExistingUser=userManager.getUserByName(userName);
		boolean errorOccurred=false;
		if (alreadyExistingUser!=null) {
			FacesContext.getCurrentInstance().addMessage("registrationform:username", new FacesMessage(FacesMessage.SEVERITY_ERROR	,LocalizationUtil.getTranslation("username_in_use", locale), null));
			errorOccurred=true;
			
		}
		if (userName.equals(AnonymusUserName.ANONYMUS_USER_NAME)) {
			FacesContext.getCurrentInstance().addMessage("registrationform:username", new FacesMessage(FacesMessage.SEVERITY_ERROR	,LocalizationUtil.getTranslation("username_not_allowed", locale), null));
			errorOccurred=true;
		}
		if (!password1.equals(password2)) {
			FacesContext.getCurrentInstance().addMessage("registrationform:password1", new FacesMessage(FacesMessage.SEVERITY_ERROR	,LocalizationUtil.getTranslation("password_mismatch", locale), null));
			FacesContext.getCurrentInstance().addMessage("registrationform:password2", new FacesMessage(FacesMessage.SEVERITY_ERROR	,LocalizationUtil.getTranslation("password_mismatch", locale), null));
			errorOccurred=true;
		}
		
		if (!errorOccurred) {
			userManager.registerUser(userName, password1);
			registrationSuccessful=true;	
		}
		
	}
	
	public static String getMaxUsernameSize() {
		return Integer.toString(User.MAX_USERNAME_SIZE);
	}
	

	
	
}
