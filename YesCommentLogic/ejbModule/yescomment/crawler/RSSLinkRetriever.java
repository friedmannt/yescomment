package yescomment.crawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RSSLinkRetriever {

	
	public static List<String> getItemLinksFromRSS(String rssUrl,DocumentBuilder documentBuilder) throws SAXException, IOException  {
		
		List<String> urls=new ArrayList<String>();
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
				urls.add(linkUrl);
	 
			}
		}
		return urls;
	}
	
	
	

}
