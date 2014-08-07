package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;

public class URLIT {

	
	@Test
	public void testArticleInfoURLShortened1() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://plst.hu/recqq");
		assertEquals( "http://plastik.hu/2013/12/07/elso-talalkozasom-a-bitcoinnal/",ai.getFinalURL());
		
	}
	
	@Test
	public void testArticleInfoURLShortened2() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://bit.ly/noobtest");
		assertEquals("http://everythingtutorials.org/noobtest/", ai.getFinalURL());
		
		
		
	}

	
	@Test
	public void testArticleInfoFinalURL1() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://plastik.hu/2014/05/03/az-elmult-negyedevben-az-apple-tobb-penzt-koltott-reszveny-visszavasarlasra-mint-amennyi-arbevetelt-csinalt-a-google/");
		assertEquals("http://plastik.hu/2014/05/03/az-elmult-negyedevben-az-apple-tobb-penzt-koltott-reszveny-visszavasarlasra-mint-amennyi-arbevetelt-csinalt-a-google/",ai.getFinalURL());
	
		
		
	}
	
	@Test
	public void testArticleInfoFinalURL2() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra?utm_source=ketrec&utm_medium=link&utm_content=2013_10_16&utm_campaign=index");
		assertEquals( "http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra",ai.getFinalURL());
		
	}
	
	@Test
	public void testArticleInfoFinalURL3() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=7839");
		assertEquals("http://handras.hu/?p=7839",ai.getFinalURL());
	}

	
	@Test
	public void testArticleInfoFinalURL4() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://figaro.postr.hu/holokauszt-emlekkoncert?utm_source=origo-nyito&utm_medium=sec-p&utm_campaign=sec");
		assertEquals("http://figaro.postr.hu/holokauszt-emlekkoncert",ai.getFinalURL());
	}
	
	
	@Test
	public void testURLParts() {
		assertEquals("itunes.apple.com", URLUtil.getSiteOfURL("https://itunes.apple.com/us/tv-season/the-100-season-1/id824282666"));
		assertEquals("https", URLUtil.getSchemaOfURL("https://itunes.apple.com/us/tv-season/the-100-season-1/id824282666"));
		assertEquals("t2n11292", URLUtil.getFragmentOfURL("https://www.openshift.com/faq#t2n11292"));
		
	}
	@Test
	public void testURLValidity() {
		assertTrue(URLUtil.urlIsValid("https://itunes.apple.com/us/tv-season/the-100-season-1/id824282666"));
		assertFalse(URLUtil.urlIsValid("ftp://ftp.funet.fi/pub/standards/RFC/rfc959.txt"));
	}
	@Test
	public void testURLToString() throws IOException {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://figaro.postr.hu/holokauszt-emlekkoncert?utm_source=origo-nyito&utm_medium=sec-p&utm_campaign=sec");
		assertNotNull(ai.toString());
		
	}
	
	
	
}
