package yescomment.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




/**
 * Gets title and head meta values of a HTML file (based on the head element, and the html element in case of language)
 * 
 * @author Friedmann Tamás
 * 
 */
public class HTMLParser {

	

	
	/**
	 * Gets value from html head, the key attribute name and value is parametrizable. Key attribute name is usually "name" or "property".
	 * The value is always in the content attribute
	 * @param head
	 * @param metaAttributeKeyName
	 * @param metaAttributeKeyValue
	 * @return the content, if multiple same meta tags found, returns the firts non null value 
	 */
	public static String getMetaValue(@NotNull Element head,@NotNull String metaAttributeKeyName, @NotNull String metaAttributeKeyValue ) {
		Elements metaTags=head.getElementsByTag("meta");
		List<String> values=new ArrayList<String>();
		for (Element metaTag:metaTags) {
			if (metaTag.hasAttr(metaAttributeKeyName)&&metaTag.attr(metaAttributeKeyName).equalsIgnoreCase(metaAttributeKeyValue)) {
				values.add(metaTag.attr("content"));
			}
		}
		
		Iterator<String> valueCleanIterator=values.iterator();
		while (valueCleanIterator.hasNext()) {
			String value=valueCleanIterator.next();
			if (value==null||value.length()==0){
				valueCleanIterator.remove();
			}
		}
		if (values.isEmpty()) {
			return null;
		}
		else {
			return values.get(0);
		}
	}

	/**
	 * Link tag is similar to meta tag, the key attribute name is always rel, and the value is in href attribute
	 * @param head
	 * @param key
	 * @return
	 */
	public static String getLinkValue(@NotNull Element head, @NotNull String key) {
		Elements linkTags=head.getElementsByTag("link");
		List<String> values=new ArrayList<String>();
		for (Element linkTag:linkTags) {
			if (linkTag.hasAttr("rel")&&linkTag.attr("rel").equalsIgnoreCase(key)) {
				values.add(linkTag.attr("href"));
			}
		}
		
		Iterator<String> valueCleanIterator=values.iterator();
		while (valueCleanIterator.hasNext()) {
			String value=valueCleanIterator.next();
			if (value==null||value.length()==0){
				valueCleanIterator.remove();
			}
		}
		if (values.isEmpty()) {
			return null;
		}
		else {
			return values.get(0);
		}
		
		
		
	}

	/**
	 * Returns the lang attribute from the main html tag
	 * @param doc
	 * @return
	 */
	public static String getLanguageFromHTMLTag(@NotNull Document  doc) {
		Elements htmlElements = doc.getElementsByTag("html");
		if (htmlElements.size()!=1) {
			return null;
		}

		else {
			Element htmlElement= htmlElements.get(0);
			if (htmlElement.hasAttr("lang")) {
				return htmlElement.attr("lang"); 
			}
			else {
			return null;
			}
		}
	}

	
}
