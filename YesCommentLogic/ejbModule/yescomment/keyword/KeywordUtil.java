package yescomment.keyword;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class KeywordUtil {

	public static final String getURLofKeyword(String keyword) throws UnsupportedEncodingException {
		return String.format(
				"/faces/keywords.xhtml?keyword=%s",
				URLEncoder.encode(keyword, "UTF-8"));
	}
}
