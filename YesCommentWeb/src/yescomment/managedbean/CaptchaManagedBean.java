package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import yescomment.managedbean.captcha.CaptchaOption;
import yescomment.managedbean.captcha.CaptchaOption.Color;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;
import yescomment.util.NumberUtil;

@ManagedBean
@SessionScoped
public class CaptchaManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int CAPTCHA_OPTION_SIZE = 3;// this many options are
														// presented
	public static int getCaptchaOptionSize() {
		return CAPTCHA_OPTION_SIZE;
	}

	public void answer(CaptchaOption captchaOption)  {
		if (captchaOption==null||!captchaOption.equals(correctCaptchaOption)) {
			
			FacesContext.getCurrentInstance().addMessage("captchaform:captcha", new FacesMessage(FacesMessage.SEVERITY_ERROR,LocalizationUtil.getTranslation("incorrect_captcha_answer", JSFUtil.getLocale()), null));
		}
		else {
			setCaptchaCorrectlyAnswered(true);
		}

		
	}

	private boolean captchaCorrectlyAnswered;

	public boolean isCaptchaCorrectlyAnswered() {
		return captchaCorrectlyAnswered;
	}

	public void setCaptchaCorrectlyAnswered(boolean captchaCorrectlyAnswered) {
		this.captchaCorrectlyAnswered = captchaCorrectlyAnswered;
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


	public String localizeColor(Color color) {
		return LocalizationUtil.getEnumTranslation(color, JSFUtil.getLocale());
	}
	
	public String localizeNumber(Integer number) {
		return LocalizationUtil.getNumberTranslation(number, JSFUtil.getLocale());
	}
}
