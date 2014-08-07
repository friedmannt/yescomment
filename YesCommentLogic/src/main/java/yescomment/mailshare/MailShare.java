package yescomment.mailshare;

import java.util.Locale;

import yescomment.model.Article;
import yescomment.util.LocalizationUtil;
import yescomment.util.URLUtil;

public class MailShare {

	public static String getMailtoLinkForArticle(final Article article, final String userName,final String url, final Locale locale) {

		return "mailto:?subject=" + getSubjectOfShareEmail(locale, userName) + "&amp;body=" + getBodyOfShareEmail(locale, article,url);
	}

	private static String getSubjectOfShareEmail( final Locale locale, final String userName) {
		if (userName == null || userName.length() == 0) {
			return LocalizationUtil.getTranslation("share_email_subject_nouser", locale);
		} else {
			return String.format(LocalizationUtil.getTranslation("share_email_subject_user", locale), userName);
		}

	}

	private  static String getBodyOfShareEmail(final Locale locale, final Article article,final String url) {

		return LocalizationUtil.getTranslation("share_email_body_line1", locale) + '\n' + String.format(LocalizationUtil.getTranslation("share_email_body_line2", locale), article.getTitle(), URLUtil.getSiteOfURL(article.getUrl())) + '\n' + url+"?articleId="+article.getId();
	}
	
}
