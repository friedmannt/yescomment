package yescomment.captcha;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import yescomment.managedbean.UserSessionBean;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@FacesValidator("CaptchaValidator")
public class CaptchaValidator implements Validator {

	private static final int SECONDS_BETWEEN_CAPTCHA_ANSWER = 10;
	
	@Override
	public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
		String answer = (String) value;
		//gets captcha from session
		UserSessionBean userSessionBean = (UserSessionBean) uic.getValueExpression("userSessionBean").getValue(fc.getELContext());
		
		if (!userSessionBean.getCaptchaWrapper().checkAnswer(answer)) {
			//answer is incorrect
			userSessionBean.setLastCaptchaAnswerDate(new Date());
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("incorrect_captcha_answer", JSFUtil.getLocale()), null));

		}
		
		
		int secondsToWait =JSFUtil.secondsToWaitUntilOperationAllowed(userSessionBean.getLastCaptchaAnswerDate(), SECONDS_BETWEEN_CAPTCHA_ANSWER) ;
		if (secondsToWait != 0) {
			//answer is correct, but too near to incorrect answer
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format(LocalizationUtil.getTranslation("wait_n_seconds_next_captcha", JSFUtil.getLocale()), secondsToWait), null));
		}
	}

}
