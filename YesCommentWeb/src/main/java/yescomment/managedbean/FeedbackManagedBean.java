package yescomment.managedbean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Feedback;
import yescomment.persistence.FeedbackManager;

@ManagedBean
@ViewScoped
public class FeedbackManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	FeedbackManager feedbackManager;

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private boolean sent=false;
	
	
	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public void sendFeedback() {
		assert !sent;
		feedbackManager.sendFeedback(text);
		sent=true;
	}

	public static String getMaxFeedbackSize() {
		return Integer.toString(Feedback.MAX_FEEDBACK_SIZE);
	}
	
}
