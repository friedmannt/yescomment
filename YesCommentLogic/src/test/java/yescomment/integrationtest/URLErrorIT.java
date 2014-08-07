package yescomment.integrationtest;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jsoup.HttpStatusException;
import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;

public class URLErrorIT {

	

	@Test(expected=IllegalArgumentException.class)
	public void testArticleInfoMalformedURL() throws IOException  {
		ArticleInfoRetriever.getArticleInfoFromURL("a.b.hu");
	}
	
	@Test(expected=UnknownHostException.class)
	public void testArticleInfoUnkownHost() throws IOException  {
		ArticleInfoRetriever.getArticleInfoFromURL("http://a.b.hu");
	}
	
	@Test(expected=HttpStatusException.class)
	public void testArticleInfoNotFound1() throws IOException  {
		ArticleInfoRetriever.getArticleInfoFromURL("http://index.hu/bb");
		
	}
	
	@Test(expected=HttpStatusException.class)
	public void testArticleInfoNotFound2() throws IOException  {
		ArticleInfoRetriever.getArticleInfoFromURL("http://www.origo.hu/a.html");
		
	}
	
	
	
	
}
