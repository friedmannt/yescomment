package yescomment.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import yescomment.util.URLUtil;

public class URLErrorTest {

	

	
	
	@Test(expected=UnknownHostException.class)
	public void testArticleInfoUnkownHost() throws IOException  {
		 URLUtil.getArticleInfoFromURL("a.b.hu");
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testArticleInfoNotFound1() throws IOException  {
		URLUtil.getArticleInfoFromURL("http://index.hu/bb");
		
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testArticleInfoNotFound2() throws IOException  {
		 URLUtil.getArticleInfoFromURL("http://www.origo.hu/a.html");
		
	}
	
	
	
	
}
