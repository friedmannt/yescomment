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

	public void loadArticleFromIdParam() {
		
		if (articleId == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No id param given", "Please specify an article id"));
		} else {
			Article article = articleManager.find(articleId);
			if (article == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Article not found", "Article not found"));
			} else {
				this.article = article;

			}
		}
		String loginUserName = userSessionBean.getLoginUserName();
		if (loginUserName != null) {
			newCommentAuthor = loginUserName;
		}

	}

	private String newCommentText;

	public String getNewCommentText() {
		return newCommentText;
	}

	public void setNewCommentText(String newCommentText) {
		this.newCommentText = newCommentText;
	}

	private String newCommentAuthor;

	public String getNewCommentAuthor() {
		return newCommentAuthor;
	}

	public void setNewCommentAuthor(String newCommentAuthor) {
		this.newCommentAuthor = newCommentAuthor;
	}

	public void postNewComment() {
		if (userSessionBean.getLoginUserName() != null) {

			article = commentManager.addCommentToArticle(article, newCommentText, newCommentAuthor);
			newCommentText = null;
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
		List<Comment> comments = new ArrayList<Comment>(article.getComments());
		if (reverseArticleOrder) {
			Collections.reverse(comments);
		}
		return comments;
	}

	public int getOrderNumberOfComment(final Comment comment) {
		return article.getComments().indexOf(comment) + 1;
	}

	public String loginAction() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("viewed_article_before_login", this.getArticle().getId());
		return "login.xhtml?faces-redirect=true";
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

	private static final Integer MAX_COMMENT_SIZE = Comment.MAX_COMMENT_SIZE;

	public static Integer getMaxCommentSize() {
		return MAX_COMMENT_SIZE;
	}

}
