package yescomment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import yescomment.util.HTMLParser;

public class HTMLParseTest {

	@Test
	public void testHTMLTitle() throws MalformedURLException, IOException {
		
		assertEquals("GAget – soon! « Plastik média",HTMLParser.getTitle("http://plastik.hu/2013/10/10/gaget-soon/"));
		assertEquals("Why We're Shutting Off Our Comments | Popular Science",HTMLParser.getTitle("http://www.popsci.com/science/article/2013-09/why-were-shutting-our-comments"));
		assertEquals("Index - Gazdaság - Itt az újabb rezsicsökkentés",HTMLParser.getTitle("http://index.hu/gazdasag/2013/10/14/itt_az_ujabb_rezsicsokkentes"));
		
		

	}
	@Test
	public void testHTMLKeywords() throws MalformedURLException, IOException {
		
		assertNull(HTMLParser.getMetaValue("http://plastik.hu/2013/10/10/gaget-soon/","keywords","name"));
		assertEquals("Malaysia Airlines flight MH370,Plane crashes,Malaysia,Vietnam,Asia Pacific,World news,World news",HTMLParser.getMetaValue("http://www.theguardian.com/world/2014/mar/12/malaysia-airlines-search-mired-in-confusion-over-planes-final-path","keywords","name"));
		assertEquals("pc piac idc gartner hp lenovo acer asus dell",HTMLParser.getMetaValue("http://www.hwsw.hu/hirek/51099/pc-piac-idc-gartner-hp-lenovo-acer-asus-dell.html","keywords","name"));
		assertEquals("CNN,CNN Video,CNN news,CNN.com,CNN TV,news,news online,breaking news,U.S. news,world news,weather,business,CNN Money,sports,politics,law,technology,entertainment,education,travel,health,special reports,autos,developing story,news video,CNN Intl",HTMLParser.getMetaValue("http://edition.cnn.com/video/?/video/bestoftv/2013/10/10/pkg-foster-alzheimers-family.cnn","keywords","name"));
		assertEquals("nfü, számít a szava, uniós források, Gazdaság",HTMLParser.getMetaValue("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/","keywords","name"));
		
		
		

	}
}
