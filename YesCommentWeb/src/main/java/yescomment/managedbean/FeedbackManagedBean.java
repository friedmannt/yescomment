package yescomment.managedbean;

import java.io.Serializable;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Feedback;
import yescomment.model.Feedback.FeedbackSubject;
import yescomment.persistence.FeedbackManager;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@ViewScoped
public class FeedbackManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	FeedbackManager feedbackManager;
	private FeedbackSubject subject;

	public FeedbackSubject getSubject() {
		return subject;
	}

	public void setSubject(FeedbackSubject subject) {
		this.subject = subject;
	}

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private boolean sent = false;

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public void sendFeedback() {
		assert !sent;
		feedbackManager.sendFeedback(subject, text);
		sent = true;
	}

	public static String getMaxFeedbackSize() {
		return Integer.toString(Feedback.MAX_FEEDBACK_SIZE);
	}

	public String getFeedbackSubjectTranslation(final Feedback.FeedbackSubject feedbackSubject) {
		Locale locale = JSFUtil.getLocale();
		return LocalizationUtil.getEnumTranslation(feedbackSubject, locale);
	}

	public FeedbackSubject[] getAllFeedbackSubjects() {
		return FeedbackSubject.values();
	}

}
