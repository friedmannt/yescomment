package yescomment.crawler.nocommentdetector;

import java.util.ServiceLoader;

import javax.validation.constraints.NotNull;

import yescomment.crawler.CrawlerConfig;
/**
 * returns the nocommentdetector for the given crawlerconfig. 
 * If null is returned, there is no nocommentdetector, which means all articles are not commentable
 * @author Friedmann Tamás
 *
 */
public class NoCommentDetectorService {
	private static ServiceLoader<NoCommentDetector> serviceLoader = ServiceLoader.load(NoCommentDetector.class);

	public static NoCommentDetector getNoCommentDetector(@NotNull CrawlerConfig crawlerConfig) {
		return getNoCommentDetector(crawlerConfig.getName());
	}

	public static NoCommentDetector getNoCommentDetector(@NotNull String crawlerConfigName) {
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
