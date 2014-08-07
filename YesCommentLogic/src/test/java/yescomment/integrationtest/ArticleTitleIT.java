package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.util.ArticleInfo;

public class ArticleTitleIT {

	@Test
	public void testArticleInfoTitle1() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://plst.hu/recqq");
		assertEquals("Első találkozásom a Bitcoinnal", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle2() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://bit.ly/noobtest");

		assertEquals("Minecraft Noob Test - How Much Of A Noob Are You?", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle3() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.hasznaltauto.hu/");

		assertEquals("Használtautó.hu - eladó használt és új autó kereső", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle4() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra?utm_source=ketrec&utm_medium=link&utm_content=2013_10_16&utm_campaign=index");

		assertEquals("Disznót cserélnék iPhone-ra", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle5() throws IOException {
		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=7839");

		assertEquals("Gunner Z – App ajánló | Handras", ai.getTitle());

	}

	@Test()
	public void testArticleInfoTitle6() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/");

		assertEquals("Ennek az egyeztetésnek meg mi értelme?", ai.getTitle());
	}
	
	@Test()
	public void testArticleInfoTitle7() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://velvet.hu/blogok/balaton/2014/07/01/tinder_a_balatonon/");

		assertEquals("Türelmesen tinderezzen a Balatonon!", ai.getTitle());
	}
	
	@Test()
	public void testArticleInfoTitle8() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://plastik.hu/2013/10/10/gaget-soon/");

		assertEquals("GAget – soon!", ai.getTitle());
	}
	@Test()
	public void testArticleInfoTitle9() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.popsci.com/science/article/2013-09/why-were-shutting-our-comments");

		assertEquals("Why We're Shutting Off Our Comments", ai.getTitle());
	}
	@Test()
	public void testArticleInfoTitle10() throws IOException {

		ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://444.hu/2014/03/25/az-alairasok-meg-csak-osszejottek-a-gyanus-partoknak-de-szavazatszamlalokra-mar-nem-futotta/");

		assertEquals("Az aláírások még csak összejöttek a gyanús pártoknak, de szavazatszámlálókra már nem futotta - !!444!!!", ai.getTitle());
	}
	
}
