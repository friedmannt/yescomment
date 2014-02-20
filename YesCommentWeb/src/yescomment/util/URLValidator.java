package yescomment.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("URLValidator")
public class URLValidator implements Validator {

	@Override
	public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
		
		if (!URLUtil.urlIsValid((String) value)) {
		
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("url_invalid", JSFUtil.getLocale()),null));
		}
		

	}

}
