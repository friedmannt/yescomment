package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.util.ArticleInfo;

public class ArticleKeywordsIT {

	
	
	

	
	
	@Test
	public void testHTMLKeywords1() throws MalformedURLException, IOException {
		ArticleInfo ai=ArticleInfoRetriever.getArticleInfoFromURL("http://plastik.hu/2013/10/10/gaget-soon/");
		assertNull(ai.getKeywords());
	}
	
	@Test
	public void testHTMLKeywords2() throws MalformedURLException, IOException {
		ArticleInfo ai=ArticleInfoRetriever.getArticleInfoFromURL("http://www.theguardian.com/world/2014/mar/12/malaysia-airlines-search-mired-in-confusion-over-planes-final-path");
		assertEquals("Malaysia Airlines flight MH370,Plane crashes,Malaysia,Vietnam,Asia Pacific,World news",ai.getKeywords());
	}
	@Test
	public void testHTMLKeywords3() throws MalformedURLException, IOException {
		ArticleInfo ai=ArticleInfoRetriever.getArticleInfoFromURL("http://www.hwsw.hu/hirek/51099/pc-piac-idc-gartner-hp-lenovo-acer-asus-dell.html");
		assertEquals("pc,piac,idc,gartner,hp,lenovo,acer,asus,dell",ai.getKeywords());
	}
	@Test
	public void testHTMLKeywords4() throws MalformedURLException, IOException {
		ArticleInfo ai=ArticleInfoRetriever.getArticleInfoFromURL("http://edition.cnn.com/video/?/video/bestoftv/2013/10/10/pkg-foster-alzheimers-family.cnn");
		assertEquals("breaking news video, news videos, cnn video, videos, Breaking News Videos, Story Video and Show Clips - CNN.com",ai.getKeywords());
	}
	@Test
	public void testHTMLKeywords5() throws MalformedURLException, IOException {
		ArticleInfo ai=ArticleInfoRetriever.getArticleInfoFromURL("http://444.hu/2014/03/25/az-alairasok-meg-csak-osszejottek-a-gyanus-partoknak-de-szavazatszamlalokra-mar-nem-futotta/");
		assertEquals("választás 2014, szavazatszámláló bizottság, bizniszpártok",ai.getKeywords());
	}
	
}
