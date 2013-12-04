package yescomment.managedbean;

import java.io.Serializable;

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

}
