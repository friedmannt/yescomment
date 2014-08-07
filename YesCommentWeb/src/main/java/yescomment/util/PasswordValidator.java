package yescomment.util;

import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("PasswordValidator")
public class PasswordValidator implements Validator {

	private static final List<Character> validLowercaseChars=Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
	private static final List<Character> validUppercaseChars=Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
	private static final List<Character> validNumbers=Arrays.asList('0','1','2','3','4','5','6','7','8','9');
	private static final List<Character> validSpecialChars=Arrays.asList('<','>','#','@','(',')','{','}','_','+','-','*','/','~','.','?','!','%','=',',',';');
	
	@Override
	public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
		String password=(String) value;
		if (password.length()<4||password.length()>8)
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("password_length_between_4_and_8", JSFUtil.getLocale()),null));
		for (int charIndex=0;charIndex<password.length();charIndex++) {
			Character characterAtIndex=password.charAt(charIndex);
			if (!validLowercaseChars.contains(characterAtIndex)&&!validUppercaseChars.contains(characterAtIndex)&&!validNumbers.contains(characterAtIndex)&&!validSpecialChars.contains(characterAtIndex)) {
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("password_invalid_char", JSFUtil.getLocale()),null));
			}
		}
	}
	
	
	

}
