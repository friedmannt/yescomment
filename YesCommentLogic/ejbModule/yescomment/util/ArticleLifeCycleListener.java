package yescomment.util;

import yescomment.model.Article;

public interface ArticleLifeCycleListener  {

	
	void articleCreated(Article article);
	
	void articleDeleted(Article article);
}
