package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import yescomment.managedbean.captcha.CaptchaOption;
import yescomment.managedbean.captcha.CaptchaOption.Color;
import yescomment.model.Comment;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;
import yescomment.util.NumberUtil;

@ManagedBean
@ViewScoped
public class LoginManagedBean implements Serializable {

	private static final int CAPTCHA_OPTION_SIZE = 3;// this many options are
														// presented

	public static int getCaptchaOptionSize() {
		return CAPTCHA_OPTION_SIZE;
	}

	
	
	
	public static Integer getMaxAuthorSize() {
		return Comment.MAX_AUTHOR_SIZE;
	}

	@ManagedProperty(value = "#{userSessionBean}")
	private UserSessionBean userSessionBean;

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}


	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	
	public void tryToLogin()  {
		boolean success = true;
		if (selectedCaptchaOption==null||!selectedCaptchaOption.equals(correctCaptchaOption)) {
			success = false;
			FacesContext.getCurrentInstance().addMessage("loginform:captcha", new FacesMessage(FacesMessage.SEVERITY_ERROR,"sumc", "detc"));
		}
		

		if (success) {
			if (userName == null||userName.length()==0) {
				userName="Anonymus";
			}
			userSessionBean.setLoginUserName(userName);
		}
		
	}

	
	
	@PostConstruct
	public void initializeCaptcha() {
		correctCaptchaOption = new CaptchaOption(
				CaptchaOption.Color.values()[NumberUtil.getRandomInt(6)],
				NumberUtil.getRandomInt(100));
		int numberOfCorrectCaptchaOption = NumberUtil
				.getRandomInt(CAPTCHA_OPTION_SIZE);
		// fill options
		for (int i = 0; i < CAPTCHA_OPTION_SIZE; i++) {
			if (i == numberOfCorrectCaptchaOption) {
				possibleCaptchaOptions[i] = new CaptchaOption(
						correctCaptchaOption.getColor(),
						correctCaptchaOption.getNumber());
			} else {
				possibleCaptchaOptions[i] = new CaptchaOption(
						CaptchaOption.Color.values()[NumberUtil.getRandomInt(6)],
						NumberUtil.getRandomInt(100));
				// TODO ensure, no options are the same. If options are same,
				// continue randomize it
			}
		}
	}

	private CaptchaOption correctCaptchaOption;

	public CaptchaOption getCorrectCaptchaOption() {
		return correctCaptchaOption;
	}

	public void setCorrectCaptchaOption(CaptchaOption correctCaptchaOption) {
		this.correctCaptchaOption = correctCaptchaOption;
	}

	private CaptchaOption[] possibleCaptchaOptions = new CaptchaOption[CAPTCHA_OPTION_SIZE];

	public CaptchaOption[] getPossibleCaptchaOptions() {
		return possibleCaptchaOptions;
	}

	public void setPossibleCaptchaOptions(CaptchaOption[] possibleCaptchaOptions) {
		this.possibleCaptchaOptions = possibleCaptchaOptions;
	}

	private CaptchaOption selectedCaptchaOption;

	public CaptchaOption getSelectedCaptchaOption() {
		return selectedCaptchaOption;
	}

	public void setSelectedCaptchaOption(CaptchaOption selectedCaptchaOption) {
		this.selectedCaptchaOption = selectedCaptchaOption;
	}

	public String localizeColor(Color color) {
		return LocalizationUtil.getEnumTranslation(color, JSFUtil.getLocale());
	}
	
	public String localizeNumber(Integer number) {
		return LocalizationUtil.getNumberTranslation(number, JSFUtil.getLocale());
	}
}
