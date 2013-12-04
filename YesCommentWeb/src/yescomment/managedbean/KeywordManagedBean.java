package yescomment.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;

@ManagedBean
@RequestScoped
public class KeywordManagedBean implements Serializable {
	@ManagedProperty("#{param.keyword}")
	private String keyword;

	@EJB
	ArticleManager articleManager;
	
	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private List<Article> resultArticles;


	public List<Article> getResultArticles() {
		return resultArticles;
	}


	public void setResultArticles(List<Article> resultArticles) {
		this.resultArticles = resultArticles;
	}
	
	public void searchForKeyword() {
		
		resultArticles=articleManager.getArticlesWithKeyword(keyword);
	}
	
	@PostConstruct
	public void searchForKeywordIfGiven() {
		System.out.println("KW"+keyword);
		if (keyword!=null) {
			searchForKeyword();
		}
	}
	

	
}
