package yescomment.managedbean.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Feedback;
import yescomment.persistence.FeedbackManager;

@ManagedBean
@ViewScoped
@RolesAllowed("yescommentadmin")
public class FeedbackAdminManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@EJB
	FeedbackManager feedbackManager;


	public String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public List<Feedback> loadAllFeedback() {
		return feedbackManager.getEntitiesOrdered("createDate",Date.class,false);
	}
	public void deleteFeedback() {
		feedbackManager.deleteFeedback(id);
		id=null;
	}
	
	
}
