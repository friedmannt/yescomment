package yescomment.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@Cacheable
public abstract class AbstractEntity implements Serializable {

	/**
	 * id is generated via uuid
	 */
	private static final long serialVersionUID = 1L;
	
	 
	@Id()
	@Column(length=32)//UUID length without minus signs
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@NotNull
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public AbstractEntity() {

		id = UUID.randomUUID().toString().replace("-", "");
		createDate = new Date();
	}

}
