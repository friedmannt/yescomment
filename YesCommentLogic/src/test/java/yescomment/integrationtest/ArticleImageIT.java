package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.util.ArticleInfo;

public class ArticleImageIT {

	
	
	

	@Test
	public void testArticleInfoImage1() throws IOException {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/gazdasag/orban-most-mar-nincs-visszaut-1212218");
		
		assertEquals("http://mno.hu/static/imgs/logo.png", ai.getImageURL());

		
	}

	@Test
	public void testArticleInfoImage2() throws IOException {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://www.speedshop.hu/termekek/2011422/caterpillar-b100.html");
		
		assertEquals("http://www.speedshop.hu/Caterpillar_B-i314161.png", ai.getImageURL());

		
	}
	@Test
	public void testArticleInfoImage3() throws IOException {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("http://velvet.hu/blogok/balaton/2014/07/01/tinder_a_balatonon/");
		
		assertNotNull(ai.getImageURL()); 

		
	}
	
	


	
}
