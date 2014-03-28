package yescomment.managedbean.keyword;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;

import yescomment.keyword.AllKeywordsSingleton;

@ApplicationScoped
@ManagedBean
public class KeywordTagCloudBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int KEYWORDS_TAGCLOUD_SIZE=250;
	@EJB
	AllKeywordsSingleton allKeywordsSingleton;
	
	public List<TagCloudItem> getTopKeywordsTagCloudItems()
			throws UnsupportedEncodingException {
		String contextRoot=FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		List<TagCloudItem> tagCloudItems = new ArrayList<TagCloudItem>();
		Map<String, Integer> keywordsAndOccurenceCounts = allKeywordsSingleton
				.retrieveTopKeywords(KEYWORDS_TAGCLOUD_SIZE);
		int sumOccurenceCount=0;//all occurences for all keywords, for relative strength;
		double avgOccurenceCount=0d;
		for (String keyword : keywordsAndOccurenceCounts.keySet()) {
				Integer occurenceCount=keywordsAndOccurenceCounts.get(keyword);
				sumOccurenceCount=sumOccurenceCount+occurenceCount;
				

		}
		avgOccurenceCount=(double)sumOccurenceCount/keywordsAndOccurenceCounts.keySet().size();
		
		for (String keyword : keywordsAndOccurenceCounts.keySet()) {
			Integer occurenceCount=keywordsAndOccurenceCounts.get(keyword);
			String urlOfTagCloudItem=getURLofKeyword(contextRoot,keyword);// create keyword url
			double ratio=occurenceCount/avgOccurenceCount;
		
			//flattening, sqrt twice
			ratio= Math.sqrt(Math.sqrt(ratio));
			//rounding for two decimal places
			ratio=Math.round(ratio*100d)/100d;
			
			tagCloudItems.add(new TagCloudItem(keyword,urlOfTagCloudItem,
					ratio));

		}
		
		return tagCloudItems;
	}
	
	
	public static final String getURLofKeyword(@NotNull final String contextRoot,@NotNull final String keyword) throws UnsupportedEncodingException {
		return String.format(
				"%s/faces/keywords.xhtml?keyword=%s#resultarticlesforkeyword",contextRoot,
				URLEncoder.encode(keyword, "UTF-8"));
	}
	
	
	
	
}
