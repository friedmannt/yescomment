package yescomment.managedbean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import yescomment.model.Article;
import yescomment.persistence.search.ArticleOrder;
import yescomment.persistence.search.ArticleSearchCriteria;
import yescomment.persistence.search.ArticleSearchCriteria.CommentCountRange;
import yescomment.persistence.search.ArticleSearcher;
import yescomment.util.ArticleLanguage;
import yescomment.util.JSFUtil;
import yescomment.util.ListPaginator;
import yescomment.util.LocalizationUtil;
import yescomment.util.Paginator;

@ManagedBean
@ViewScoped
public class ArticleDetailedSearchManagedBean implements Serializable {

	private static final int ARTICLE_PER_PAGE = 20;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	ArticleSearcher articleSearcher;

	private ArticleSearchCriteria articleSearchCriteria;

	public ArticleSearchCriteria getArticleSearchCriteria() {
		return articleSearchCriteria;
	}

	public void setArticleSearchCriteria(ArticleSearchCriteria articleSearchCriteria) {
		this.articleSearchCriteria = articleSearchCriteria;
	}

	private ArticleOrder articleOrder=ArticleOrder.ARTICLE_CREATE_DATE;

	

	public ArticleOrder getArticleOrder() {
		return articleOrder;
	}

	public void setArticleOrder(ArticleOrder articleOrder) {
		this.articleOrder = articleOrder;
	}

	@PostConstruct
	public void initializeArticleSearch() {
		articleSearchCriteria = new ArticleSearchCriteria();
		articleOrder = ArticleOrder.ARTICLE_CREATE_DATE;
	}

	/**
	 * contains only resultarticles on current page
	 */
	private List<Article> resultArticles;

	public List<Article> getResultArticles() {
		return resultArticles;
	}

	public void setResultArticles(List<Article> resultArticles) {
		this.resultArticles = resultArticles;
	}

	public void searchForArticles() {
		// replace empty string with null
		if (articleSearchCriteria.getTitle() != null && articleSearchCriteria.getTitle().isEmpty()) {
			articleSearchCriteria.setTitle(null);
		}
		if (articleSearchCriteria.getSite() != null && articleSearchCriteria.getSite().isEmpty()) {
			articleSearchCriteria.setSite(null);
		}
		if (articleSearchCriteria.getCommenter() != null && articleSearchCriteria.getCommenter().isEmpty()) {
			articleSearchCriteria.setCommenter(null);
		}

		Integer resultCount = articleSearcher.countOfSearchedArticles(articleSearchCriteria).intValue();
		paginator = this.new ArticleSearchResultPaginator(ARTICLE_PER_PAGE, resultCount);

	}


	public List<ArticleLanguage> possibleArticleLanguages() {
		return Arrays.asList(ArticleLanguage.values());
	}

	public List<CommentCountRange> possibleCommentCountRanges() {
		return Arrays.asList(CommentCountRange.values());
	}
	public List<ArticleOrder> possibleArticleOrders () {
		return Arrays.asList(ArticleOrder.values());
	}

	public String getLanguageTranslation(final ArticleLanguage articleLanguage) {
		Locale locale = JSFUtil.getLocale();
		return LocalizationUtil.getEnumTranslation(articleLanguage, locale);
	}

	public String getCommentCountRangeTranslation(final CommentCountRange commentCountRange) {
		Locale locale = JSFUtil.getLocale();
		return LocalizationUtil.getEnumTranslation(commentCountRange, locale);
	}
	
	public String getArticleOrderTranslation(final ArticleOrder articleOrder) {
		Locale locale = JSFUtil.getLocale();
		return LocalizationUtil.getEnumTranslation(articleOrder, locale);
	}


	/**
	 * Besides paging, reloads resultArticles, for the current page
	 * 
	 * @author Friedmann Tamás
	 * 
	 */
	public class ArticleSearchResultPaginator extends ListPaginator {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ArticleSearchResultPaginator(int pageSize, int itemCount) {
			super(pageSize, itemCount);

		}

		@Override
		public void displayedItemsChanged() {

			super.displayedItemsChanged();
			final int offset = getStartIndex();
			final int limit = getPageSize();
			resultArticles = articleSearcher.searchForArticles(articleSearchCriteria, articleOrder, false, offset, limit);
		}

	}

	private Paginator paginator;

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

}
