package yescomment.util;

import yescomment.model.Article;

/**
 * Implementations can be notified on article creating or article deleting
 * @author Friedmann Tamás
 *
 */
public interface ArticleLifeCycleListener  {

	
	void articleCreated(Article article);
	
	void articleDeleted(Article article);
}
