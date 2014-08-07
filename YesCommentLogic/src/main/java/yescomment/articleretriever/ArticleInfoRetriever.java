package yescomment.articleretriever;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.validation.constraints.NotNull;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import yescomment.util.ArticleInfo;
import yescomment.util.HTMLParser;
import yescomment.util.StringUtil;

public class ArticleInfoRetriever {
	/**
	 * Accept all cookies
	 */
	static {
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	}
	
	
	/**
	 * Retrieves article info from an url
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public static ArticleInfo getArticleInfoFromURL(@NotNull final String urlString) throws IOException{
		final Connection connection = Jsoup.connect(urlString).followRedirects(true).userAgent("YesComment Bot/1.0").timeout(30*1000);
		final Document document=connection.get();
		
		final Element head=document.head();
		ArticleInfo articleInfo = new ArticleInfo();
		articleInfo.setFinalURL(document.location());
		
		articleInfo.setTitle(document.title());

		String description = HTMLParser.getMetaValue(head,  "name","description");
		articleInfo.setDescription(description);

		String keywords = HTMLParser.getMetaValue(head, "name","keywords");
		articleInfo.setKeywords(keywords);

		String language = HTMLParser.getMetaValue(head,  "http-equiv","content-language");
		articleInfo.setLanguage(language);
		String languageFromHTMLTag = HTMLParser.getLanguageFromHTMLTag(document);
		if (languageFromHTMLTag != null) {
			articleInfo.setLanguage(languageFromHTMLTag);
		}

		// canonical tag is a link tag
		String canonicalLink = HTMLParser.getLinkValue(head, "canonical");
		String canonicalMeta = HTMLParser.getMetaValue(head,  "name","canonical");
		if (canonicalLink != null) {
			articleInfo.setFinalURL(canonicalLink);
		} else if (canonicalMeta != null) {
			articleInfo.setFinalURL(canonicalMeta);
		}

		// opengraph meta tags are the strongest, they are retireved in the end
		retrieveOpenGraphTags(articleInfo, head);
		//language should be 2 characters
		stripLanguage(articleInfo);

		return articleInfo;

	}
	
	private static void retrieveOpenGraphTags(ArticleInfo articleInfo, Element head) {
		// property is the basic keyname, but sometimes it is name
		String openGraphTitle = HTMLParser.getMetaValue(head,  "property","og:title");
		if (openGraphTitle != null) {
			articleInfo.setTitle(openGraphTitle);
		} else {
			openGraphTitle = HTMLParser.getMetaValue(head,  "name","og:title");
			if (openGraphTitle != null) {
				articleInfo.setTitle(openGraphTitle);
			}
		}
		String openGraphImage = HTMLParser.getMetaValue(head, "property","og:image");
		if (openGraphImage != null) {
			articleInfo.setImageURL(openGraphImage);
		} else {
			openGraphImage = HTMLParser.getMetaValue(head, "name","og:image");
			if (openGraphImage != null) {
				articleInfo.setImageURL(openGraphImage);
			}
		}
		String openGraphDescription = HTMLParser.getMetaValue(head, "property","og:description");
		if (openGraphDescription != null) {
			articleInfo.setDescription(openGraphDescription);
		} else {
			openGraphDescription = HTMLParser.getMetaValue(head, "name","og:description");
			if (openGraphDescription != null) {
				articleInfo.setDescription(openGraphDescription);
			}
		}
		String openGraphLocale = HTMLParser.getMetaValue(head, "property","og:locale");
		if (openGraphLocale != null) {
			articleInfo.setLanguage(openGraphLocale);
		} else {
			openGraphLocale = HTMLParser.getMetaValue(head,  "name","og:locale");
			if (openGraphLocale != null) {
				articleInfo.setLanguage(openGraphLocale);
			}
		}
		String openGraphURL = HTMLParser.getMetaValue(head,  "property","og:url");
		if (openGraphURL != null) {
			articleInfo.setFinalURL(openGraphURL);
		} else {
			openGraphURL = HTMLParser.getMetaValue(head,  "name","og:url");
			if (openGraphURL != null) {
				articleInfo.setFinalURL(openGraphURL);
			}
		}
	}
	/**
	 * Long language codes are stripped to first two chars
	 * 
	 * @param articleInfo
	 */
	private static void stripLanguage(@NotNull final ArticleInfo articleInfo) {
		if (articleInfo.getLanguage() != null) {
			articleInfo.setLanguage(StringUtil.maximizeStringLength(articleInfo.getLanguage(), 2));
		}

	}
}
