package yescomment.managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
		} catch (UnknownHostException e) {
			newArticlePassedTheCheck = false;
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					"newarticleform:newarticlepassedthecheck",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, String
							.format("Unkown host %s", url), String.format(
							"Unkown host %s", url)));
			return;

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
		if (newArticleInfo.getResponseCode() == 200) {

			// we should check, whether article url is unique
			Article articleWithSameURL = articleManager
					.getArticleByURL(url);
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

		} else {
			newArticlePassedTheCheck = false;
			FacesContext.getCurrentInstance().addMessage(
					"newarticleform:newarticlepassedthecheck",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, String
							.format("Response code is %d",
									newArticleInfo.getResponseCode()), String
							.format("Response code is %d",
									newArticleInfo.getResponseCode())));

		}
	}

	public String createNewArticle() {

		Article article = new Article();
		article.setUrl(newArticleInfo.getFinalURL());
		
		if (newArticleInfo.getTitle() != null) {
			article.setTitle(newArticleInfo.getTitle());
		} else {
			article.setTitle("");
		}
		if (newArticleInfo.getImageURL()!=null) {
			article.setImageurl(newArticleInfo.getImageURL());
		}
		else {
			article.setImageurl("resources/images/defaultarticleimage.png");
		}
		article.setDescription(newArticleInfo.getDescription());
		article.setRegistrationDate(new Date());

		List<String> newArticleKeywords = null;
		if (newArticleInfo.getKeywords() != null
				&& !newArticleInfo.getKeywords().isEmpty()) {
			newArticleKeywords = new ArrayList<String>();
			List<String> keywords = Arrays.asList(newArticleInfo.getKeywords()
					.split(","));
			for (String keyword : keywords) {
				keyword = keyword.trim();
				if (!keyword.equals("")) {
					newArticleKeywords.add(keyword);
				}

			}

		}

		if (newArticleKeywords != null) {
			article.getKeywords().addAll(newArticleKeywords);
		}

		Article savedArticle =articleManager.save(article);
		// redirect to new article
		Long newArticleId = savedArticle.getId();
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
