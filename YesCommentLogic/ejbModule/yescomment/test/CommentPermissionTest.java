package yescomment.test;

import java.io.IOException;

import org.junit.Test;

import yescomment.crawler.nocommentdetector.NoCommentDetector;
import yescomment.crawler.nocommentdetector.NoCommentDetectorService;
import yescomment.util.ArticleCommentPermission;
import yescomment.util.ArticleInfo;
import yescomment.util.URLUtil;


public class CommentPermissionTest {

	
	@Test
	public void testNoCommentsAllowedOrigo1() throws IOException {
		NoCommentDetector nd=NoCommentDetectorService.getNoCommentDetector("origo");
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://www.origo.hu/nagyvilag/20140211-nemzetkozi-zsido-szervezet-surgeti-a-magyar-kormanyt-a-parbeszedre.html",nd);
		org.junit.Assert.assertEquals(ArticleCommentPermission.NOT_ALLOWED, ai.getArticleCommentPermission());
	}
	
	
	@Test
	public void testCommentsAllowedOrigo2() throws IOException {
		NoCommentDetector nd=NoCommentDetectorService.getNoCommentDetector("origo");
		ArticleInfo ai= URLUtil.getArticleInfoFromURL("http://www.origo.hu/itthon/20140303-miert-halogatta-orban-a-megszolalast-az-ukran-helyzetben.html",nd);
		org.junit.Assert.assertEquals(ArticleCommentPermission.NOT_ALLOWED, ai.getArticleCommentPermission());
	
	}
}
