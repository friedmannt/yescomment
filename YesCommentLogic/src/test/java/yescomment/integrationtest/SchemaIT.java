package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.util.ArticleInfo;

public class SchemaIT {

	
	@Test
	public void testEliminateHttps() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("https://www.youtube.com/watch?v=schQZY3QjCw");
		assertEquals( "http://www.youtube.com/watch?v=schQZY3QjCw",ai.getFinalURL());
	}
	@Test
	public void testNotEliminateHttps() throws IOException  {
		ArticleInfo ai= ArticleInfoRetriever.getArticleInfoFromURL("https://www.otpbank.hu/portal/hu/IR_reszveny_reszvenyesi");
		assertEquals( "https://www.otpbank.hu/portal/hu/IR_reszveny_reszvenyesi",ai.getFinalURL());
	}

	
}
