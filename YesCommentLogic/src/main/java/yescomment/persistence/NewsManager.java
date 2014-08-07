package yescomment.persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import yescomment.model.News;

@Stateless

public class NewsManager extends AbstractEntityManager<News>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@PersistenceContext(unitName = "YesCommentModel")
	private EntityManager em;
	
	public NewsManager() {
		super(News.class);
		
	}


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}





	public void publishNews(@NotNull final String title,@NotNull final String text) {

		News news = new News();
		news.setTitle(title);
		news.setNewsText(text);
		create(news);
		
	}


	public void deleteNews(@NotNull final String id) {

		News news=find(id);
		if (news!=null) {
			remove(news);
		}
		
	}
	
	
	
}
