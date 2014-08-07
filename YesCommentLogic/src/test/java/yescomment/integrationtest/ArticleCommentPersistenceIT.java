package yescomment.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

import yescomment.articleretriever.ArticleInfoRetriever;
import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.util.AnonymusUserName;
import yescomment.util.ArticleInfo;
import yescomment.util.VoteDirection;

public class ArticleCommentPersistenceIT extends AbstractPersistenceIT{

	
	@Test
	public void testArticleCreation() {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Article article = new Article();
			article.setTitle("Test Title");
			article.setUrl("www.google.com");
			article.setCommentCount(0);

			article = articleManager.create(article);
			assertNotNull(article.getId());
			article = articleManager.find(article.getId());
			assertEquals("Test Title", article.getTitle());
			assertEquals(1, articleManager.findAll().size());
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}

	}

	@Test
	public void testArticleURLRetrieve() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://bit.ly/noobtest");
			Article article = articleManager.createArticleFromArticleInfo(ai);
			article = articleManager.create(article);

			assertEquals("http://everythingtutorials.org/noobtest/", article.getUrl());

			Article foundArticle = articleManager.getArticleByURL("http://everythingtutorials.org/noobtest/");

			assertEquals(foundArticle.getId(), article.getId());
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

	@Test
	public void testArticleKeywords() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.theguardian.com/media/2014/jul/03/loaded-lads-mag-era-cover-models");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				article = articleManager.create(article);
			}
			{
				ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://www.theguardian.com/business/2014/jul/03/wage-gap-opens-up-between-younger-and-older-workers");
				Article article = articleManager.createArticleFromArticleInfo(ai);
				article = articleManager.create(article);
			}
			List<String> keywords = articleManager.getAllKeywords(true);
			assertTrue(keywords.containsAll(Arrays.asList("Executive pay and bonuses", "Business")));
			List<String> keywordsWithB = articleManager.getAllKeywords("B", true);
			assertTrue(keywordsWithB.contains("Business"));
			assertFalse(keywordsWithB.contains("Executive pay and bonuses"));
			List<Article> articlesWithBusinessKeyword = articleManager.getArticlesWithKeyword("Business", null);
			assertFalse(articlesWithBusinessKeyword.isEmpty());
			assertEquals(2, articleManager.getArticlesOnDate(new Date()).size());
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

	@Test
	public void testArticleComments() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			ArticleInfo ai1 = ArticleInfoRetriever.getArticleInfoFromURL("https://www.youtube.com/watch?v=_sDy4wzc6-c&list=FLZXdqOoxxNrZuL2MFPeR2uA&index=31");
			Article article1 = articleManager.createArticleFromArticleInfo(ai1);
			article1 = articleManager.create(article1);
			article1 = commentManager.addCommentToArticle(article1, "Jó zene", AnonymusUserName.ANONYMUS_USER_NAME, null);
			article1 = commentManager.addCommentToArticle(article1, "Igen", AnonymusUserName.ANONYMUS_USER_NAME, null);

			ArticleInfo ai2 = ArticleInfoRetriever.getArticleInfoFromURL("https://www.youtube.com/watch?v=nIwociNAJQA");
			Article article2 = articleManager.createArticleFromArticleInfo(ai2);
			article2 = articleManager.create(article2);
			article2 = commentManager.addCommentToArticle(article2, "Rossz zene", AnonymusUserName.ANONYMUS_USER_NAME, null);
			article2 = commentManager.addCommentToArticle(article2, "Nem, jó", AnonymusUserName.ANONYMUS_USER_NAME, null);
			article2 = commentManager.addCommentToArticle(article2, "Kedvenc", AnonymusUserName.ANONYMUS_USER_NAME, null);
			assertEquals(2,article1.getCommentCount().intValue() );
			assertEquals(2, article1.getComments().size());
			assertEquals(3,article2.getCommentCount().intValue());
			assertEquals(3, article2.getComments().size());
			assertNotNull(article1.getLastCommentDate());
			assertNotNull(article2.getLastCommentDate());

			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

	@Test
	public void testArticleCommentRemoveAndHide() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("https://www.youtube.com/watch?v=_sDy4wzc6-c&list=FLZXdqOoxxNrZuL2MFPeR2uA&index=31");
			Article article = articleManager.createArticleFromArticleInfo(ai);
			article = articleManager.create(article);
			assertNull(commentManager.getLastCommentOfArticle(article));
			article = commentManager.addCommentToArticle(article, "Jó zene",AnonymusUserName.ANONYMUS_USER_NAME, null);
			article = commentManager.addCommentToArticle(article, "Igen", AnonymusUserName.ANONYMUS_USER_NAME, null);
			for (Comment comment : article.getComments()) {
				commentManager.hideComment(comment.getId());
			}
			for (Comment comment : article.getComments()) {
				assertEquals(Boolean.TRUE, comment.getHidden());
			}
			for (Comment comment : article.getComments()) {
				commentManager.unhideComment(comment.getId());
			}
			for (Comment comment : article.getComments()) {
				assertEquals(Boolean.FALSE, comment.getHidden());
			}

			while (commentManager.getLastCommentOfArticle(article) != null) {
				commentManager.removeComment(commentManager.getLastCommentOfArticle(article).getId());
			}

			assertEquals(0,article.getCommentCount().intValue());
			assertEquals(0, article.getComments().size());

			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

	@Test
	public void testArticleCommentVote() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://daringfireball.net/thetalkshow/");
			Article article = articleManager.createArticleFromArticleInfo(ai);
			article = articleManager.create(article);
			article = commentManager.addCommentToArticle(article, "Jó a podcast", "feri", null);
			Comment comment = commentManager.getLastCommentOfArticle(article);
			voteManager.voteOnComment(comment.getId(), "laci", VoteDirection.UP);
			voteManager.voteOnComment(comment.getId(), "laci", VoteDirection.UP);
			voteManager.voteOnComment(comment.getId(), "laci", VoteDirection.DOWN);
			voteManager.voteOnComment(comment.getId(), "feri", VoteDirection.DOWN);
			voteManager.voteOnComment(comment.getId(), "pisti", VoteDirection.DOWN);
			assertEquals(1,comment.getUpVoteCount().intValue());
			assertEquals(1,comment.getDownVoteCount().intValue());

			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

	@Test
	public void testCommentReplyAndDeleteReplied() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			ArticleInfo ai = ArticleInfoRetriever.getArticleInfoFromURL("http://daringfireball.net/thetalkshow/");
			Article article = articleManager.createArticleFromArticleInfo(ai);
			article = articleManager.create(article);
			article = commentManager.addCommentToArticle(article, "Jó a podcast", "feri", null);
			Comment originalComment = commentManager.getLastCommentOfArticle(article);
			article = commentManager.addCommentToArticle(article, "Milyen igaz", "laci", originalComment.getId());
			Comment replyComment = commentManager.getLastCommentOfArticle(article);
			assertEquals(1, commentManager.getRepliesOfComment(originalComment.getId()).size());
			commentManager.removeComment(originalComment.getId());
			assertNull(replyComment.getReplyOf());
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}
	}

	@Test
	public void testArticleWithoutLanguage() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			

				articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://daringfireball.net/thetalkshow/")));
		
				articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8974")));
				articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8968")));
				articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8965")));
				articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://bekenagykovet.hu/chem/sved1.html")));
				

			List<Article> articlesWithoutLanguage=articleManager.getArticlesWithoutLanguage();
			for (Article article:articlesWithoutLanguage) {
				assertNull(article.getLanguage());
			}
			
			tx.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (tx.isActive()) {
				tx.rollback();
			}
			throw t;
		}

	}
	
	
	@Test
	public void testArticleCommentMerge() throws IOException {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			

				Article article1 = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://daringfireball.net/thetalkshow/")));
				for (int i = 1; i <= 100; i++) {
					commentManager.addCommentToArticle(article1, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
				}

			
			

				Article article2 = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8974")));
				for (int i = 1; i <= 50; i++) {
					commentManager.addCommentToArticle(article2, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
				}

			
			

				Article article3 = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8968")));
				for (int i = 1; i <= 10; i++) {
					commentManager.addCommentToArticle(article3, "Comment" + i, AnonymusUserName.ANONYMUS_USER_NAME, null);
				}

			
			

				Article bigArticle = articleManager.create(articleManager.createArticleFromArticleInfo(ArticleInfoRetriever.getArticleInfoFromURL("http://handras.hu/?p=8965")));
				articleManager.mergeArticlesComments(article1.getId(), bigArticle.getId());
				articleManager.mergeArticlesComments(article2.getId(), bigArticle.getId());
				articleManager.mergeArticlesComments(article3.getId(), bigArticle.getId());
				
				assertNull(articleManager.find(article1.getId()));
				assertNull(articleManager.find(article2.getId()));
				assertNull(articleManager.find(article3.getId()));
				assertEquals(100+50+10	, bigArticle.getCommentCount().intValue());

			
			
			
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
