package yescomment.managedbean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@ViewScoped
public class LoginManagedBean implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Logs in and navigate
	 * 
	 * @return
	 */
	public String tryToLogin() {

		try {
			login();
			return "main";
		} catch (ServletException e) {
			e.printStackTrace();
			return "loginerror";
		}
	}

	
	
	public void login() throws ServletException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.login(username, password);
	}

	public void logout() throws ServletException {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.logout();
		username=null;
		password=null;
		
	}

}
