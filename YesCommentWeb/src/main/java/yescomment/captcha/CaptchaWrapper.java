package yescomment.captcha;

import java.io.Serializable;
import java.util.Date;
/**
 * Wraps captcha, and check answers
 * @author Friedmann Tamás
 *
 */
public class CaptchaWrapper implements Serializable {

	
	public static final int DEFAULT_CAPTCHA_LENGTH=8;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Captcha captcha;

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

	public CaptchaWrapper() {

		captcha = new Captcha(DEFAULT_CAPTCHA_LENGTH);
	}

	public void randomizeCaptcha() {
		captcha = new Captcha(DEFAULT_CAPTCHA_LENGTH);

	}

	public boolean checkAnswer(final String answer) {

		if (answer == null || !captcha.answer(answer)) {
			captchaCorrectlyAnswered = false;
			//randomizeCaptcha(); no new captcha
			return false;
		} else {
			captchaCorrectlyAnswered = true;
			return true;
		}
	}

	/**
	 * Time is not needed to the captcha, but for image url, a constantly
	 * changing url should be created. adding time to the url
	 * 
	 * @return
	 */
	public static final long nowTime() {
		return new Date().getTime();
	}
}
