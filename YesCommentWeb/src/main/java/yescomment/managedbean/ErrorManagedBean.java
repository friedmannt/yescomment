package yescomment.managedbean;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import org.apache.commons.httpclient.HttpStatus;

@ManagedBean
@RequestScoped
public class ErrorManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	public String getStackTraceOfException() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		Throwable ex = (Throwable) requestMap.get("javax.servlet.error.exception");
		if (ex==null) {
			return null;
		}
		else {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		fillStackTrace(ex, pw);

		return writer.toString();
		}
	}

	private void fillStackTrace(Throwable ex, PrintWriter pw) {
		

		ex.printStackTrace(pw);

		if (ex instanceof ServletException) {
			Throwable cause = ((ServletException) ex).getRootCause();

			if (null != cause) {
				pw.println("Root Cause:");
				fillStackTrace(cause, pw);
			}
		} else {
			Throwable cause = ex.getCause();

			if (null != cause) {
				pw.println("Cause:");
				fillStackTrace(cause, pw);
			}
		}
	}
	public String getMessageOfStatusCode( ){
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		Integer statusCode =  (Integer) requestMap.get("javax.servlet.error.status_code");
		return HttpStatus.getStatusText(statusCode);
	}
}
