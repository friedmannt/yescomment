package yescomment.managedbean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;

@ManagedBean
@RequestScoped
public class SearchArticleManagedBean implements Serializable {

	@EJB
	ArticleManager articleManager;

	private String searchedArticleURL;

	public String getSearchedArticleURL() {
		return searchedArticleURL;
	}

	public void setSearchedArticleURL(String searchedArticleURL) {
		this.searchedArticleURL = searchedArticleURL;
	}

	public String searchForArticle() {
		// if article is found, we redirect there
		// if not found, can create new article
		
		Article article = articleManager.getArticleByURL(searchedArticleURL);
		Long articleId = null;
		if (article != null) {
			// redirect to searched article
			articleId = article.getId();
			
			return "/viewarticle.xhtml?faces-redirect=true&articleId="+articleId;
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("searchedArticleURL", searchedArticleURL);
			return "newarticle.xhtml?faces-redirect=true";
		}
		
		
		

	}
}
