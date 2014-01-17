package yescomment.persistence;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import yescomment.model.Article;
import yescomment.model.Comment;

@Stateless
public class CommentManager extends AbstractEntityManager<Comment>{

	@Resource
	EJBContext context;
	
	@EJB
	ArticleManager articleManager;

	@PersistenceContext(unitName = "YesCommentModel")
	private EntityManager em;
	
	public CommentManager() {
		super(Comment.class);
		
	}

	@Override
	protected void notifyEntityCreation(Comment entity) {
		// doing nothing
		
	}

	@Override
	protected void notifyEntityModification(Comment entity) {
		// doing nothing
		
	}

	@Override
	protected void notifyEntityDeletion(Comment entity) {
		// doing nothing
		
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public Article addCommentToArticle(Article article,final String commentText,final String commentAuthor ) {
		article = articleManager.find(article.getId());//reread
		Comment comment = new Comment();
		comment.setCommentText(commentText);
		comment.setAuthor(commentAuthor);
		comment.setCommentDate(new Date());
		comment.setArticle(article);
		article.getComments().add(comment);
		create(comment);
		return article;

	}
	
}
