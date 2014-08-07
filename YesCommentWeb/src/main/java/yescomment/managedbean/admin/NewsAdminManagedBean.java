package yescomment.managedbean.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.News;
import yescomment.persistence.NewsManager;

@ManagedBean
@ViewScoped
@RolesAllowed("yescommentadmin")
public class NewsAdminManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@EJB
	NewsManager newsManager;
	private String title;//for creation
	
	private String text; //for creation
	private String id;//for deletion

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void pubishNews() {
		newsManager.publishNews(title,text);
		title=null;
		text=null;
	}
	
	public List<News> loadAllNews() {
		return newsManager.getEntitiesOrdered("createDate",Date.class,false);
	}
	public void deleteNews() {
		newsManager.deleteNews(id);
		id=null;
	}
	
	
}
