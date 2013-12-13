package yescomment.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.ArticleManager;

@ManagedBean
@ViewScoped
public class ViewArticleManagedBean implements Serializable {



	@EJB
	ArticleManager articleManager;
	
	private Long idParam;

	public Long getIdParam() {
		return idParam;
	}

	public void setIdParam(Long idParam) {
		this.idParam = idParam;
	}

	private Article article;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public void loadArticleFromIdParam() {
		
		if (idParam == null) {
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"No id param given",
									"Please specify an article id"));
		} else {
			Article article = articleManager.find(idParam);
			if (article == null) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Article not found", "Article not found"));
			} else {
				this.article = article;

			}
		}
		String loginUserName = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("login_username");
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
		
		Comment comment = new Comment();
		comment.setCommentText(newCommentText);
		comment.setAuthor(newCommentAuthor);
		comment.setCommentDate(new Date());
		comment.setArticle(article);
		article.getComments().add(comment);
		article=articleManager.save(article);
		newCommentText = null;
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

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.put("viewed_article_before_login", this.getArticle().getId());
		return "login.xhtml?faces-redirect=true";
	}

}
