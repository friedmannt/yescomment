package yescomment.util;

import yescomment.model.Comment;

/**
 * Implementations can be notified on comment creating or comment deleting
 * @author Friedmann Tamás
 *
 */
public interface CommentLifeCycleListener  {

	
	void commentCreated(Comment comment);
	
	void commentDeleted(Comment comment);
}
