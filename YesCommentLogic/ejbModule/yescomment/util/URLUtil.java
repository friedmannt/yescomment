package yescomment.util;

import java.io.IOException;
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

	public static class ArticleInfo implements Serializable{
		private String finalURL;
		private String title;
		private String description;
		private String keywords;
		private int responseCode;

		public String getFinalURL() {
			return finalURL;
		}

		public void setFinalURL(String finalURL) {
			this.finalURL = finalURL;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
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
					+ ", responseCode=" + responseCode + "]";
		}

	}

	static {
		CookieHandler.setDefault(new CookieManager(null,
				CookiePolicy.ACCEPT_ALL));
	}

	public static String getSchemaOfURL(String urlString) {
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
		articleInfo.setResponseCode(responseCode);
		for (Map.Entry<String, List<String>> header : connection
				.getHeaderFields().entrySet()) {
			LOGGER.fine(String.format("Header: %s = %s", header.getKey(),
					header.getValue()));

		}
		String contentType = connection.getHeaderField("Content-Type");
		articleInfo.setFinalURL(connection.getURL().toString());
		connection.disconnect();
		if (responseCode == 200 && contentType.indexOf("text/html") >= 0) {
			articleInfo.setFinalURL(eliminateUnnecessaryURLParams(articleInfo
					.getFinalURL()));
			Source source = new Source(new URL(articleInfo.getFinalURL()));
			String title = HTMLParser.getTitle(source);
			articleInfo.setTitle(title);
			String keywords = HTMLParser.getMetaValue(source, "keywords");
			articleInfo.setKeywords(keywords);
			String description = HTMLParser.getMetaValue(source, "description");
			articleInfo.setDescription(description);

		}
		LOGGER.fine(String.format("Got article info for %s", urlString));
		return articleInfo;

	}

	public static String eliminateUnnecessaryURLParams(String finalURL)
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

	private static boolean urlsAreSameContent(String referenceURL,
			String testURL) throws MalformedURLException, IOException {
		Source sourceRef = new Source(new URL(referenceURL));
		Source sourceTest = new Source(new URL(testURL));
		String titleRef = HTMLParser.getTitle(sourceRef);
		String titleTest = HTMLParser.getTitle(sourceTest);

		String descRef = HTMLParser.getMetaValue(sourceRef, "description");
		String descTest = HTMLParser.getMetaValue(sourceTest, "description");
		String keywordsRef = HTMLParser.getMetaValue(sourceRef, "keywords");
		String keywordsTest = HTMLParser.getMetaValue(sourceTest, "keywords");
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
