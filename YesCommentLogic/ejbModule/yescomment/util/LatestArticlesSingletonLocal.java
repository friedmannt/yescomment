package yescomment.util;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import yescomment.model.Article;

@Local
public interface LatestArticlesSingletonLocal extends   ArticleListener,Serializable{

	List<Article> retrieveLatestArticles();

}
