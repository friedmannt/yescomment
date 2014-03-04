package yescomment.crawler.nocommentdetector;

import java.io.Serializable;

import net.htmlparser.jericho.Source;
import yescomment.util.ArticleCommentPermission;

/**
 * Interface for nocomment detection
 * @author Friedmann Tamás
 *
 */
public interface NoCommentDetector extends Serializable{

	/**
	 * The name of the crawlerconfig, for which this nocommentdetector exists
	 * @return
	 */
	String getAcceptedCrawlerConfigName();
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	ArticleCommentPermission getArticleCommentPermission(Source source);
	
}
