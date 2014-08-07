package yescomment.rss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.ArticleManager;
import yescomment.recommended.RecommendationAscpect;
import yescomment.recommended.RecommendedArticlesRetriever;
import yescomment.util.URLUtil;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * Creates RSS feed, can be multiple articles, or multiple comments of one
 * article
 * 
 * @author Friedmann Tamás
 * 
 */
@Stateless
public class RSSFeedCreator implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	protected RecommendedArticlesRetriever recommendedArticlesRetriever;
	@EJB
	protected ArticleManager articleManager;

	private static final class CommentComparator implements Comparator<Comment>,Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Comment o1, Comment o2) {

			return o2.getCreateDate().compareTo(o1.getCreateDate());

		}
	}
	
	/**
	 * Returns rss of the articles based on a recommentdation aspect Server data
	 * is needed because feed items contain links
	 * 
	 * @param recommendationAscpect
	 * @param serverName
	 * @param serverPort
	 * @param contextPath
	 * @return
	 */
	public SyndFeed getRSSFeedOfRecommendationAspect(final @NotNull RecommendationAscpect recommendationAscpect,final String rssUrlBase) {
		List<Article> articles = recommendedArticlesRetriever.retrieveRecommendedArticles(recommendationAscpect);
		final SyndFeed feed = new SyndFeedImpl();
		feed.setEncoding("UTF-8");
		feed.setFeedType("rss_2.0");

		feed.setTitle("Yescomment RSS of " + recommendationAscpect.toString().toLowerCase() + " articles");
		feed.setLink("www.yescomment.net");
		feed.setDescription("Yescomment is a free commenting site. Comments can be made on any aricle on the web");
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		for (Article article : articles) {
			SyndEntry entry = new SyndEntryImpl();
			entry.setTitle(article.getTitle());
			entry.setLink(String.format("%s/faces/viewarticle.xhtml?articleId=%s",rssUrlBase, article.getId()));

			entry.setAuthor(URLUtil.getSiteOfURL(article.getUrl()));
			entry.setPublishedDate(article.getCreateDate());
			SyndContent description = new SyndContentImpl();
			description.setType("text/html");
			StringBuilder itemDescriptionStringBuilder = new StringBuilder();

			itemDescriptionStringBuilder.append("<p>").append(article.getDescription()).append("</p>");
			itemDescriptionStringBuilder.append("<p> Keywords:").append(article.getKeywords()).append("</p>");

			description.setValue(itemDescriptionStringBuilder.toString());
			entry.setDescription(description);
			entries.add(entry);
		}

		feed.setEntries(entries);
		return feed;
	}

	/**
	 * Returns rss of the comments of one article Server data is needed because
	 * feed items contain links
	 * 
	 * @param articleId
	 * @param serverName
	 * @param serverPort
	 * @param contextPath
	 * @return
	 */
	public SyndFeed getRSSFeedOfArticlesComments(final @NotNull String articleId,final String rssUrlBase) {
		Article article = articleManager.find(articleId);
		final SyndFeed feed = new SyndFeedImpl();
		feed.setEncoding("UTF-8");
		feed.setFeedType("rss_2.0");

		feed.setTitle("Yescomment RSS of the comments of article " + article.getTitle() + "(" +URLUtil.getSiteOfURL(article.getUrl()) + ")");
		feed.setLink("www.yescomment.net");
		feed.setDescription("Yescomment is a free commenting site. Comments can be made on any aricle on the web");
		List<Comment> commentsByDateDescending = new ArrayList<Comment>(article.getComments());
		Collections.sort(commentsByDateDescending, new CommentComparator());
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		for (Comment comment : commentsByDateDescending) {
			int index=commentsByDateDescending.indexOf(comment);
			SyndEntry entry = new SyndEntryImpl();
			entry.setTitle("Comment #"+(commentsByDateDescending.size()-index));
			entry.setLink(String.format("%s/faces/viewarticle.xhtml?articleId=%s&highlightCommentId=%s#comments",rssUrlBase, article.getId(),comment.getId()));

			entry.setAuthor(comment.getAuthor());
			entry.setPublishedDate(comment.getCreateDate());
			SyndContent description = new SyndContentImpl();
			description.setType("text/html");
			StringBuilder itemDescriptionStringBuilder = new StringBuilder();

			itemDescriptionStringBuilder.append("<p>").append(comment.getHidden()?"*******":comment.getCommentText()).append("</p>");
			

			description.setValue(itemDescriptionStringBuilder.toString());
			entry.setDescription(description);
			entries.add(entry);
		}

		feed.setEntries(entries);
		return feed;
	}

}
