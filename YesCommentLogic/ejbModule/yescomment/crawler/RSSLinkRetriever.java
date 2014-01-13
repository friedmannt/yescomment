package yescomment.crawler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RSSLinkRetriever {

	
	public static List<RSSItem> getItemsFromRSS(String rssUrl,DocumentBuilder documentBuilder) throws SAXException, IOException, DOMException, ParseException {
		DateFormat dateFormatterRssPubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		List<RSSItem> rssItems=new ArrayList<RSSItem>();
		Document doc = documentBuilder.parse(rssUrl);
	 
		//optional, but recommended
		doc.getDocumentElement().normalize();

		Element channelElement=(Element)doc.getElementsByTagName("channel").item(0);
		
		NodeList itemNodeList = channelElement.getElementsByTagName("item");
	 
		
	 
		for (int temp = 0; temp < itemNodeList.getLength(); temp++) {
	 
			Node itemNode = itemNodeList.item(temp);
	 
		
			if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element itemElement = (Element) itemNode;
	 
				Element linkElement=(Element)itemElement.getElementsByTagName("link").item(0);
				String linkUrl = linkElement.getTextContent();
				Element pubDateElement=(Element)itemElement.getElementsByTagName("pubDate").item(0);
				Date pubDate = dateFormatterRssPubDate .parse( pubDateElement.getTextContent());
				RSSItem rssItem=new RSSItem();
				rssItem.setLink(linkUrl);
				rssItem.setPubDate(pubDate);
				
				rssItems.add(rssItem);
	 
			}
		}
		return rssItems;
	}
	
	
	

}
