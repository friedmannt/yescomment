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
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("tiny.cc/virgosysop");
		assertEquals( "http://talentscreener.com/screen/registration/JE8G17WXDBWHSGYH",ai.getFinalURL());
		assertEquals("Jelentkezés - Talentscreener",ai.getTitle() );
		
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		

	}
	
	@Test
	public void testArticleInfo2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("bit.ly/noobtest");
		assertEquals("http://everythingtutorials.org/noobtest/", ai.getFinalURL());
		assertEquals("Minecraft Noob Test - How Much Of A Noob Are You? Test your Minecraft Skills!",ai.getTitle() );
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
		assertEquals("Disznót cserélnék iPhone-ra - Colorcam",ai.getTitle() );
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
		Source source = new Source(new URL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/"));
		String title =(HTMLParser.getTitle(source));
		assertEquals("Index - Gazdaság - Ennek az egyeztetésnek meg mi értelme?", title);
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/");
		assertEquals(HttpURLConnection.HTTP_OK, ai.getResponseCode());
		assertEquals("Index - Gazdaság - Ennek az egyeztetésnek meg mi értelme?", ai.getTitle());
	}
	
	
}
