package yescomment.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;

/**
 * Singleton, holding all the keywords. If articles are created or deleted, it is notified
 * @author Friedmann Tamás
 *
 */
@Singleton

public class AllKeywordsSingleton implements AllKeywordsSingletonLocal{
	@EJB
	ArticleManager articleManager;
	

	private Map<String,Integer> keywords; //value is occurence count

	@Override
	public Map<String,Integer> retrieveKeywords() {
		if (keywords == null) {
			populateKeywords();
		}
		return keywords;
	}


	private void populateKeywords() {
		//get all keywords database
		
		List<String> allKeywords = articleManager.getAllKeywords(false);
		keywords=new HashMap<String, Integer>(allKeywords.size());//initial size
		for (String keyword:allKeywords) {
			int currentKeywordCount=0;
			if (keywords.containsKey(keyword)) {
			currentKeywordCount=keywords.get(keyword);	
				
			}
			
			keywords.put(keyword, currentKeywordCount+1);
		}
	}


	@Override
	public void articleCreated(Article article) {
		if (keywords == null) {
			populateKeywords();
		}
		for (String keyword:article.getKeywords()) {
			int currentKeywordCount=0;
			if (keywords.containsKey(keyword)) {
			currentKeywordCount=keywords.get(keyword);	
				
			}
			
			keywords.put(keyword, currentKeywordCount+1);
		}
		
	}


	@Override
	public void articleDeleted(Article article) {
		if (keywords == null) {
			populateKeywords();
		}
		for (String keyword:article.getKeywords()) {
			int currentKeywordCount=0;
			if (keywords.containsKey(keyword)) {
			currentKeywordCount=keywords.get(keyword);	
				
			}
			if (currentKeywordCount!=1) {
				keywords.put(keyword, currentKeywordCount-1);	
			}
			else {
				keywords.remove(keyword);
			}
			
		}
		
	}

}
