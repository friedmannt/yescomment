package yescomment.util;

import java.util.Date;

import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;

/**
 * Last comment date and comment count are redundant data in article. ArticleRedundantCommentDataUtil maintains these two attributes 
 * @author Friedmann Tamás
 *
 */
public class ArticleRedundantCommentDataUtil {

	/**
	 * recounts comments and resets latest comment date
	 * @param article
	 */
	public static void validateRedundantCommentData(@NotNull final Article article) {
		validateCommentCount(article);
		validateLastCommentDate(article);
	
}
	
	/**
	 * recounts comments
	 * @param article
	 */
	public static void validateCommentCount(@NotNull final Article article) {
		
			article.setCommentCount(article.getComments().size());
		
	}
	
	/**
	 * resets latest comment date
	 * @param article
	 */
	public static void validateLastCommentDate(@NotNull final Article article) {
		Date lastCommentDate=null;
		for (Comment comment:article.getComments()) {
			if (lastCommentDate==null||lastCommentDate.before(comment.getCreateDate())) {
				lastCommentDate=comment.getCreateDate();
			}
			
		}
		article.setLastCommentDate(lastCommentDate);
	}
	
}
