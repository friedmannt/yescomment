package yescomment.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import yescomment.captcha.Captcha;
import yescomment.captcha.CaptchaOption;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@SessionScoped
public class CaptchaManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int CAPTCHA_OPTION_SIZE = 10;// this many options are
														// presented
	private Captcha captcha;
	
	public Captcha getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
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
		captcha=new Captcha(CAPTCHA_OPTION_SIZE);
		
	}
	

	public void answer(CaptchaOption captchaOption)  {
		
		if (captchaOption==null||!captcha.answer(captchaOption)) {
			
			FacesContext.getCurrentInstance().addMessage("newcommentform:captcha", new FacesMessage(FacesMessage.SEVERITY_ERROR,LocalizationUtil.getTranslation("incorrect_captcha_answer", JSFUtil.getLocale()), null));
			randomizeCaptcha();
		}
		else {
			setCaptchaCorrectlyAnswered(true);
		}

		
	}
	public void randomizeCaptcha() {
		captcha=new Captcha(CAPTCHA_OPTION_SIZE);//recreate the captcha
	}
	

	public String localizeColor(CaptchaOption.Color color) {
		return LocalizationUtil.getEnumTranslation(color, JSFUtil.getLocale());
	}
	
	public String localizeNumber(Integer number) {
		return LocalizationUtil.getNumberTranslation(number, JSFUtil.getLocale());
	}
}
