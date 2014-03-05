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

import yescomment.keyword.AllKeywordsSingletonLocal;
import yescomment.keyword.KeywordUtil;

@ApplicationScoped
@ManagedBean
public class KeywordTagCloudBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	AllKeywordsSingletonLocal allKeywordsSingleton;
	
	public TagCloudModel getTopKeywordsTagCloudModel()
			throws UnsupportedEncodingException {

		DefaultTagCloudModel model = new DefaultTagCloudModel();
		Map<String, Integer> keywordsAndOccurenceCounts = allKeywordsSingleton
				.retrieveTopKeywords(500);
		for (String keyword : keywordsAndOccurenceCounts.keySet()) {

			model.addTag(new DefaultTagCloudItem(keyword, KeywordUtil.getURLofKeyword(keyword),
					keywordsAndOccurenceCounts.get(keyword)));// create keyword
																// url

		}
		return model;
	}
	
	

}
