package yescomment.recommended;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;

/**
 * EJB retrieving recommended articles. Does not cache results
 * 
 * 
 * @author Friedmann Tamás
 * 
 */
@Stateless
public class RecommendedArticlesRetriever implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int ARTICLE_LIMIT = 20;

	@EJB
	protected ArticleManager articleManager;

	public List<Article> retrieveRecommendedArticles(final @NotNull RecommendationAscpect recommendationAscpect) {
		// minimum create date is one month before
		Calendar minCreateCal = Calendar.getInstance();
		minCreateCal.add(Calendar.MONTH, -1);
		final String attributeName = recommendationAscpect.getArticleOrder().getAttributeName();
		final Class<?> attributeClass = recommendationAscpect.getArticleOrder().getAttributeClass();
		return articleManager.getEntitiesOrdered(attributeName, attributeClass, false, ARTICLE_LIMIT, minCreateCal.getTime());

	}

}
