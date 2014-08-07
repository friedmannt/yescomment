package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.model.Article;
import yescomment.util.ArticleInfo;

public class KeywordRetrieveIT extends AbstractPersistenceIT {

	@Test
	public void testKeywordRetrieve() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/hirtv_gazdasagi_hirei/40-szazalekkal-is-csokkenhet-a-havi-torlesztoreszlet-1235924");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/hirtvarchiv/rogan-az-allam-vallalhatja-a-terhek-egy-reszet-1235858");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/belfold/szorosabb-kapcsolatra-torekednek-kinaval-1235814");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/belfold/kiszedik-a-partpolitikat-a-rendszerbol-1235805");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				 articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/belfold/orban-eljohet-a-fair-bankok-korszaka-1235660");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				 articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/belfold/elfogadtak-a-devizahiteleseket-segito-torvenyt-1235617");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				 articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://mno.hu/hirtvarchiv/teriteken-a-devizahitelesek-mentese-es-a-reklamado-1235543");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				 articleManager.create(article);
			}


			
			
			Map<String, Integer> keywordsAndOccurences= allKeywordsSingleton.retrieveTopKeywords(10);
			assertTrue(keywordsAndOccurences.size()<=10);
			assertEquals(3, keywordsAndOccurences.get("devizahitel").intValue());
			assertEquals(2, keywordsAndOccurences.get("Rogán Antal").intValue());
			assertNull(keywordsAndOccurences.get("MSZP"));
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}

	}

}
