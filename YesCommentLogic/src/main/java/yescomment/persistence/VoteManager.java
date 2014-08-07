package yescomment.persistence;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.model.Vote;
import yescomment.util.VoteDirection;

@Stateless
public class VoteManager extends AbstractEntityManager<Vote> {

	private static final long serialVersionUID = 1L;

	@EJB
	protected CommentManager commentManager;

	@PersistenceContext(unitName = "YesCommentModel")
	protected EntityManager em;

	public VoteManager() {
		super(Vote.class);

	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	private List<String> usersWhoVotedOnComment(@NotNull final Comment comment) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> q = cb.createQuery(String.class);
		Root<Comment> r = q.from(Comment.class);
		Join<Article, Vote> j = r.join("votes");
		q.where(cb.equal(r,comment));
		q.multiselect(j.<String>get("userName"));
		
		TypedQuery<String> query = em.createQuery(q);

		return  query.getResultList();
	}

	private boolean enabledToVoteOnComment(@NotNull final Comment comment, @NotNull final String userName) {
		
		if (comment.getAuthor().equals(userName)) {
			return false;
		} else if (usersWhoVotedOnComment(comment).contains(userName)) {
			return false;
		} else {
			return true;
		}
	}

	public Comment voteOnComment(@NotNull final String commentId, @NotNull final String userName, @NotNull final VoteDirection voteDirection) {
		Comment comment = commentManager.find(commentId);
		if (enabledToVoteOnComment(comment, userName)) {
			Vote vote = new Vote();
			comment.getVotes().add(vote);
			vote.setComment(comment);
			vote.setUserName(userName);
			vote.setVoteDirection(voteDirection);
			if (voteDirection == VoteDirection.UP) {
				comment.setUpVoteCount(comment.getUpVoteCount() + 1);

			}
			if (voteDirection == VoteDirection.DOWN) {
				comment.setDownVoteCount(comment.getDownVoteCount() + 1);

			}
		}
		return commentManager.merge(comment);
	}

}
