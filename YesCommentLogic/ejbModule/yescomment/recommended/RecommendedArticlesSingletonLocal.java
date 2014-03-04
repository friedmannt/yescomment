package yescomment.recommended;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import yescomment.model.Article;

@Local
public interface RecommendedArticlesSingletonLocal extends    RecommendedArticlesSingletonMBean{

	List<Article> retrieveRecommendedArticles(final RecommendationAscpect recommendationAscpect);

	
}
