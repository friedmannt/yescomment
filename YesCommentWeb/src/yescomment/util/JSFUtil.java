package yescomment.util;

import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JSFUtil {
	public static final String getContextRoot() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getContextName();

	}

	public static String getRequestURL() {
		Object request = FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return ((HttpServletRequest) request).getRequestURL().toString();

	}

	public static Locale getLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	

	
}
