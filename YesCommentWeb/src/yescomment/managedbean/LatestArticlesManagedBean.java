package yescomment.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import yescomment.model.Article;
import yescomment.util.LatestArticlesSingletonLocal;

@ManagedBean
@ApplicationScoped
public class LatestArticlesManagedBean  implements Serializable{

	@EJB
	LatestArticlesSingletonLocal latestArticlesSingleton;
	
	public List<Article> retrieveLatestArticles() {
		return latestArticlesSingleton.retrieveLatestArticles();
	}

	
	
}
