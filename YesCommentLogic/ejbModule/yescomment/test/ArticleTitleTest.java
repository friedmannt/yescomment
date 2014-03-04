package yescomment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;

public class ArticleTitleTest {

	@Test
	public void testArticleInfoTitle1() throws IOException {
		ArticleInfo ai = URLUtil.getArticleInfoFromURL("plst.hu/recqq");
		assertEquals("Első találkozásom a Bitcoinnal « Plastik média", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle2() throws IOException {
		ArticleInfo ai = URLUtil.getArticleInfoFromURL("bit.ly/noobtest");

		assertEquals("Minecraft Noob Test - How Much Of A Noob Are You?", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle3() throws IOException {
		ArticleInfo ai = URLUtil.getArticleInfoFromURL("plastik.hu");

		assertEquals("Plastik média", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle4() throws IOException {
		ArticleInfo ai = URLUtil.getArticleInfoFromURL("http://colorcam.blog.hu/2013/10/16/disznot_cserelnek_iphone-ra?utm_source=ketrec&utm_medium=link&utm_content=2013_10_16&utm_campaign=index");

		assertEquals("Disznót cserélnék iPhone-ra", ai.getTitle());

	}

	@Test
	public void testArticleInfoTitle5() throws IOException {
		ArticleInfo ai = URLUtil.getArticleInfoFromURL("http://handras.hu/?p=7839");

		assertEquals("Gunner Z – App ajánló – Handras", ai.getTitle());

	}

	@Test()
	public void testArticleInfoTitle6() throws IOException {

		ArticleInfo ai = URLUtil.getArticleInfoFromURL("http://index.hu/gazdasag/2013/10/28/ennek_az_egyeztetesnek_meg_mi_ertelme/");

		assertEquals("Ennek az egyeztetésnek meg mi értelme?", ai.getTitle());
	}

}
