package yescomment.util;

import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.UrlValidator;



public class URLUtil {
	

	

	public static String getSchemaOfURL(@NotNull final String urlString) {
		if (urlString == null) {
			throw new IllegalArgumentException();
		}
		if (urlString.indexOf("://") >= 0) {
			return urlString.substring(0, urlString.indexOf("://"));
		} else {
			return null;
		}
	}

	public static String getSiteOfURL(@NotNull final String urlString) {

		if (urlString == null) {
			throw new IllegalArgumentException();
		}
		String urlWithoutScheme = null;
		if (urlString.indexOf("://") >= 0) {
			urlWithoutScheme = urlString.substring(urlString.indexOf("://") + 3);
		} else {
			urlWithoutScheme = urlString;
		}
		if (urlWithoutScheme.indexOf('/') >= 0) {

			return urlWithoutScheme.substring(0, urlWithoutScheme.indexOf('/'));
		} else {
			return urlWithoutScheme;
		}

	}

	public static String getFragmentOfURL(@NotNull final String urlString) {
		int indexOfHashmark = urlString.lastIndexOf('#');
		if (indexOfHashmark < 0) {
			return null;
		} else {
			return urlString.substring(indexOfHashmark+1);
		}

	}


	/**
	 * Validates url. Default schemes in apache.commons.UrlValidator are http
	 * and https
	 * 
	 * @param url
	 * @return
	 */
	public static boolean urlIsValid(@NotNull String url) {

		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.NO_FRAGMENTS);
		return urlValidator.isValid(url);

	}

}
