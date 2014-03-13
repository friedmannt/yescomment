package yescomment.managedbean.keyword;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

import yescomment.keyword.AllKeywordsSingleton;
import yescomment.keyword.KeywordUtil;

@ApplicationScoped
@ManagedBean
public class KeywordTagCloudBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int KEYWORDS_TAGCLOUD_SIZE=500;
	@EJB
	AllKeywordsSingleton allKeywordsSingleton;
	
	public TagCloudModel getTopKeywordsTagCloudModel()
			throws UnsupportedEncodingException {

		DefaultTagCloudModel model = new DefaultTagCloudModel();
		Map<String, Integer> keywordsAndOccurenceCounts = allKeywordsSingleton
				.retrieveTopKeywords(KEYWORDS_TAGCLOUD_SIZE);
		for (String keyword : keywordsAndOccurenceCounts.keySet()) {

			model.addTag(new DefaultTagCloudItem(keyword, KeywordUtil.getURLofKeyword(keyword),
					keywordsAndOccurenceCounts.get(keyword)));// create keyword
																// url

		}
		return model;
	}
	
	

}
