package yescomment.util;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import net.htmlparser.jericho.Source;

import org.apache.commons.validator.routines.UrlValidator;

import yescomment.crawler.RSSItem;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class URLUtil {
	private static Logger LOGGER = Logger.getLogger("URLUtil");

	static {
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	}

	private static String getSchemaOfURL(String urlString) {
		if (urlString == null) {
			throw new IllegalArgumentException();
		}
		if (urlString.indexOf("://") >= 0) {
			return urlString.substring(0, urlString.indexOf("://"));
		} else {
			return null;
		}
	}

	private static String addDefaultSchemaToURL(String urlString) {
		if (getSchemaOfURL(urlString) == null) {
			return "http://" + urlString;
		} else {
			return urlString;
		}
	}

	private static String removeFragmentFromURL(String urlString) {
		int indexOfHashmark = urlString.lastIndexOf('#');
		if (indexOfHashmark < 0) {
			return urlString;
		} else {
			return urlString.substring(0, indexOfHashmark);
		}

	}

	public static String getSiteOfURL(String urlString) {

		if (urlString == null) {
			throw new IllegalArgumentException();
		}
		String urlWithoutScheme = null;
		if (urlString.indexOf("://") >= 0) {
			urlWithoutScheme = urlString.substring(urlString.indexOf("://") + 3);
		} else {
			urlWithoutScheme = urlString;
		}
		if (urlWithoutScheme.indexOf("/") >= 0) {

			return urlWithoutScheme.substring(0, urlWithoutScheme.indexOf("/"));
		} else {
			return urlWithoutScheme;
		}

	}

	public static ArticleInfo getArticleInfoFromURL(String urlString) throws IOException {
		return getArticleInfoFromURL(urlString, null);

	}

	/**
	 * 
	 * @param urlString
	 * @param helpRssItem
	 *            ,With the help of an rssItem, many information is supplied, so
	 *            no need to read again from html source
	 * @return
	 * @throws IOException
	 */
	public static ArticleInfo getArticleInfoFromURL(String urlString, RSSItem helpRssItem) throws IOException {
		LOGGER.info(String.format("Getting article info for %s", urlString));
		ArticleInfo articleInfo = new ArticleInfo();
		String urlStringWithSchema = addDefaultSchemaToURL(urlString);
		// remove fragment (#)
		String urlStringWithoutFragment = removeFragmentFromURL(urlStringWithSchema);
		URL url = new URL(urlStringWithoutFragment);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "YesCommentBot/1.0");
		connection.connect();
		int responseCode = connection.getResponseCode();

		LOGGER.fine(String.format("Response code is %d", responseCode));

		articleInfo.setFinalURL(connection.getURL().toString());
		Source source = new Source(connection);
		if (helpRssItem != null) {
			articleInfo.setTitle(helpRssItem.getTitle());
		} else {
			String title = HTMLParser.getTitle(source);
			articleInfo.setTitle(title);
		}
		if (helpRssItem != null) {
			articleInfo.setDescription(helpRssItem.getDescription());
		} else {
			String description = HTMLParser.getMetaValue(source, "description", "name");
			articleInfo.setDescription(description);
		}
		String keywords = HTMLParser.getMetaValue(source, "keywords", "name");
		articleInfo.setKeywords(keywords);
		// opengraph meta tags are the strongest
		String openGraphTitle = HTMLParser.getMetaValue(source, "og:title", "property");
		if (openGraphTitle != null) {
			articleInfo.setTitle(openGraphTitle);
		}
		String openGraphImage = HTMLParser.getMetaValue(source, "og:image", "property");
		if (openGraphImage != null) {
			articleInfo.setImageURL(openGraphImage);
		}
		String openGraphDescription = HTMLParser.getMetaValue(source, "og:description", "property");
		if (openGraphDescription != null) {
			articleInfo.setDescription(openGraphDescription);
		}

		articleInfo.setExtractedArticleText(extractTextFromArticle(source, articleInfo.getFinalURL()));

		articleInfo.setFinalURL(eliminateUnnecessaryURLParams(articleInfo.getFinalURL()));

		connection.disconnect();
		LOGGER.fine(String.format("Got article info for %s", urlString));
		return articleInfo;

	}

	private static String extractTextFromArticle(Source source, String url) {
		try {
			return ArticleExtractor.INSTANCE.getText(source.toString());
		} catch (BoilerpipeProcessingException e) {
			LOGGER.fine(String.format("Text cannot be extracted from %s", url));
			e.printStackTrace();
		}
		return null;
	}

	private static String eliminateUnnecessaryURLParams(String finalURL) throws MalformedURLException, IOException {
		if (finalURL.indexOf("?") < 0) {
			return finalURL;
		} else {
			String basePart = finalURL.substring(0, finalURL.lastIndexOf("?") + 1);
			String query = finalURL.substring(finalURL.lastIndexOf("?") + 1);
			// remove anchor
			if (query.indexOf("#") >= 0) {
				query = query.substring(0, query.indexOf("#"));
			}
			String[] querySplittedByParamSeparator = query.split("&");
			String urlWithoutQuery = basePart;
			int paramIndex = 0;
			while (!urlsAreSameContent(finalURL, urlWithoutQuery) && paramIndex < querySplittedByParamSeparator.length) {

				urlWithoutQuery = urlWithoutQuery + (paramIndex > 0 ? "&" : "") + querySplittedByParamSeparator[paramIndex];

				paramIndex++;
			}
			// if all params removed, we dont need the ?
			if (urlWithoutQuery.endsWith("?")) {
				urlWithoutQuery = urlWithoutQuery.substring(0, urlWithoutQuery.length() - 1);
			}
			return urlWithoutQuery;
		}

	}

	/**
	 * to sites are the same if title, description and keywords are same. Cannot
	 * be full content matching, as advertisemets might be randomised
	 * 
	 * @param referenceURL
	 * @param testURL
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static boolean urlsAreSameContent(String referenceURL, String testURL) throws MalformedURLException, IOException {
		Source sourceRef = new Source(new URL(referenceURL));
		Source sourceTest = new Source(new URL(testURL));

		String titleRef = HTMLParser.getTitle(sourceRef);
		String titleTest = HTMLParser.getTitle(sourceTest);

		String descRef = HTMLParser.getMetaValue(sourceRef, "description", "name");
		String descTest = HTMLParser.getMetaValue(sourceTest, "description", "name");
		String keywordsRef = HTMLParser.getMetaValue(sourceRef, "keywords", "name");
		String keywordsTest = HTMLParser.getMetaValue(sourceTest, "keywords", "name");
		if ((titleRef == null && titleTest == null) || titleRef.equals(titleTest)) {
			if ((descRef == null && descTest == null) || descRef.equals(descTest)) {

				if ((keywordsRef == null && keywordsTest == null) || keywordsRef.equals(keywordsTest)) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Validates url. Default schemes in apache.commons.UrlValidator are http
	 * and https
	 * 
	 * @param url
	 * @return
	 */
	public static boolean urlIsValid(String url) {

		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		return urlValidator.isValid(url);

	}

}
