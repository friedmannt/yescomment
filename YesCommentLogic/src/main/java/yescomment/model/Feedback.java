package yescomment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: Feedback
 * 
 */
@Entity
@Table(name = "yescomment_feedback")

public class Feedback extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Enumerated(EnumType.STRING)
	private FeedbackSubject feedbackSubject;
	
	
	public static final int MAX_FEEDBACK_SIZE=4000;
	@Column(nullable = false, length = MAX_FEEDBACK_SIZE)
	@NotNull
	@Size(max = MAX_FEEDBACK_SIZE)
	private String feedbackText;
	
	
	


	public FeedbackSubject getFeedbackSubject() {
		return feedbackSubject;
	}

	public void setFeedbackSubject(FeedbackSubject feedbackSubject) {
		this.feedbackSubject = feedbackSubject;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}
	
	public static enum FeedbackSubject {
		OPINION,BUGREPORT,IDEA,MISSPELL,LAWVIOLATION
	}
	

	
}
