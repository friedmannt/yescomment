package yescomment.keyword;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.Local;

import yescomment.util.ArticleLifeCycleListener;

@Local
public interface AllKeywordsSingletonLocal extends   ArticleLifeCycleListener,Serializable{

	/**
	 * Retrieves top N keywords, ordered by occurence count
	 * @param n
	 * @return
	 */
	Map<String, Integer> retrieveTopKeywords(int n);

	

}
