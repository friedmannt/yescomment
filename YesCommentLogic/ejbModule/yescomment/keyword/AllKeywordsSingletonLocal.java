package yescomment.keyword;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.Local;

import yescomment.util.ArticleListener;

@Local
public interface AllKeywordsSingletonLocal extends   ArticleListener,Serializable{

	Map<String, Integer> retrieveKeywords();

	

}
