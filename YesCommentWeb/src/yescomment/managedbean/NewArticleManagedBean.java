package yescomment.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.URLUtil;
import yescomment.util.URLUtil.ArticleInfo;

@ManagedBean
@ViewScoped
public class NewArticleManagedBean implements Serializable {

	
	@EJB
	ArticleManager articleManager;
	
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

	public void retrieveNewArticleInfo(String url) {

		try {
			newArticleInfo = URLUtil.getArticleInfoFromURL(url);
		} catch (IOException e) {
			newArticlePassedTheCheck = false;
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					"newarticleform:newarticlepassedthecheck",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getClass()
							.getName() + ":" + e.getMessage(), e.getClass()
							.getName() + ":" + e.getMessage()));
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
										newArticleInfo.getFinalURL()), String
								.format("Article already exists: %s",
										newArticleInfo.getFinalURL())));

			}

		
	}

	public String createNewArticle() {

		Article article = articleManager.createArticleFromArticleInfo(newArticleInfo);

		article =articleManager.create(article);
		// redirect to new article
		Long newArticleId = article.getId();
		return "/viewarticle.xhtml?faces-redirect=true&id=" + newArticleId;

	}

	
	public void fillNewArticleURLFromRequest() {
		
		String searchedURL = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getFlash().get("searchedArticleURL");
		if (searchedURL != null) {
			retrieveNewArticleInfo(searchedURL);
		}
	}

}
