package yescomment.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="ENTITY_SEQ",sequenceName="ENTITY_SEQ", allocationSize=1,initialValue=0)
	@GeneratedValue(generator="ENTITY_SEQ", strategy= GenerationType.SEQUENCE)
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
