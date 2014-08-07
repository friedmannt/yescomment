package yescomment.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.util.ArticleRedundantCommentDataUtil;

@Stateless
public class CommentManager extends AbstractEntityManager<Comment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	protected ArticleManager articleManager;

	@PersistenceContext(unitName = "YesCommentModel")
	protected EntityManager em;

	public CommentManager() {
		super(Comment.class);

	}

	

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	
	public Article addCommentToArticle(@NotNull  Article article, @NotNull final String commentText, @NotNull final String commentAuthor,final String newCommentReplyOfId) {
		article = articleManager.find(article.getId());// reread
		Comment comment = new Comment();
		comment.setCommentText(commentText);
		comment.setAuthor(commentAuthor);
		comment.setArticle(article);
		if (newCommentReplyOfId!=null) {
			Comment repliedComment=find(newCommentReplyOfId);
			if (repliedComment!=null) {
				comment.setReplyOf(repliedComment);
			}
		}
		article.getComments().add(comment);
		ArticleRedundantCommentDataUtil.validateRedundantCommentData(article);
		create(comment);
		return article;

	}

	

	

	public void hideComment(@NotNull final String commentId) {
		Comment comment = find(commentId);
		if (comment != null) {

			comment.setHidden(true);
		}
	}

	public void unhideComment(@NotNull final String commentId) {
		Comment comment = find(commentId);
		if (comment != null) {
			comment.setHidden(false);
		}
	}
	
	public void removeComment(@NotNull final String commentId) {
		Comment comment = find(commentId);
		if (comment != null) {
			//the comments which replied to this commen will be no replies
			List<Comment> replies=getRepliesOfComment(commentId);
			for (Comment reply:replies) {
				reply.setReplyOf(null);
			}
			
			Article article = comment.getArticle();
			article.getComments().remove(comment);
		
			ArticleRedundantCommentDataUtil.validateRedundantCommentData(article);
			remove(comment);
		}
	}
	
	public Comment getLastCommentOfArticle(final @NotNull Article article) {
		Comment lastComment=null;
		for (Comment comment:article.getComments()) {
			if (lastComment==null||lastComment.getCreateDate().before(comment.getCreateDate())) {
				lastComment=comment;
			}
		}
		return lastComment;
		
	}
	
	public List<Comment> getRepliesOfComment(@NotNull final String commentId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Comment> q = cb.createQuery(Comment.class);
		Root<Comment> r = q.from(Comment.class);
		Join<Comment, Comment> j = r.join("replyOf");
		List<Predicate> predicateList = new ArrayList<Predicate>();

		predicateList.add(cb.equal(j.<String>get("id"), commentId));
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		q.where(predicates);
		TypedQuery<Comment> query = em.createQuery(q);
		return query.getResultList();

	
	}

}
