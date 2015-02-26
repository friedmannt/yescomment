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

	/**
	 * The new articles articleinfo descriptor
	 */
	ArticleInfo newArticleInfo;

	/**
	 * new article is importable and not yet imported
	 */
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

	/**
	 * The Future wrapper around articleinfo, helps to retrieve asynchronously
	 */
	private Future<ArticleInfo> articleInfoFutureResult;

	/**
	 * At page load, we get future articleinfo from flash
	 * 
	 * @throws InterruptedException
	 */
	public void getArticleInfoFutureResultFromFlash() throws InterruptedException {
		Locale locale = JSFUtil.getLocale();
		Object objectFromFlash = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("articleInfoFutureResult");
		if (objectFromFlash != null) {

			// might be null, if newarticlepage is loaded directly
			if (objectFromFlash instanceof Future<?>) {
				articleInfoFutureResult = (Future<ArticleInfo>) objectFromFlash;
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, LocalizationUtil.getTranslation("url_empty", locale), null));
		}

	}

	/**
	 * Shows, whether the Future<ArticleInfo> exists
	 * 
	 * @return
	 */
	public boolean articleInfoFutureResultExists() {
		return articleInfoFutureResult != null;
	}

	/**
	 * Tests whether article info retrieve is ready If not ready, returns false,
	 * if ready, executes the retrieve
	 * 
	 * @return
	 */
	public boolean checkArticleInfo() {

		if (articleInfoFutureResult.isDone()) {
			retrieveNewArticleInfo();
			return true;
		} else {
			return false;
		}

	}

	/**
	 * If it is already ready, we retrieve the articleinfo, and set
	 * newArticlePassedTheCheck
	 */
	private void retrieveNewArticleInfo() {
		assert articleInfoFutureResult.isDone();
		try {
			newArticleInfo = articleInfoFutureResult.get();
		} catch (ExecutionException e) {
			// e.printStackTrace();
			Throwable cause = e.getCause();
			newArticlePassedTheCheck = Boolean.FALSE;
			FacesContext.getCurrentInstance().addMessage("newarticleform:newarticlepassedthecheck", new FacesMessage(FacesMessage.SEVERITY_ERROR, cause.toString(), null));
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

	/**
	 * Creates the new article;
	 * 
	 * @return
	 */
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

}
