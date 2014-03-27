package yescomment.managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Article;
import yescomment.recommended.RecommendationAscpect;
import yescomment.recommended.RecommendedArticlesSingletonLocal;

@ManagedBean
@ViewScoped
public class RecommendedArticlesManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	RecommendedArticlesSingletonLocal recommendedArticlesSingleton;

	List<Article> latestArticles;

	List<Article> mostCommentedArticles;
	
	

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

	@PostConstruct
	public void setSelectedRecommendationAspectToLatest() {
		latestArticles = recommendedArticlesSingleton.retrieveRecommendedArticles(RecommendationAscpect.LATEST);
		mostCommentedArticles = recommendedArticlesSingleton.retrieveRecommendedArticles(RecommendationAscpect.MOSTCOMMENTED);
		selectedRecommendationAspect = RecommendationAscpect.LATEST;
	}

}
