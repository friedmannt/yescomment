package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import yescomment.util.URLUtil;
import yescomment.util.URLUtil.ArticleInfo;

public class URLTest {

	
	@Test
	public void testArticleInfoTitleResponseCodeURLShortened1() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("plst.hu/recqq");
		assertEquals( "http://plastik.hu/2013/12/07/elso-talalkozasom-a-bitcoinnal/",ai.getFinalURL());
		assertEquals("Első találkozásom a Bitcoinnal « Plastik média",ai.getTitle() );
		
		
		

	}
	
	@Test
	public void testArticleInfoTitleResponseCodeURLShortened2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("bit.ly/noobtest");
		assertEquals("http://everythingtutorials.org/noobtest/", ai.getFinalURL());
		assertEquals("Minecraft Noob Test - How Much Of A Noob Are You?",ai.getTitle() );
		
		
	}

	
	@Test
	public void testArticleInfoTitle1() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("plastik.hu");
		assertEquals("http://plastik.hu",ai.getFinalURL());
		assertEquals("Plastik média",ai.getTitle() );
		
		
	}
	
	@Test
	public void testArticleInfoTitle2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra?utm_source=ketrec&utm_medium=link&utm_content=2013_10_16&utm_campaign=index");
		assertEquals( "http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra",ai.getFinalURL());
		assertEquals("Disznót cserélnék iPhone-ra",ai.getTitle() );
		
		
	}
	
	@Test
	public void testArticleInfoTitle3() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://handras.hu/?p=7839");
		assertEquals("http://handras.hu/?p=7839",ai.getFinalURL());
		assertEquals("Gunner Z – App ajánló – Handras",ai.getTitle() );
		
		
	}

	
	@Test()
	public void testArticleInfoTitle4() throws IOException  {
		
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/");
		
		assertEquals("Ennek az egyeztetésnek meg mi értelme?", ai.getTitle());
	}
	
	@Test(expected=UnknownHostException.class)
	public void testArticleInfoUnkownHost() throws IOException  {
		 URLUtil.getArticleInfoFromURL("a.b.hu");
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testArticleInfoNotFound1() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://index.hu/bb");
		
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testArticleInfoNotFound2() throws IOException  {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://www.origo.hu/a.html");
		
	}
	
	
	
	@Test()
	public void testArticleInfoImage() throws IOException  {
		
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://gamerportal.hu/article/3594/How-To-Survive");
		
		assertEquals("http://gamerportal.hu/@img/share/gamerportal.png", ai.getImageURL());
	}
	

	
	
	
}
