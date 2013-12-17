package yescomment.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.htmlparser.jericho.Source;

public class URLUtil {
	private static Logger LOGGER = Logger.getLogger("URLUtil");

	public static class ArticleInfo implements Serializable {
		private String finalURL;
		private String imageURL;
		private String title;
		private String description;
		private String keywords;

		public String getFinalURL() {
			return finalURL;
		}

		public void setFinalURL(String finalURL) {
			this.finalURL = finalURL;
		}

		public String getImageURL() {
			return imageURL;
		}

		public void setImageURL(String imageURL) {
			this.imageURL = imageURL;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public String toString() {
			return "ArticleInfo [finalURL=" + finalURL + ", title=" + title
					+ ", description=" + description + ", keywords=" + keywords
					+ "]";
		}

	}

	static {
		CookieHandler.setDefault(new CookieManager(null,
				CookiePolicy.ACCEPT_ALL));
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

	public static String getSiteOfURL(String urlString) {

		if (urlString == null) {
			throw new IllegalArgumentException();
		}
		String urlWithoutScheme = null;
		if (urlString.indexOf("://") >= 0) {
			urlWithoutScheme = urlString
					.substring(urlString.indexOf("://") + 3);
		} else {
			urlWithoutScheme = urlString;
		}
		if (urlWithoutScheme.indexOf("/") >= 0) {

			return urlWithoutScheme.substring(0, urlWithoutScheme.indexOf("/"));
		} else {
			return urlWithoutScheme;
		}

	}

	public static ArticleInfo getArticleInfoFromURL(String urlString)
			throws IOException {
		LOGGER.fine(String.format("Getting article info for %s", urlString));
		ArticleInfo articleInfo = new ArticleInfo();
		String urlStringWithSchema = addDefaultSchemaToURL(urlString);
		URL url = new URL(urlStringWithSchema);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "YesCommentBot/1.0");
		connection.connect();
		int responseCode = connection.getResponseCode();

		LOGGER.fine(String.format("Response code is %d", responseCode));

		articleInfo.setFinalURL(connection.getURL().toString());
		Source source = new Source(connection);

		String title = HTMLParser.getTitle(source);
		articleInfo.setTitle(title);
		String keywords = HTMLParser.getMetaValue(source, "keywords", "name");
		articleInfo.setKeywords(keywords);
		String description = HTMLParser.getMetaValue(source, "description",
				"name");
		articleInfo.setDescription(description);
		// opengraph meta tags are the strongest
		String openGraphTitle = HTMLParser.getMetaValue(source, "og:title",
				"property");
		if (openGraphTitle != null) {
			articleInfo.setTitle(openGraphTitle);
		}
		String openGraphImage = HTMLParser.getMetaValue(source, "og:image",
				"property");
		if (openGraphImage != null) {
			articleInfo.setImageURL(openGraphImage);
		}
		String openGraphDescription = HTMLParser.getMetaValue(source,
				"og:description", "property");
		if (openGraphDescription != null) {
			articleInfo.setDescription(openGraphDescription);
		}

		articleInfo.setFinalURL(eliminateUnnecessaryURLParams(articleInfo
				.getFinalURL()));

		connection.disconnect();
		LOGGER.fine(String.format("Got article info for %s", urlString));
		return articleInfo;

	}

	private static String eliminateUnnecessaryURLParams(String finalURL)
			throws MalformedURLException, IOException {
		if (finalURL.indexOf("?") < 0) {
			return finalURL;
		} else {
			String basePart = finalURL.substring(0,
					finalURL.lastIndexOf("?") + 1);
			String query = finalURL.substring(finalURL.lastIndexOf("?") + 1);
			// remove anchor
			if (query.indexOf("#") >= 0) {
				query = query.substring(0, query.indexOf("#"));
			}
			String[] querySplittedByParamSeparator = query.split("&");
			String urlWithoutQuery = basePart;
			int paramIndex = 0;
			while (!urlsAreSameContent(finalURL, urlWithoutQuery)
					&& paramIndex < querySplittedByParamSeparator.length) {

				urlWithoutQuery = urlWithoutQuery + (paramIndex > 0 ? "&" : "")
						+ querySplittedByParamSeparator[paramIndex];

				paramIndex++;
			}
			// if all params removed, we dont need the ?
			if (urlWithoutQuery.endsWith("?")) {
				urlWithoutQuery = urlWithoutQuery.substring(0,
						urlWithoutQuery.length() - 1);
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
	private static boolean urlsAreSameContent(String referenceURL,
			String testURL) throws MalformedURLException, IOException {
		Source sourceRef = new Source(new URL(referenceURL));
		Source sourceTest = new Source(new URL(testURL));

		String titleRef = HTMLParser.getTitle(sourceRef);
		String titleTest = HTMLParser.getTitle(sourceTest);

		String descRef = HTMLParser.getMetaValue(sourceRef, "description",
				"name");
		String descTest = HTMLParser.getMetaValue(sourceTest, "description",
				"name");
		String keywordsRef = HTMLParser.getMetaValue(sourceRef, "keywords",
				"name");
		String keywordsTest = HTMLParser.getMetaValue(sourceTest, "keywords",
				"name");
		if ((titleRef == null && titleTest == null)
				|| titleRef.equals(titleTest)) {
			if ((descRef == null && descTest == null)
					|| descRef.equals(descTest)) {

				if ((keywordsRef == null && keywordsTest == null)
						|| keywordsRef.equals(keywordsTest)) {
					return true;
				}
			}
		}
		return false;

	}

	

}
