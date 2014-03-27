package yescomment.managedbean;

import java.io.Serializable;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ArticleInfo;

@ManagedBean
@ViewScoped
public class NewArticleManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@ManagedProperty(value="#{userSessionBean}")
	private UserSessionBean userSessionBean;

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}
	
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	@EJB
	ArticleManager articleManager;
	
	@EJB
	ArticleInfoRetriever articleInfoRetriever;
	
	ArticleInfo newArticleInfo;
	boolean newArticlePassedTheCheck = false;

	public ArticleInfo getNewArticleInfo() {
		return newArticleInfo;
	}

	public void setNewArticleInfo(ArticleInfo newArticleInfo) {
		this.newArticleInfo = newArticleInfo;
	}

	public boolean isNewArticlePassedTheCheck() {
		return newArticlePassedTheCheck;
	}

	public void setNewArticlePassedTheCheck(boolean newArticlePassedTheCheck) {
		this.newArticlePassedTheCheck = newArticlePassedTheCheck;
	}

	public void retrieveNewArticleInfo(String url){

		try {
			Future<ArticleInfo> articleInfoFutureResult=articleInfoRetriever.retrieveArticleInfo(url);
			newArticleInfo = articleInfoFutureResult.get();//use asynch future call instead on blocking get
			
		} catch (Exception e) {
			newArticlePassedTheCheck = false;
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					"newarticleform:newarticlepassedthecheck",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getClass()
							.getName() + ":" + e.getMessage(),null));
			return;
		} 
		

			// we should check, whether final article url is unique
			Article articleWithSameURL = articleManager
					.getArticleByURL(newArticleInfo.getFinalURL());
			if (articleWithSameURL == null) {
				newArticlePassedTheCheck = true;
			} else {
				newArticlePassedTheCheck = false;
				FacesContext.getCurrentInstance().addMessage(
						"newarticleform:newarticlepassedthecheck",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, String
								.format("Article already exists: %s",
										newArticleInfo.getFinalURL()), null));

			}

		
	}

	public String createNewArticle() {

		Article article = articleManager.createArticleFromArticleInfo(newArticleInfo);

		article =articleManager.create(article);
		// redirect to new article
		String newArticleId = article.getId();
		return "/viewarticle.xhtml?faces-redirect=true&articleId=" + newArticleId;

	}

	
	public void fillNewArticleURLFromSearch() {
		
		String searchedURL = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getFlash().get("searchedArticleURL");
		if (searchedURL != null) {
			retrieveNewArticleInfo(searchedURL);
		}
	}

}
