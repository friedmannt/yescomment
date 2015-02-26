package yescomment.persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import yescomment.model.Feedback;
import yescomment.model.Feedback.FeedbackSubject;

@Stateless

public class FeedbackManager extends AbstractEntityManager<Feedback> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "YesCommentModel")
	private EntityManager em;

	public FeedbackManager() {
		super(Feedback.class);

	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	
	
	public void sendFeedback(@NotNull final FeedbackSubject subject, @NotNull final String text) {
		Feedback feedback = new Feedback();
		feedback.setFeedbackSubject(subject);
		feedback.setFeedbackText(text);
		create(feedback);

	}

	
	public void deleteFeedback(@NotNull final String id) {
		Feedback feedback = find(id);
		if (feedback != null) {
			remove(feedback);
		}

	}

}
