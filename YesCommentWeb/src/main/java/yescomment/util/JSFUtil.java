package yescomment.util;

import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import yescomment.keyword.KeywordLanguageFilter;

public class JSFUtil {
	public static final String getContextRoot() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getContextName();

	}

	public static String getRequestURL() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return  request.getRequestURL().toString();

	}
	
	public static String getUserName() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return  request.getRemoteUser();
	}

	public static Locale getLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	/**
	 * some operations should have some time between them
	 * @param lastOperationDate
	 * @param secondsBetweenOperation
	 * @return number of seconds to wait, 0 if wait is not needed
	 */
	public static int secondsToWaitUntilOperationAllowed(final Date lastOperationDate,final int secondsBetweenOperation) {
		if (lastOperationDate==null) {
			return 0;
		}
		else {
			Date now = new Date();
			if (now.getTime()-lastOperationDate.getTime()>=secondsBetweenOperation*1000L) {
				return 0;
			}
			else {
				return (int) (secondsBetweenOperation*1000-(now.getTime()-lastOperationDate.getTime()))/1000;
			}
		}
	}
	
	public static String getWebSocketURLBase(HttpServletRequest request) {
		
		StringBuilder urlStringBuilder=new StringBuilder("ws://");
		String serverName=request.getServerName();
		int serverPort=request.getServerPort();
		/*openshift:websocekturl is fixed 8000*/
		serverPort=8000;
		
		String contextPath=request.getContextPath();
		urlStringBuilder.append(serverName);
		urlStringBuilder.append(':');
		urlStringBuilder.append(serverPort);
		urlStringBuilder.append(contextPath);
		return urlStringBuilder.toString();
	} 
	
	
	public static String getRSSLinkURLBase(HttpServletRequest request) {
		StringBuilder urlStringBuilder=new StringBuilder("http://");
		String serverName=request.getServerName();
		int serverPort=request.getServerPort();
		/*openshift:web is at default port*/
		serverPort=8000;
		
		String contextPath=request.getContextPath();
		urlStringBuilder.append(serverName);
		urlStringBuilder.append(':');
		urlStringBuilder.append(serverPort);
		urlStringBuilder.append(contextPath);
		return urlStringBuilder.toString();
	} 
	
	
	public static String getLanguageCodeOfKeywordLanguageFilter(@NotNull final KeywordLanguageFilter keywordLanguageFilter) {
		
		switch (keywordLanguageFilter) {
		case ALL_LANGUAGE:
			return null;
		case USER_LANGUAGE:
			return JSFUtil.getLocale().getLanguage();

		default:
			return null;
		}
	}

}
