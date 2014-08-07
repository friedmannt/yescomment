package yescomment.managedbean;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import yescomment.mailshare.MailShare;
import yescomment.model.Article;
import yescomment.util.JSFUtil;

@ApplicationScoped
@ManagedBean
public class ShareArticleInEmailManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getMailtoLinkForArticle(final Article article, final String userName,final String url) {
		Locale locale = JSFUtil.getLocale();
		return MailShare.getMailtoLinkForArticle(article, userName, url, locale);
		
	}


}
