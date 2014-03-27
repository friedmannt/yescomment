package yescomment.util;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Utility class for paging a list
 * 
 * @author Friedmann Tamás
 * 
 * @param <T>
 */
public class ListPaginator<T> implements Serializable,Paginator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<T> model;

	private int currentPage;

	private int pageSize;

	public ListPaginator(@NotNull List<T> model,@Min(1) int pageSize) {

		super();
		if (model == null || pageSize < 1) {
			throw new IllegalArgumentException();
		}

		this.model = model;
		this.pageSize = pageSize;
		this.currentPage = 1;
	}

	

	public List<T> getCurrentViewOfModel() {
		int startingIndex = pageSize * (currentPage - 1);
		if (startingIndex < 0) {
			startingIndex = 0;
		}
		int endingIndex = startingIndex + pageSize;
		if (endingIndex > model.size()) {
			endingIndex = model.size();
		}
		return model.subList(startingIndex, endingIndex);
	}

	@Override
	public void firstPage() {
		currentPage = 1;
	}

	@Override
	public void prevPage() {
		if (currentPage > 1) {
			currentPage--;
		}
	}

	@Override
	public void nextPage() {
		if (currentPage < getTotalPage()) {
			currentPage++;
		}
	}
	
	@Override
	public void lastPage() {
		currentPage = getTotalPage();
	}


	@Override
	public int getTotalPage() {
		if (model.size() % pageSize == 0) {
			return model.size() / pageSize;
		} else {
			return (model.size() / pageSize) + 1;
		}
	}

	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage<1) {
			throw new IllegalArgumentException();
		}
		this.currentPage = currentPage;
		maximizeCurrentPageToTotalPage();
	}

	
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize<1) {
			throw new IllegalArgumentException();
		}
		this.pageSize = pageSize;
		maximizeCurrentPageToTotalPage();
	}



	public List<T> getModel() {
		return model;
	}


	

	public void setModel(List<T> model) {
		if (model == null) {
			throw new IllegalArgumentException();
		}
		this.model = model;
		maximizeCurrentPageToTotalPage();
	}
	
	
	private void maximizeCurrentPageToTotalPage() {
		currentPage = Math.min(currentPage, getTotalPage());
	}
	
	

}
