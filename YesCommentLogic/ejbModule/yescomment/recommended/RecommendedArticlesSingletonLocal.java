package yescomment.recommended;

import java.util.List;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;

@Local
public interface RecommendedArticlesSingletonLocal extends    RecommendedArticlesSingletonMBean{

	List<Article> retrieveRecommendedArticles(@NotNull final RecommendationAscpect recommendationAscpect);

	
}
