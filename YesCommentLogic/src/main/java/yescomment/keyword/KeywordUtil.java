package yescomment.keyword;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.validation.constraints.NotNull;

public class KeywordUtil {

	/**
	 * Get url of keyword, on the keyword page
	 * @param keyword
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final String getURLofKeyword(@NotNull String keyword) throws UnsupportedEncodingException {
		return String.format(
				"/faces/keywords.xhtml?keyword=%s#resultarticlesforkeyword",
				URLEncoder.encode(keyword, "UTF-8"));
	}
}
