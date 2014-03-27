package yescomment.managedbean.keyword;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		for (String keyword : keywordsAndOccurenceCounts.keySet()) {
				Integer occurenceCount=keywordsAndOccurenceCounts.get(keyword);
				sumOccurenceCount=sumOccurenceCount+occurenceCount;


		}

		for (String keyword : keywordsAndOccurenceCounts.keySet()) {
			Integer occurenceCount=keywordsAndOccurenceCounts.get(keyword);
			String urlOfTagCloudItem=getURLofKeyword(contextRoot,keyword);// create keyword url
			float ratio=(float) occurenceCount/(float)sumOccurenceCount;
			int strength = 0;
			if (ratio>0.1) {
				strength=5;
			}
			else if (ratio>0.05) {
				strength=4;
			}
			else if (ratio>0.02) {
				strength=3;
			}
			else if (ratio>0.01) {
				strength=2;
			}
			else {
				strength=1;
			}
			System.out.println("keyword="+keyword+" ratio="+ratio);
			tagCloudItems.add(new TagCloudItem(keyword,urlOfTagCloudItem,
					strength));

		}
		
		return tagCloudItems;
	}
	
	
	public static final String getURLofKeyword(@NotNull final String contextRoot,@NotNull final String keyword) throws UnsupportedEncodingException {
		return String.format(
				"%s/faces/keywords.xhtml?keyword=%s#resultarticlesforkeyword",contextRoot,
				URLEncoder.encode(keyword, "UTF-8"));
	}

}
