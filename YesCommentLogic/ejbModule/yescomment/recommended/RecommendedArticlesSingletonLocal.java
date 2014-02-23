package yescomment.recommended;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import yescomment.model.Article;
import yescomment.util.ArticleLifeCycleListener;
import yescomment.util.CommentLifeCycleListener;

@Local
public interface RecommendedArticlesSingletonLocal extends    Serializable{

	List<Article> retrieveRecommendedArticles(final RecommendationAscpect recommendationAscpect);

	
}
