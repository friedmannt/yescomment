package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;

public class ArticleImageTest {

	
	
	@Test()
	public void testArticleInfoImage1() throws IOException  {
		
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://gamerportal.hu/article/3594/How-To-Survive");
		
		assertEquals("http://gamerportal.hu/@img/share/gamerportal.png", ai.getImageURL());
	}
	

	@Test
	public void testArticleInfoImage2() throws IOException {
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://mno.hu/gazdasag/orban-most-mar-nincs-visszaut-1212218");
		
		assertEquals("http://mno.hu/data/cikk/1/21/22/18/cikk_1212218/orban_BA_uj_fekvo_lead_fill_345x200.jpg", ai.getImageURL());

		
	}


	
}
