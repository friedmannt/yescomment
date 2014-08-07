package yescomment.integrationtest;

import java.io.IOException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;

public class NotHtmlIT {

	
	@Test(expected=IOException.class)
	public void testArticleInfoURLShortened1() throws IOException  {
		ArticleInfoRetriever.getArticleInfoFromURL("http://www.asus.com/media/global/products/YVF7HRbsK0Xt6t2h/P_setting_fff_1_90_end_500.png");
		
	}
	
	 
	
}
