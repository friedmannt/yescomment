package yescomment.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import yescomment.articleretriever.AsynchArticleInfoRetriever;
import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ArticleInfo;

@ManagedBean
@RequestScoped
public class SearchArticleByURLManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	ArticleManager articleManager;

	private String searchedArticleURL;

	public String getSearchedArticleURL() {
		return searchedArticleURL;
	}

	public void setSearchedArticleURL(String searchedArticleURL) {
		this.searchedArticleURL = searchedArticleURL;
	}

	@EJB
	AsynchArticleInfoRetriever articleInfoRetriever;

	public String searchForArticleByURL() throws IOException {
		// if article is found, we redirect there
		// if not found, can create new article
		
		Article article = articleManager.getArticleByURL(searchedArticleURL);
		String articleId = null;
		if (article != null) {
			// redirect to searched article
			articleId = article.getId();
			
			return "/viewarticle.xhtml?faces-redirect=true&articleId="+articleId;
		} else {
			Future<ArticleInfo> articleInfoFutureResult = articleInfoRetriever.retrieveArticleInfo(searchedArticleURL);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("articleInfoFutureResult", articleInfoFutureResult);
			return "newarticle.xhtml?faces-redirect=true";


		}
		
		
		

	}
}
