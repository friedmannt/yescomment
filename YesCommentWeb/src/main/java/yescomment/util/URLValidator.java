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
		final String url = (String) value;

		String schema = URLUtil.getSchemaOfURL(url);
		if (schema == null || (!schema.equals("http") && !schema.equals("https"))) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("protocol_invalid", JSFUtil.getLocale()), null));
		}
		String fragment = URLUtil.getFragmentOfURL(url);
		if (fragment != null ) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("fragment_not_allowed", JSFUtil.getLocale()), null));
		}
		if (!URLUtil.urlIsValid(url)) {

			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("url_invalid", JSFUtil.getLocale()), null));
		}

	}
}
