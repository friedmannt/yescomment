package yescomment.crawler.nocommentdetector;

import java.util.ServiceLoader;

import yescomment.crawler.CrawlerConfig;
/**
 * returns the nocommentdetector for the given crawlerconfig. 
 * If null is returned, there is no nocommentdetector, which means all articles are not commentable
 * @author Friedmann Tamás
 *
 */
public class NoCommentDetectorService {
	private static ServiceLoader<NoCommentDetector> serviceLoader = ServiceLoader.load(NoCommentDetector.class);

	public static NoCommentDetector getNoCommentDetector(CrawlerConfig crawlerConfig) {
		return getNoCommentDetector(crawlerConfig.getName());
	}

	public static NoCommentDetector getNoCommentDetector(String crawlerConfigName) {
		synchronized (serviceLoader) {
			for (NoCommentDetector ncd : serviceLoader) {
				if (ncd.getAcceptedCrawlerConfigName().equals(crawlerConfigName)) {
					return ncd;
				}

			}
		}
		return null;
	}
}
