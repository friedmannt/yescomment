package yescomment.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import yescomment.model.Article;
import yescomment.util.URLUtil;

@ApplicationScoped
@ManagedBean
public class YesCommentApplicationBean implements Serializable {

	public String getSiteOfArticle(final Article article) {
		return URLUtil.getSiteOfURL(article.getUrl());
	}

	/**
	 * ui:repeat does not support set, only list
	 * 
	 * @param article
	 * @return
	 */
	public List<String> getKeywordsAsList(final Article article) {
		if (article != null) {
			return new ArrayList<String>(article.getKeywords());
		} else {
			return null;
		}

	}
	
	

}
