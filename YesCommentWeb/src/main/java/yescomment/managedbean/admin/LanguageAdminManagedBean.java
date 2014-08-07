package yescomment.managedbean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ArticleLanguage;

@ManagedBean
@ViewScoped
@RolesAllowed("yescommentadmin")
public class LanguageAdminManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@EJB
	ArticleManager articleManager;

	List<Article> articlesWithoutLanguage;
	List<String> possibleArticleLanguages;
	
	

	public List<Article> getArticlesWithoutLanguage() {
		return articlesWithoutLanguage;
	}

	public void setArticlesWithoutLanguage(List<Article> articlesWithoutLanguage) {
		this.articlesWithoutLanguage = articlesWithoutLanguage;
	}
	
	public List<String> getPossibleArticleLanguages() {
		return possibleArticleLanguages;
	}

	public void setPossibleArticleLanguages(List<String> possibleArticleLanguages) {
		this.possibleArticleLanguages = possibleArticleLanguages;
	}

	@PostConstruct
	public void populateArticlesWithoutLanguage() {
		articlesWithoutLanguage=articleManager.getArticlesWithoutLanguage();
		possibleArticleLanguages= new ArrayList<String>(ArticleLanguage.values().length);
		for (ArticleLanguage articleLanguage:ArticleLanguage.values()) {
			possibleArticleLanguages.add(articleLanguage.toString().toLowerCase());
		}
		Collections.sort(possibleArticleLanguages);
		
	}
	
	
	
	/**
	 * Saving only the ones, where article is set
	 */
	public void saveLanguageChanges() {
		for (Article article:articlesWithoutLanguage) {
			if (article.getLanguage()!=null) {
				articleManager.merge(article);
			}
		}
	}
}
