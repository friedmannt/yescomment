package yescomment.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.News;
import yescomment.persistence.NewsManager;

@ManagedBean
@ViewScoped
public class NewsManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	NewsManager newsManager;

	List<News> news;

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	@PostConstruct
	public void loadAllNews() {
		news = newsManager.getEntitiesOrdered("createDate", Date.class, false);
	}

}
