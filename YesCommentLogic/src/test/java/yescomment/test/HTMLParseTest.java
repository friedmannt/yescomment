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
		assertEquals("Az aláírások még csak összejöttek a gyanús pártoknak, de szavazatszámlálókra már nem futotta !!444!!!",HTMLParser.getTitle("http://444.hu/2014/03/25/az-alairasok-meg-csak-osszejottek-a-gyanus-partoknak-de-szavazatszamlalokra-mar-nem-futotta/"));
		
		

	}
	@Test
	public void testHTMLKeywords() throws MalformedURLException, IOException {
		
		assertNull(HTMLParser.getMetaValue("http://plastik.hu/2013/10/10/gaget-soon/","keywords","name"));
		assertEquals("Malaysia Airlines flight MH370,Plane crashes,Malaysia,Vietnam,Asia Pacific,World news,World news",HTMLParser.getMetaValue("http://www.theguardian.com/world/2014/mar/12/malaysia-airlines-search-mired-in-confusion-over-planes-final-path","keywords","name"));
		assertEquals("pc piac idc gartner hp lenovo acer asus dell",HTMLParser.getMetaValue("http://www.hwsw.hu/hirek/51099/pc-piac-idc-gartner-hp-lenovo-acer-asus-dell.html","keywords","name"));
		assertEquals("CNN,CNN Video,CNN news,CNN.com,CNN TV,news,news online,breaking news,U.S. news,world news,weather,business,CNN Money,sports,politics,law,technology,entertainment,education,travel,health,special reports,autos,developing story,news video,CNN Intl",HTMLParser.getMetaValue("http://edition.cnn.com/video/?/video/bestoftv/2013/10/10/pkg-foster-alzheimers-family.cnn","keywords","name"));
		assertEquals("választás 2014, szavazatszámláló bizottság, bizniszpártok",HTMLParser.getMetaValue("http://444.hu/2014/03/25/az-alairasok-meg-csak-osszejottek-a-gyanus-partoknak-de-szavazatszamlalokra-mar-nem-futotta/","keywords","name"));
		
		
		

	}
}
