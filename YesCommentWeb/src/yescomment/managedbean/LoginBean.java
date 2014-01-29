package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import yescomment.managedbean.captcha.CaptchaOption;
import yescomment.managedbean.captcha.CaptchaOption.Color;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;
import yescomment.util.NumberUtil;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

	private static final int CAPTCHA_OPTION_SIZE = 3;// this many options are
														// presented

	public static int getCaptchaOptionSize() {
		return CAPTCHA_OPTION_SIZE;
	}

	@ManagedProperty(value = "#{userSessionBean}")
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

	public String tryToLogin() {
		boolean success = true;
		if (userName == null||userName.length()==0) {
			success = false;
			FacesContext.getCurrentInstance().addMessage("loginform:username", new FacesMessage(FacesMessage.SEVERITY_ERROR,"sumu", "detu"));
		}
		if (selectedCaptchaOption==null||!selectedCaptchaOption.equals(correctCaptchaOption)) {
			success = false;
			FacesContext.getCurrentInstance().addMessage("loginform:captcha", new FacesMessage(FacesMessage.SEVERITY_ERROR,"sumc", "detc"));
		}
		if (success) {
			userSessionBean.setLoginUserName(userName);
			return "/viewarticle.xhtml?faces-redirect=true&articleId="
					+ viewedArticleId;
		}
		else {
			return null;
		}
	}

	@PostConstruct
	public void getViewedArticleIdFromFlash() {
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

		viewedArticleId = (Long) FacesContext.getCurrentInstance()
				.getExternalContext().getFlash()
				.get("viewed_article_before_login");

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
