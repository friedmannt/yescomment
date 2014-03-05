package yescomment.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable{
	
	/**
	 * id is generated via uuid 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AbstractEntity() {
		
		id=UUID.randomUUID().toString();
	}
	
	

}
