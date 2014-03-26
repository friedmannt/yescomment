package yescomment.persistence;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.util.VoteDirection;

@Stateless
public class CommentManager extends AbstractEntityManager<Comment>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@EJB
	ArticleManager articleManager;
	

	@PersistenceContext(unitName = "YesCommentModel")
	private EntityManager em;
	
	public CommentManager() {
		super(Comment.class);
		
	}

	@Override
	protected void notifyEntityCreation(Comment entity) {
		
		
	}

	@Override
	protected void notifyEntityModification(Comment entity) {
		// doing nothing
		
	}

	@Override
	protected void notifyEntityDeletion(Comment entity) {
		
		
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public Article addCommentToArticle(@NotNull Article article,@NotNull final  String commentText,@NotNull final String commentAuthor ) {
		article = articleManager.find(article.getId());//reread
		Comment comment = new Comment();
		comment.setCommentText(commentText);
		comment.setAuthor(commentAuthor);
		comment.setCommentDate(new Date());
		comment.setArticle(article);
		article.getComments().add(comment);
		article.setCommentCount(article.getCommentCount()+1);
		create(comment);
		return article;

	}
	
	 public Comment voteOnComment(@NotNull Comment comment,@NotNull final VoteDirection voteDirection) {
		comment=find(comment.getId());//reread
		if (voteDirection==VoteDirection.UP) {
			comment.setPlusCount(comment.getPlusCount() + 1);	
		}
		if (voteDirection==VoteDirection.DOWN) {
			comment.setMinusCount(comment.getMinusCount() + 1);
		}
		
		return merge(comment);
		
		 
		
	 }
	
	
}
