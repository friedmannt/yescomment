package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;

public class URLTest {

	
	@Test
	public void testArticleInfoURLShortened1() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("plst.hu/recqq");
		assertEquals( "http://plastik.hu/2013/12/07/elso-talalkozasom-a-bitcoinnal/",ai.getFinalURL());
		
		
		
		

	}
	
	@Test
	public void testArticleInfoURLShortened2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("bit.ly/noobtest");
		assertEquals("http://everythingtutorials.org/noobtest/", ai.getFinalURL());
		
		
		
	}

	
	@Test
	public void testArticleInfoURL1() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("plastik.hu");
		assertEquals("http://plastik.hu",ai.getFinalURL());
	
		
		
	}
	
	@Test
	public void testArticleInfoURL2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra?utm_source=ketrec&utm_medium=link&utm_content=2013_10_16&utm_campaign=index");
		assertEquals( "http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra",ai.getFinalURL());
	
		
		
	}
	
	@Test
	public void testArticleInfoURL3() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://handras.hu/?p=7839");
		assertEquals("http://handras.hu/?p=7839",ai.getFinalURL());
		
		
		
	}

	
	
	
}
