package yescomment.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.ArticleManager;
import yescomment.persistence.CommentManager;

@ManagedBean
@ViewScoped
public class ViewArticleManagedBean implements Serializable {

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

	private Long articleId;

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	private Long highlightCommentId;

	public Long getHighlightCommentId() {
		return highlightCommentId;
	}

	public void setHighlightCommentId(Long highlightCommentId) {
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
				if (reverseArticleOrder) {
					firstPage();	
				}
				else {
					lastPage();
				}

			}
		}
		

	}

	public void postNewComment() {
		if (userSessionBean.getLoginUserName() != null) {

			article = commentManager.addCommentToArticle(article, newCommentText, userSessionBean.getLoginUserName());
			newCommentText = null;
			if (reverseArticleOrder) {
				firstPage();	
			}
			else {
				lastPage();
			}
			
		}
	}

	private boolean reverseArticleOrder = true;// initialize with reverse

	public boolean isReverseArticleOrder() {
		return reverseArticleOrder;
	}

	public void setReverseArticleOrder(boolean reverseArticleOrder) {
		this.reverseArticleOrder = reverseArticleOrder;
	}

	public List<Comment> getCommentsOfArticle() {
		if (article == null) {
			return Collections.emptyList();
		} else {
			List<Comment> comments = new ArrayList<Comment>(article.getComments());
			if (reverseArticleOrder) {
				Collections.reverse(comments);
			}
			return comments;
		}
	}

	public int getOrderNumberOfComment(final Comment comment) {
		return article.getComments().indexOf(comment) + 1;
	}

	

	public void upvoteComment(Comment comment) {
		comment.setPlusCount(comment.getPlusCount() + 1);
		userSessionBean.getVotedCommentIds().add(comment.getId());
		commentManager.merge(comment);
	}

	public void downvoteComment(Comment comment) {
		comment.setMinusCount(comment.getMinusCount() + 1);
		userSessionBean.getVotedCommentIds().add(comment.getId());
		commentManager.merge(comment);
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

	public void firstPage() {
		commentStartIndex = 0;
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;
	}

	public void prevPage() {
		commentStartIndex = Math.max(0, commentStartIndex - COMMENT_PAGE_SIZE);
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;
	}

	public void nextPage() {
		commentStartIndex = Math.min((article.getCommentCount() - 1) / COMMENT_PAGE_SIZE * COMMENT_PAGE_SIZE, commentStartIndex + COMMENT_PAGE_SIZE);
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;

	}

	public void lastPage() {
		commentStartIndex = (article.getCommentCount() - 1) / COMMENT_PAGE_SIZE * COMMENT_PAGE_SIZE;
		commentEndIndex = commentStartIndex + COMMENT_PAGE_SIZE - 1;
	}

	
}
