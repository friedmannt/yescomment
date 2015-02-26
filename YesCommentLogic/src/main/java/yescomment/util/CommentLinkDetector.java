package yescomment.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;
/**
 * Detects links in text.
 * Splits the comment text into link and non-link parts
 * @author Friedmann Tamás
 *
 */
public class CommentLinkDetector {
	static final String URL_REGEX = "\\b(https?)://" + "[-A-Za-z0-9+&@#/%?=~_|!:,.;]" + "*[-A-Za-z0-9+&@#/%=~_|]";

	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

	/*
	 * Returns the comment contents, flagging links
	 */
	public static List<CommentContent> detectLinksInText(@NotNull final String text) {
		//initial matchar
		Matcher matcher = URL_PATTERN.matcher(text);
		//the last match is at the beginning at the beginning
		int lastMatchPosition = 0;
		LinkedList<CommentContent> splitted = new LinkedList<CommentContent>();

		while (matcher.find()) {
			//while we find a link
			
			//content before the link
			final String firstPart = text.substring(lastMatchPosition, matcher.start());
			if (firstPart.length()>0) {
				splitted.add(new CommentContent(firstPart,false));	
			}

			//the link
			splitted.add(new CommentContent(matcher.group(),true));

			//adjust last match position to the link's end
			lastMatchPosition = matcher.end();
		}
		//remaining part
		final String lastPart = text.substring(lastMatchPosition);
		if (lastPart.length()>0) {
			splitted.add(new CommentContent(lastPart,false));	
		}
		

		return splitted;
	}

	/**
	 * Value class for comment content, flags, if the content is link or not
	 * @author Friedmann Tamás
	 *
	 */
	public static class CommentContent implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String content;
		private final boolean isLink;

		public String getContent() {
			return content;
		}

		public boolean isLink() {
			return isLink;
		}

		public CommentContent(String content, boolean isLink) {
			super();
			this.content = content;
			this.isLink = isLink;
		}

		@Override
		public String toString() {
			return "CommentContent [content=" + content + ", isLink=" + isLink + "]";
		}

	}

	
}
