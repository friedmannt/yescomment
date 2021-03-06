package yescomment.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import yescomment.util.JSFUtil;
import yescomment.util.URLUtil;

@ApplicationScoped
@ManagedBean
public class YesCommentApplicationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getSiteOfURL(final String url) {
		return URLUtil.getSiteOfURL(url);
	}

	/**
	 * ui:repeat does not support set, only list
	 * 
	 * @param article
	 * @return
	 */
	public <T> List<T> getSetAsList(final Set<T> set) {
		if (set != null) {
			return new ArrayList<T>(set);
		} else {
			return null;
		}

	}
	
	public String getWebSocketURLBase() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return JSFUtil.getWebSocketURLBase(request);
	}
	
	
	public String getCurrentLanguage() {
		return JSFUtil.getLocale().getLanguage();
	}

}
