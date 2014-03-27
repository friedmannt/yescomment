package yescomment.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.ArticleManager;
import yescomment.persistence.CommentManager;
import yescomment.util.Paginator;
import yescomment.util.VoteDirection;

@ManagedBean
@ViewScoped
public class ViewArticleManagedBean implements Serializable, Paginator {

	public static enum CommentSortOrder {
		OLDESTFIRST, NEWESTFIRST;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{userSessionBean}")
	private UserSessionBean userSessionBean;

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

	@EJB
	ArticleManager articleManager;

	@EJB
	CommentManager commentManager;

	private String articleId;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	private String highlightCommentId;

	public String getHighlightCommentId() {
		return highlightCommentId;
	}

	public void setHighlightCommentId(String highlightCommentId) {
		this.highlightCommentId = highlightCommentId;
	}

	private Article article;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	private String newCommentText;

	public String getNewCommentText() {
		return newCommentText;
	}

	public void setNewCommentText(String newCommentText) {
		this.newCommentText = newCommentText;
	}

	public void loadArticle() {

		if (articleId == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No id param given", "Please specify an article id"));
		} else {
			Article article = articleManager.find(articleId);
			if (article == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Article not found", "Article not found"));
			} else {
				this.article = article;
				// sorting the comments ascending
				Collections.sort(article.getComments(), new Comparator<Comment>() {

					@Override
					public int compare(Comment o1, Comment o2) {

						return o1.getCommentDate().compareTo(o2.getCommentDate());
					}
				});
				if (highlightCommentId == null) {
					// jump to first page
					firstPage();
				} else {

					// jump to the page of the highlighted comment
					pageToComment(highlightCommentId);
				}

			}
		}

	}

	public void postNewComment() {
		String userName = userSessionBean.getUserName();
		String author = userName != null && userName.length() > 0 ? userName : "Anonymus";
		article = commentManager.addCommentToArticle(article, newCommentText, author);
		newCommentText = null;
		highlightCommentId = null;
		// the newly added comment should be visible, based on ordering, it is
		// on the first page or last page
		// on first page, if comment order is reversed, last page, if comment
		// order is not reserver
		if (commentSortOrder == CommentSortOrder.NEWESTFIRST) {
			firstPage();
		} else {
			lastPage();
		}

	}

	private CommentSortOrder commentSortOrder = CommentSortOrder.NEWESTFIRST;// initialize
																				// with
																				// newest
																				// first

	public CommentSortOrder getCommentSortOrder() {
		return commentSortOrder;
	}

	public void setCommentSortOrder(CommentSortOrder commentSortOrder) {
		this.commentSortOrder = commentSortOrder;
	}

	public List<Comment> getCommentsOfArticle() {
		if (article == null) {
			return Collections.emptyList();
		} else {
			List<Comment> comments = new ArrayList<Comment>(article.getComments());
			if (commentSortOrder == CommentSortOrder.NEWESTFIRST) {
				Collections.reverse(comments);
			}
			return comments;
		}
	}

	public int getOrderNumberOfComment(final Comment comment) {
		return article.getComments().indexOf(comment) + 1;
	}

	
	
	
	public void voteOnComment(@NotNull  Comment comment,@NotNull final VoteDirection voteDirection) {
		userSessionBean.getVotedCommentIds().add(comment.getId());
		Comment votedComment=commentManager.voteOnComment(comment, voteDirection);
		comment.setPlusCount(votedComment.getPlusCount());
		comment.setMinusCount(votedComment.getMinusCount());
	}



	public boolean alreadyVotedOnComment(Comment comment) {
		return userSessionBean.getVotedCommentIds().contains(comment.getId());
	}

	public boolean commentShouldBeHighlighted(Comment comment) {
		return highlightCommentId == null ? false : highlightCommentId.equals(comment.getId());
	}

	public static String getMaxCommentSize() {
		return Integer.valueOf(Comment.MAX_COMMENT_SIZE).toString();
	}

	public static final Integer COMMENT_PAGE_SIZE = 10;

	private int commentStartIndex;// zero based, inclusive

	private int commentEndIndex;// zero based, inclusive

	public int getCommentStartIndex() {
		return commentStartIndex;
	}

	public int getCommentEndIndex() {
		return commentEndIndex;
	}

	@Override
	public void firstPage() {
		commentStartIndex = 0;
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;
	}

	@Override
	public void prevPage() {
		commentStartIndex = Math.max(0, commentStartIndex - COMMENT_PAGE_SIZE);
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;
	}

	@Override
	public void nextPage() {
		commentStartIndex = Math.min((article.getCommentCount() - 1) / COMMENT_PAGE_SIZE * COMMENT_PAGE_SIZE, commentStartIndex + COMMENT_PAGE_SIZE);
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;

	}

	@Override
	public void lastPage() {
		commentStartIndex = (article.getCommentCount() - 1) / COMMENT_PAGE_SIZE * COMMENT_PAGE_SIZE;
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;
	}

	@Override
	public int getCurrentPage() {
		return (commentStartIndex) / COMMENT_PAGE_SIZE + 1;
	}

	@Override
	public int getTotalPage() {

		return (article.getCommentCount() - 1) / COMMENT_PAGE_SIZE + 1;
	}

	// setting page start and end indices to show highlighted comment
	private void pageToComment(@NotNull final String highlightCommentId) {
		List<Comment> comments = getCommentsOfArticle();
		Integer indexOfHighlightedComment = null;
		for (int i = 0; i < comments.size(); i++) {
			Comment comment = comments.get(i);
			if (comment.getId().equals(highlightCommentId)) {
				indexOfHighlightedComment = i;
				break;
			}
		}
		if (indexOfHighlightedComment != null) {
			commentStartIndex = indexOfHighlightedComment / COMMENT_PAGE_SIZE * COMMENT_PAGE_SIZE;
			commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;

		}

	}

}
