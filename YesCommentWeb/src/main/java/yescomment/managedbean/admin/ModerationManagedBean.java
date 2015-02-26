package yescomment.managedbean.admin;

import java.io.Serializable;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.persistence.ArticleManager;
import yescomment.persistence.CommentManager;

@ManagedBean
@ViewScoped
@RolesAllowed("yescommentadmin")
public class ModerationManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	CommentManager commentManager;
	
	@EJB
	ArticleManager articleManager;

	private String deletedCommentId;// for delete
	
	private String deletedArticleId;// for delete

	private String moderatedCommentId;// for hide/unhide

	private String mergedArticleId;//article will be deleted and merged
	
	private String mergeTargetArticleId;//merge target
	
	private String validateCommentDataArticleId;//article will have valid comment count and last comment date

	public String getDeletedCommentId() {
		return deletedCommentId;
	}

	public String getDeletedArticleId() {
		return deletedArticleId;
	}

	public void setDeletedArticleId(String deletedArticleId) {
		this.deletedArticleId = deletedArticleId;
	}

	public void setDeletedCommentId(String deletedCommentId) {
		this.deletedCommentId = deletedCommentId;
	}

	public String getModeratedCommentId() {
		return moderatedCommentId;
	}

	public void setModeratedCommentId(String moderatedCommentId) {
		this.moderatedCommentId = moderatedCommentId;
	}

	public String getMergedArticleId() {
		return mergedArticleId;
	}

	public void setMergedArticleId(String mergedArticleId) {
		this.mergedArticleId = mergedArticleId;
	}

	public String getMergeTargetArticleId() {
		return mergeTargetArticleId;
	}

	public void setMergeTargetArticleId(String mergeTargetArticleId) {
		this.mergeTargetArticleId = mergeTargetArticleId;
	}

	
	
	
	public String getValidateCommentDataArticleId() {
		return validateCommentDataArticleId;
	}

	public void setValidateCommentDataArticleId(String validateCommentDataArticleId) {
		this.validateCommentDataArticleId = validateCommentDataArticleId;
	}

	public void deleteComment() {
		commentManager.removeComment(deletedCommentId);
		deletedCommentId=null;

	}
	public void deleteArticle() {
		articleManager.remove(deletedArticleId);
		deletedArticleId=null;

	}

	
	public void hideComment() {
		commentManager.hideComment(moderatedCommentId);
		moderatedCommentId=null;
	}

	public void unhideComment() {
		commentManager.unhideComment(moderatedCommentId);
		moderatedCommentId=null;
	}

	
	public void mergeArticle() {
		articleManager.mergeArticlesComments(mergedArticleId,mergeTargetArticleId);
		mergedArticleId=null;
		mergeTargetArticleId=null;
	}
	
	public void validateCommentData() {
		articleManager.validateArticleCommentData(validateCommentDataArticleId) ;
		validateCommentDataArticleId=null;
	}
	
}
