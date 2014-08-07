package yescomment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: News
 * 
 */
@Entity
@Table(name = "yescomment_user")

public class User extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public static final int MAX_USERNAME_SIZE=16;
	@Column(nullable = false, length = MAX_USERNAME_SIZE,unique = true)
	@NotNull
	@Size(max = MAX_USERNAME_SIZE)
	private String userName;
	
	
	@Column(nullable = false, length = 64)
	@NotNull
	@Size(max = 64)
	private String encodedPassword;	
	
	private boolean isAdministrator;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public boolean isAdministrator() {
		return isAdministrator;
	}

	public void setAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}
	

	
}
