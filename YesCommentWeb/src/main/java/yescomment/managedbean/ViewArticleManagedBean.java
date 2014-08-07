package yescomment.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import yescomment.model.Article;
import yescomment.model.Comment;
import yescomment.persistence.ArticleManager;
import yescomment.persistence.CommentManager;
import yescomment.persistence.VoteManager;
import yescomment.push.CommentNotificationEndpoint;
import yescomment.util.AnonymusUserName;
import yescomment.util.JSFUtil;
import yescomment.util.ListPaginator;
import yescomment.util.LocalizationUtil;
import yescomment.util.Paginator;
import yescomment.util.VoteDirection;

@ManagedBean
@ViewScoped
public class ViewArticleManagedBean implements Serializable {

	public static enum CommentSortOrder {
		OLDESTFIRST, NEWESTFIRST;
	}
	
	private static final int COMMENT_PAGE_SIZE=20;
	
	private static final int SECONDS_BETWEEN_COMMENTS=30;
	

	

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
	
	@EJB
	VoteManager voteManager;

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
	
	public List<Comment> comments;

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	private String newCommentText;

	public String getNewCommentText() {
		return newCommentText;
	}

	public void setNewCommentText(String newCommentText) {
		this.newCommentText = newCommentText;
	}
	
	public Comment newCommentReplyOf;
	
	
	
	public Comment getNewCommentReplyOf() {
		return newCommentReplyOf;
	}

	public void setNewCommentReplyOf(Comment newCommentReplyOf) {
		this.newCommentReplyOf = newCommentReplyOf;
	}

	private Paginator commentPaginator;
	
	
	
	public Paginator getCommentPaginator() {
		return commentPaginator;
	}

	public void setCommentPaginator(Paginator commentPaginator) {
		this.commentPaginator = commentPaginator;

	}
	

	private void sortComments(@NotNull final CommentSortOrder commentSortOrder) {
		// sorting the comments based on commentSortOrder
		Collections.sort(comments, new Comparator<Comment>() {

			@Override
			public int compare(Comment o1, Comment o2) {
				switch (commentSortOrder) {
				case OLDESTFIRST:
					return o1.getCreateDate().compareTo(o2.getCreateDate());
				case NEWESTFIRST:
					return o2.getCreateDate().compareTo(o1.getCreateDate());
				default:
					return 0;
					
				}
				
			}
		});
	}  

