package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import yescomment.managedbean.captcha.CaptchaOption;
import yescomment.managedbean.captcha.CaptchaOption.Color;
import yescomment.model.Comment;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;
import yescomment.util.NumberUtil;

@ManagedBean
@SessionScoped
public class LoginManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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


	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}




	public void tryToLogin()  {
		
		boolean success = true;
		if (name==null||name.length()==0) {
			success = false;
			FacesContext.getCurrentInstance().addMessage("loginform:name", new FacesMessage(FacesMessage.SEVERITY_ERROR,LocalizationUtil.getTranslation("empty_name", JSFUtil.getLocale()), null));
		}
		if (selectedCaptchaOption==null||!selectedCaptchaOption.equals(correctCaptchaOption)) {
			success = false;
			FacesContext.getCurrentInstance().addMessage("loginform:captcha", new FacesMessage(FacesMessage.SEVERITY_ERROR,LocalizationUtil.getTranslation("incorrect_captcha_answer", JSFUtil.getLocale()), null));
		}
		

		if (success) {
			userSessionBean.setLoginUserName(name);
		}
		
	}

	
	
	@PostConstruct
	public void initialize() {
		
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
