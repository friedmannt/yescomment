package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.util.ArticleInfo;

public class ArticleLanguageIT {

	@Test
	public void testArticleInfoLanguage1() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://plst.hu/recqq");
		assertEquals("hu", ai.getLanguage());

	}

	
	@Test
	public void testArticleInfoLanguage2() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.hasznaltauto.hu/");

		assertEquals("hu", ai.getLanguage());

	}

	@Test()
	public void testArticleInfoLanguage3() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.autobild.de/artikel/mercedes-cla-test-4211401.html");

		assertEquals("de", ai.getLanguage());
	}
	@Test()
	public void testArticleInfoLanguage4() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.smith-wesson.com/webapp/wcs/stores/servlet/Product4_750001_750051_815078_-1_757784_757784_757784_ProductDisplayErrorView_Y");

		assertEquals("en", ai.getLanguage());
	}

	@Test
	public void testArticleInfoLanguage5() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=7839");

		assertEquals("hu", ai.getLanguage());

	}

	@Test()
	public void testArticleInfoLanguage6() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/");

		assertEquals("hu", ai.getLanguage());
	}
	
	@Test()
	public void testArticleInfoLanguage7() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://velvet.hu/blogok/balaton/2014/07/01/tinder_a_balatonon/");

		assertEquals("hu", ai.getLanguage());
	}
	
	
	
}
