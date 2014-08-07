package yescomment.managedbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import yescomment.model.Article;
import yescomment.persistence.ArticleManager;
import yescomment.util.ListPaginator;

@ManagedBean
@ViewScoped
public class ArticleArchiveManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int ARCHIVE_ARTICLE_PAGE_SIZE = 50;

	@EJB
	ArticleManager articleManager;

	@Past
	@NotNull
	private Date archiveDate;

	public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	private List<Article> resultArticles;

	public List<Article> getResultArticles() {
		return resultArticles;
	}

	public void setResultArticles(List<Article> resultArticles) {
		this.resultArticles = resultArticles;
	}

	public void searchForArticles() {
		resultArticles = articleManager.getArticlesOnDate(archiveDate);

		articlePaginator = new ListPaginator(ARCHIVE_ARTICLE_PAGE_SIZE, resultArticles.size());
	}

	private ListPaginator articlePaginator;

	public ListPaginator getArticlePaginator() {
		return articlePaginator;
	}

	public void setArticlePaginator(ListPaginator articlePaginator) {
		this.articlePaginator = articlePaginator;
	}

	public boolean archiveArticlePaginatorShouldBeRendered() {

		return resultArticles != null && resultArticles.size() > ARCHIVE_ARTICLE_PAGE_SIZE;
	}

}
