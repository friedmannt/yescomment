package yescomment.persistence;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import yescomment.model.Comment;

@Stateless

public class CommentManager extends AbstractEntityManager<Comment>{

	@Resource
	EJBContext context;

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
	
}
