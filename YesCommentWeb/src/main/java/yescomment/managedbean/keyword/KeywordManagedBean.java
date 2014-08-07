package yescomment.managedbean.keyword;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.keyword.KeywordLanguageFilter;
import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@ViewScoped
public class KeywordManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String keyword;

	

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@EJB
	ArticleManager articleManager;
	
	private List<Article> resultArticles;

	public List<Article> getResultArticles() {
		return resultArticles;
	}
	
	private KeywordLanguageFilter selectedKeywordLanguageFilter=KeywordLanguageFilter.ALL_LANGUAGE;
	
	public KeywordLanguageFilter getSelectedKeywordLanguageFilter() {
		return selectedKeywordLanguageFilter;
	}

	public void setSelectedKeywordLanguageFilter(KeywordLanguageFilter selectedKeywordLanguageFilter) {
		this.selectedKeywordLanguageFilter = selectedKeywordLanguageFilter;
	}

	public void setResultArticles(List<Article> resultArticles) {
		this.resultArticles = resultArticles;
	}

	
	public void changeSelectedKeywordLanguageFilter(KeywordLanguageFilter selectedKeywordLanguageFilter) {
		this.selectedKeywordLanguageFilter = selectedKeywordLanguageFilter;
		//search for keyword again with different filter
		searchForKeyword();
	}
	public void searchForKeyword() {
		if (keyword == null) {
			resultArticles = null;
		} else {
			resultArticles = articleManager.getArticlesWithKeyword(keyword,JSFUtil.getLanguageCodeOfKeywordLanguageFilter(selectedKeywordLanguageFilter));
		}
	}

	

	
	public String getKeywordLanguageFilterTranslation(final KeywordLanguageFilter keywordLanguageFilter) {
		Locale locale=JSFUtil.getLocale();
		return LocalizationUtil.getEnumTranslation(keywordLanguageFilter, locale);
	}
	
}
