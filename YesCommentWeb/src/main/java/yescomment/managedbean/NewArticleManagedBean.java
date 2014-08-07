package yescomment.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import yescomment.articleretriever.AsynchArticleInfoRetriever;
import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ArticleInfo;
import yescomment.util.JSFUtil;
import yescomment.util.LocalizationUtil;

@ManagedBean
@ViewScoped
public class NewArticleManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SECONDS_BETWEEN_ARTICLE_IMPORTS = 60;

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
	AsynchArticleInfoRetriever articleInfoRetriever;

	ArticleInfo newArticleInfo;

	Boolean newArticlePassedTheCheck;

	public ArticleInfo getNewArticleInfo() {
		return newArticleInfo;
	}

	public void setNewArticleInfo(ArticleInfo newArticleInfo) {
		this.newArticleInfo = newArticleInfo;
	}

	public Boolean getNewArticlePassedTheCheck() {
		return newArticlePassedTheCheck;
	}

	public void setNewArticlePassedTheCheck(Boolean newArticlePassedTheCheck) {
		this.newArticlePassedTheCheck = newArticlePassedTheCheck;
	}

	private Future<ArticleInfo> articleInfoFutureResult;

	public void finishRetrieveNewArticleInfo() {

		try {
			newArticleInfo = articleInfoFutureResult.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			newArticlePassedTheCheck = Boolean.FALSE;
			FacesContext.getCurrentInstance().addMessage("newarticleform:newarticlepassedthecheck", new FacesMessage(FacesMessage.SEVERITY_ERROR, cause.getClass().getName() + ":" + cause.getMessage(), null));
			return;

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		// we should check, whether final article url is unique
		Article articleWithSameURL = articleManager.getArticleByURL(newArticleInfo.getFinalURL());
		if (articleWithSameURL == null) {
			newArticlePassedTheCheck = Boolean.TRUE;
		} else {
			newArticlePassedTheCheck = Boolean.FALSE;
			FacesContext.getCurrentInstance().addMessage("newarticleform:newarticlepassedthecheck", new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("article_already_exists", JSFUtil.getLocale()) + newArticleInfo.getFinalURL(), null));

		}

	}

	public String createNewArticle() {
		assert newArticlePassedTheCheck;
		Locale locale = JSFUtil.getLocale();
		int secondsToWait = JSFUtil.secondsToWaitUntilOperationAllowed(userSessionBean.getLastArticleImportDate(), SECONDS_BETWEEN_ARTICLE_IMPORTS);
		if (secondsToWait != 0) {

			FacesContext.getCurrentInstance().addMessage("newarticleform:newarticleimportbutton", new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format(LocalizationUtil.getTranslation("wait_n_seconds_next_article", locale), secondsToWait), null));
			return null;
		} else {

			userSessionBean.setLastArticleImportDate(new Date());
			Article article = articleManager.createArticleFromArticleInfo(newArticleInfo);

			article = articleManager.create(article);
			// redirect to new article
			String newArticleId = article.getId();
			return "/viewarticle.xhtml?faces-redirect=true&articleId=" + newArticleId;
		}

	}

	/**
	 * At page load, we get future articleinfo from flash, and get the result
	 * 
	 * @throws InterruptedException
	 */
	public void getArticleInfoFutureResultFromFlash() throws InterruptedException {
		Object objectFromFlash = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("articleInfoFutureResult");
		if (objectFromFlash != null) {
			// might be null, if newarticlepage is loaded directly
			if (objectFromFlash instanceof Future<?>) {
				articleInfoFutureResult = (Future<ArticleInfo>) objectFromFlash;
				finishRetrieveNewArticleInfo();
			}

		}
	}

}