	public void loadArticle() {
		Locale locale=JSFUtil.getLocale();
		if (articleId == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("no_id_param_err_sum", locale), LocalizationUtil.getTranslation("no_id_param_err_det", locale)));
		} else {
			Article article = articleManager.find(articleId);
			if (article == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("article_id_not_found_err_sum", locale),  LocalizationUtil.getTranslation("article_id_not_found_err_det", locale)));
			} else {
				this.article = article;
				this.comments=new ArrayList<>(article.getComments());
				commentPaginator=new ListPaginator(COMMENT_PAGE_SIZE, comments.size());
				sortComments(commentSortOrder);
				if (highlightCommentId == null) {
					// jump to first page
					commentPaginator.firstPage();
				} else {

					// jump to the page of the highlighted comment
					pageToComment(highlightCommentId);
				}

			}
		}

	}
	
	

	public void postNewComment() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		assert(userSessionBean.getCaptchaWrapper().isCaptchaCorrectlyAnswered()||request.getRemoteUser()!=null);
		Locale locale=JSFUtil.getLocale();
		int secondsToWait = JSFUtil.secondsToWaitUntilOperationAllowed(userSessionBean.getLastCommentDate(),SECONDS_BETWEEN_COMMENTS ) ;
		if (secondsToWait!=0) {
			
			FacesContext.getCurrentInstance().addMessage("newcommentform:postcommentbutton", new FacesMessage(FacesMessage.SEVERITY_ERROR,  String.format(LocalizationUtil.getTranslation("wait_n_seconds_next_comment", locale),secondsToWait), null));
		}
		else {
			userSessionBean.setLastCommentDate(new Date());
			String userName = JSFUtil.getUserName();
			String author = userName != null ? userName : AnonymusUserName.ANONYMUS_USER_NAME;
			String newCommentReplyOfId=newCommentReplyOf==null?null:newCommentReplyOf.getId();
			article = commentManager.addCommentToArticle(article, newCommentText, author,newCommentReplyOfId);
			//telling the websocket endpont to notify the clients
			CommentNotificationEndpoint.commentNotify(articleId);
			refreshComments();
			newCommentText = null;
			newCommentReplyOf=null;
			highlightCommentId = null;

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

	public void changeCommentSortOrderToNewestFirst() {
		if (commentSortOrder==CommentSortOrder.OLDESTFIRST) {
			commentSortOrder=CommentSortOrder.NEWESTFIRST;
			sortComments(commentSortOrder);
		}
	}
	
	public void changeCommentSortOrderToOldestFirst() {
		if (commentSortOrder==CommentSortOrder.NEWESTFIRST) {
			commentSortOrder=CommentSortOrder.OLDESTFIRST;
			sortComments(commentSortOrder);
		}
	}
	
	
	public int getOrderNumberOfComment(final Comment comment) {
		switch (commentSortOrder) {
		case NEWESTFIRST:
			return comments.size()-comments.indexOf(comment);
			
		case OLDESTFIRST:
			return comments.indexOf(comment) + 1;

		default:
			return 0;
		}
		
	}

	
	
	
	public void voteOnComment(@NotNull  Comment comment,@NotNull final VoteDirection voteDirection) {
		assert !comment.getHidden();
		Comment votedComment=voteManager.voteOnComment(comment.getId(), JSFUtil.getUserName(),voteDirection);
		comment.setUpVoteCount(votedComment.getUpVoteCount());
		comment.setDownVoteCount(votedComment.getDownVoteCount());
		
	}




	public boolean commentShouldBeHighlighted(Comment comment) {
		return highlightCommentId == null ? false : highlightCommentId.equals(comment.getId());
	}

	public static String getMaxCommentSize() {
		return Integer.toString(Comment.MAX_COMMENT_SIZE);
	}
	
	public String getPostingName() {
		return JSFUtil.getUserName()!=null?JSFUtil.getUserName():AnonymusUserName.ANONYMUS_USER_NAME;
	}	
	public void answerCaptcha() {
		

	}
	
	
	

	// setting page start and end indices to show highlighted comment
	private void pageToComment(@NotNull final String highlightCommentId) {
		
		Integer indexOfHighlightedComment = null;
		for (int i = 0; i < comments.size(); i++) {
			Comment comment = comments.get(i);
			if (comment.getId().equals(highlightCommentId)) {
				indexOfHighlightedComment = i;
				break;
			}
		}
		if (indexOfHighlightedComment != null) {
			commentPaginator.jumpToItem(indexOfHighlightedComment);

		}

	}

	public void reply (Comment comment) {
		newCommentReplyOf=comment;
	}
	public void undoReply() {
		newCommentReplyOf=null;
	}
	
	
	public void reloadArticleAndComments() {
		article=articleManager.find(articleId);
		refreshComments();
	}
	/**
	 * Refreshes comments of the article, which should be up-to-date before this method call
	 */
	private void refreshComments() {
		comments=new ArrayList<>(article.getComments());
		commentPaginator=new ListPaginator(COMMENT_PAGE_SIZE, comments.size());
		sortComments(commentSortOrder);
		// the latest comment should be visible, based on ordering, it is
		// on the first page or last page
		// on first page, if comment order is reversed, last page, if comment
		// order is not reserver
		if (commentSortOrder == CommentSortOrder.NEWESTFIRST) {
			commentPaginator.firstPage();
		} else {
			commentPaginator.lastPage();
		}
		
	}
	
	
	public String getEmbedCodeForArticlesComments() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return String.format("<iframe src=\"%s://%s:%s%s/faces/embeddable.xhtml?articleId=%s frameborder=\"0\"></iframe>",request.getScheme(),request.getServerName(),request.getServerPort(),request.getContextPath(),articleId);
	}
}
