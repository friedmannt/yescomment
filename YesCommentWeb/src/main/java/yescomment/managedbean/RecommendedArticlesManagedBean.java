package yescomment.managedbean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Article;
import yescomment.recommended.RecommendationAscpect;
import yescomment.recommended.RecommendedArticlesRetriever;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@ViewScoped
public class RecommendedArticlesManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	RecommendedArticlesRetriever recommendedArticlesRetriever;

	List<Article> latestArticles;

	List<Article> mostCommentedArticles;
	
	List<Article> lastCommentedArticles;
	
	

	public List<Article> getLatestArticles() {
		return latestArticles;
	}

	public void setLatestArticles(List<Article> latestArticles) {
		this.latestArticles = latestArticles;
	}

	public List<Article> getMostCommentedArticles() {
		return mostCommentedArticles;
	}

	public void setMostCommentedArticles(List<Article> mostCommentedArticles) {
		this.mostCommentedArticles = mostCommentedArticles;
	}

	
	
	public List<Article> getLastCommentedArticles() {
		return lastCommentedArticles;
	}

	public void setLastCommentedArticles(List<Article> lastCommentedArticles) {
		this.lastCommentedArticles = lastCommentedArticles;
	}



	private RecommendationAscpect selectedRecommendationAspect;

	public RecommendationAscpect getSelectedRecommendationAspect() {
		return selectedRecommendationAspect;
	}

	public void setSelectedRecommendationAspect(RecommendationAscpect selectedRecommendationAspect) {
		this.selectedRecommendationAspect = selectedRecommendationAspect;
	}

	public boolean latestSelected() {

		return selectedRecommendationAspect == RecommendationAscpect.LATEST;
	}

	public boolean mostCommentedSelected() {
		return selectedRecommendationAspect == RecommendationAscpect.MOSTCOMMENTED;
	}
	
	public boolean lastCommentedSelected() {
		return selectedRecommendationAspect == RecommendationAscpect.LASTCOMMENTED;
	}


	@PostConstruct
	public void initializeRecommendedArticles() {
		latestArticles = recommendedArticlesRetriever.retrieveRecommendedArticles(RecommendationAscpect.LATEST);
		mostCommentedArticles = recommendedArticlesRetriever.retrieveRecommendedArticles(RecommendationAscpect.MOSTCOMMENTED);
		lastCommentedArticles = recommendedArticlesRetriever.retrieveRecommendedArticles(RecommendationAscpect.LASTCOMMENTED);
		selectedRecommendationAspect = RecommendationAscpect.LATEST;
	}
	
	public String getRecommendationAscpectTranslation(final RecommendationAscpect recommendationAscpect) {
		Locale locale = JSFUtil.getLocale();
		return LocalizationUtil.getEnumTranslation(recommendationAscpect, locale);
	}

}
