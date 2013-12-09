package yescomment.managedbean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

import yescomment.util.AllKeywordsSingletonLocal;
import yescomment.util.KeywordUtil;

@ApplicationScoped
@ManagedBean
public class KeywordTagCloudBean implements Serializable {

	@EJB
	AllKeywordsSingletonLocal allKeywordsSingleton;
	
	public TagCloudModel getKeywordTagCloudModel()
			throws UnsupportedEncodingException {

		DefaultTagCloudModel model = new DefaultTagCloudModel();
		Map<String, Integer> keywordsAndOccurenceCounts = allKeywordsSingleton
				.retrieveKeywords();
		for (String keyword : keywordsAndOccurenceCounts.keySet()) {

			model.addTag(new DefaultTagCloudItem(keyword, KeywordUtil.getURLofKeyword(keyword),
					keywordsAndOccurenceCounts.get(keyword)));// create keyword
																// url

		}
		return model;
	}
	
	

}
