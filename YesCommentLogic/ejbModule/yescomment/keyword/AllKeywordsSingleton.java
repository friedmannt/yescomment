package yescomment.keyword;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ArticleLifeCycleListener;
import yescomment.util.MapSorter;

/**
 * Singleton, holding all the keywords. If articles are created or deleted, it
 * is notified
 * 
 * @author Friedmann Tamás
 * 
 */
@Singleton
@LocalBean
public class AllKeywordsSingleton implements ArticleLifeCycleListener,Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	ArticleManager articleManager;

	private Map<String, Integer> keywords; // value is occurence count, all
											// keywords contained

	/**
	 * Retrieves keywords (the top n) based on occurece count
	 * 
	 * @param n
	 * @return
	 */

	@Lock(LockType.READ)
	public Map<String, Integer> retrieveTopKeywords(int n) {
		if (keywords == null) {
			populateKeywords();
		}

		// sorting
		Map<String, Integer> sortedKeywords = MapSorter.sortByValues(keywords, false);

		Map<String, Integer> sortedLimitedKeywords = new HashMap<String, Integer>(n);
		int i = 0;
		for (Entry<String, Integer> entry : sortedKeywords.entrySet()) {
			i++;
			if (i <= n) {
				// up until n
				sortedLimitedKeywords.put(entry.getKey(), entry.getValue());
			} else {
				break;// reached n
			}

		}

		return sortedLimitedKeywords;
	}

	@Lock(LockType.WRITE)
	private void populateKeywords() {
		// get all keywords from database

		List<String> allKeywords = articleManager.getAllKeywords(false);
		keywords = new HashMap<String, Integer>(allKeywords.size());// initial
																	// size
		for (String keyword : allKeywords) {
			int currentKeywordCount = 0;
			if (keywords.containsKey(keyword)) {
				currentKeywordCount = keywords.get(keyword);

			}

			keywords.put(keyword, currentKeywordCount + 1);
		}
	}

	@Override
	@Lock(LockType.WRITE)
	public void articleCreated(Article article) {
		if (keywords == null) {
			populateKeywords();
		}
		for (String keyword : article.getKeywords()) {
			int currentKeywordCount = 0;
			if (keywords.containsKey(keyword)) {
				currentKeywordCount = keywords.get(keyword);

			}

			keywords.put(keyword, currentKeywordCount + 1);
		}

	}

	@Override
	@Lock(LockType.WRITE)
	public void articleDeleted(Article article) {
		if (keywords == null) {
			populateKeywords();
		}
		for (String keyword : article.getKeywords()) {
			int currentKeywordCount = 0;
			if (keywords.containsKey(keyword)) {
				currentKeywordCount = keywords.get(keyword);

			}
			if (currentKeywordCount != 1) {
				keywords.put(keyword, currentKeywordCount - 1);
			} else {
				keywords.remove(keyword);
			}

		}

	}

}
