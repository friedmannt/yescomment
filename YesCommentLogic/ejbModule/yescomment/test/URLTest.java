package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import net.htmlparser.jericho.Source;

import org.junit.Test;

import yescomment.util.HTMLParser;
import yescomment.util.URLUtil;
import yescomment.util.URLUtil.ArticleInfo;

public class URLTest {

	
	@Test
	public void testArticleInfo1() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("plst.hu/recqq");
		assertEquals( "http://plastik.hu/2013/12/07/elso-talalkozasom-a-bitcoinnal/",ai.getFinalURL());
		assertEquals("Első találkozásom a Bitcoinnal « Plastik média",ai.getTitle() );
		
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		

	}
	
	@Test
	public void testArticleInfo2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("bit.ly/noobtest");
		assertEquals("http://everythingtutorials.org/noobtest/", ai.getFinalURL());
		assertEquals("Minecraft Noob Test - How Much Of A Noob Are You?",ai.getTitle() );
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		
	}

	
	@Test
	public void testArticleInfo3() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("plastik.hu");
		assertEquals("http://plastik.hu",ai.getFinalURL());
		assertEquals("Plastik média",ai.getTitle() );
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		
	}
	
	@Test
	public void testArticleInfo4() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra?utm_source=ketrec&utm_medium=link&utm_content=2013_10_16&utm_campaign=index");
		assertEquals( "http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra",ai.getFinalURL());
		assertEquals("Disznót cserélnék iPhone-ra",ai.getTitle() );
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		
	}
	
	@Test
	public void testArticleInfo5() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://handras.hu/?p=7839");
		assertEquals("http://handras.hu/?p=7839",ai.getFinalURL());
		assertEquals("Gunner Z – App ajánló – Handras",ai.getTitle() );
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		
	}

	
	
	
	@Test(expected=UnknownHostException.class)
	public void testArticleInfo6() throws IOException  {
		 URLUtil.getArticleInfoFromURL("a.b.hu");
	}
	
	@Test()
	public void testArticleInfo7() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("www.index.hu/blabla");
		assertEquals(HttpURLConnection.HTTP_NOT_FOUND, ai.getResponseCode());
	}
	
	
	
	
	@Test()
	public void testArticleInfo8() throws IOException  {
		
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://gamerportal.hu/article/3594/How-To-Survive");
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		assertEquals("http://gamerportal.hu/@img/share/gamerportal.png", ai.getImageURL());
	}
	
	@Test()
	public void testArticleInfo9() throws IOException  {
		
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/");
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		assertEquals("Ennek az egyeztetésnek meg mi értelme?", ai.getTitle());
	}
	
	
	
}
