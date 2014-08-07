package yescomment.recommended;

import yescomment.persistence.search.ArticleOrder;

/**
 * Possible values of recommendation aspect. Recommendation aspects are backed with article order
 * 
 * 
 * 
 */
public enum RecommendationAscpect {
	LATEST(ArticleOrder.ARTICLE_CREATE_DATE), MOSTCOMMENTED(ArticleOrder.ARTICLE_COMMENT_COUNT),LASTCOMMENTED(ArticleOrder.ARTICLE_LAST_COMMENT_DATE);
	private final  ArticleOrder articleOrder;

	public ArticleOrder getArticleOrder() {
		return articleOrder;
	}

	private RecommendationAscpect(ArticleOrder articleOrder) {
		this.articleOrder = articleOrder;
	}
	
	
}