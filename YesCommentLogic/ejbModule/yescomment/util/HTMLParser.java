package yescomment.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.validation.constraints.NotNull;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;


/**
 * Gets title and header meta values of a HTML file (can be url, or HTML source)
 * @author Friedmann Tamás
 *
 */
public class HTMLParser {

	public static String getTitle(@NotNull String url) throws MalformedURLException, IOException { 
		Source source=new Source(new URL(url));
		return getTitle(source);
	}
	
	public static String getTitle(@NotNull Source source) {
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement==null) return null;
		// TITLE element never contains other tags so just decode it collapsing whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}

	public static String getMetaValue(@NotNull String url, @NotNull String key,@NotNull String metaKeyAttribute) throws MalformedURLException, IOException {
		Source source=new Source(new URL(url));
		return getMetaValue(source, key,metaKeyAttribute);
	}
	
	public static String getMetaValue(@NotNull Source source, @NotNull String key,@NotNull String metaKeyAttribute) {
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.getNextStartTag(pos,metaKeyAttribute,key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
	
	


}
