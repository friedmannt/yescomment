package yescomment.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import yescomment.util.CommentLinkDetector;
import yescomment.util.CommentLinkDetector.CommentContent;

public class CommentLinkDetectorTest {

	
	@Test
	public void testNoLink() {
		List<CommentContent> commentContents =CommentLinkDetector.detectLinksInText("Szervusztok gyerekek");
		assertEquals(1,commentContents.size());
		assertEquals("Szervusztok gyerekek",commentContents.get(0).getContent());
		assertFalse(commentContents.get(0).isLink());
		
	}

	
	@Test
	public void testOneLink() {
		List<CommentContent> commentContents =CommentLinkDetector.detectLinksInText("http://www.index.hu");
		assertEquals(1,commentContents.size());
		assertEquals("http://www.index.hu",commentContents.get(0).getContent());
		assertTrue(commentContents.get(0).isLink());
		
	}
	
	@Test
	public void testSomeLink() {
		List<CommentContent> commentContents =CommentLinkDetector.detectLinksInText("Próba szöveg http://www.index.hu és http://wwww.google.com");
		assertEquals(4,commentContents.size());
		assertEquals("Próba szöveg ",commentContents.get(0).getContent());
		assertFalse(commentContents.get(0).isLink());

		assertEquals("http://www.index.hu",commentContents.get(1).getContent());
		assertTrue(commentContents.get(1).isLink());

		assertEquals(" és ",commentContents.get(2).getContent());
		assertFalse(commentContents.get(2).isLink());

		assertEquals("http://wwww.google.com",commentContents.get(3).getContent());
		assertTrue(commentContents.get(3).isLink());

		
	}

	
	
}
