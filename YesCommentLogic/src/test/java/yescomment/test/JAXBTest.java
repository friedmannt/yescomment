package yescomment.test;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.model.Vote;
import yescomment.model.Vote.VoteDirection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JAXBTest {

	
	@Test
	public  void marshalUnMarshalTest() throws JAXBException {
		Article article=new Article();
		article.setUrl("www.index.hu");
		article.setTitle("Index hírportál");
		article.setCreateDate(new Date());
		article.setLanguage("hu");
		for (int i=1;i<=10;i++) {
			Comment comment=new Comment();
			comment.setArticle(article);
			article.getComments().add(comment);
			article.setCommentCount(article.getCommentCount()+1);
			comment.setCreateDate(new Date());
			article.setLastCommentDate(comment.getCreateDate());
			comment.setAuthor("feri");
			comment.setCommentText("BLABLA");
			for (int j=1;j<=2;j++) {
				Vote vote=new Vote();
				vote.setComment(comment);
				comment.getVotes().add(vote);
				comment.setUpVoteCount(comment.getUpVoteCount()+1);
				vote.setCreateDate(new Date());
				vote.setVoteDirection(VoteDirection.UP);
				vote.setUserName("laci");
			}
			
		}
		article.getKeywords().add("politika");
		article.getKeywords().add("botrány");
		JAXBContext jc = JAXBContext.newInstance( "yescomment.model" );
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw=new StringWriter();
		m.marshal(article, sw);
		String xmlRepresentation=sw.toString();
		Unmarshaller u = jc.createUnmarshaller();
		Article resultArticle = (Article)
		    u.unmarshal(new StringReader(xmlRepresentation));
		assertEquals(article.getTitle(), resultArticle.getTitle());
		assertEquals(article.getCommentCount(), resultArticle.getCommentCount());
		assertEquals(article.getCreateDate(), resultArticle.getCreateDate());
	}
	
}
