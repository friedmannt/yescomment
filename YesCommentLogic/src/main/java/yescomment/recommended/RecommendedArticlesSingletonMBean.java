package yescomment.recommended;


public interface RecommendedArticlesSingletonMBean{

	int getLatestArticleLimit();

	void setLatestArticleLimit(int latestArticleLimit);

	int getMostCommentedArticleLimit();

	void setMostCommentedArticleLimit(int mostCommentedArticleLimit);

	
	

	

}
