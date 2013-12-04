package yescomment.util;

import java.io.Serializable;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface AllKeywordsSingletonLocal extends   ArticleListener,Serializable{

	Map<String, Integer> retrieveKeywords();

	

}
