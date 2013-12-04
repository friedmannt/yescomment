package yescomment.util;

import java.io.Serializable;

import yescomment.model.Article;

public interface ArticleListener  {

	
	void articleCreated(Article article);
	
	void articleDeleted(Article article);
}
