package yescomment.util;

import java.io.Serializable;

import javax.validation.constraints.Max;

/**
 * Utility class for paging a list. Index is 0 based
 * 
 * @author Friedmann Tamás
 * 
 * @param <T>
 */
public class ListPaginator implements Serializable, Paginator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int pageSize;

	private final int itemCount;

	public ListPaginator(int pageSize, int itemCount) {
		super();
		this.pageSize = pageSize;
		this.itemCount = itemCount;
		firstPage();
	}

	private int startIndex;// zero based, inclusive

	private int endIndex;// zero based, inclusive

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getItemCount() {
		return itemCount;
	}

	@Override
	public void firstPage() {
		startIndex = 0;
		endIndex = startIndex + pageSize - 1;
		displayedItemsChanged();
	}

	@Override
	public void prevPage() {
		startIndex = Math.max(0, startIndex - pageSize);
		endIndex = startIndex + pageSize - 1;
		displayedItemsChanged();
	}

	@Override
	public void nextPage() {
		startIndex = Math.min((itemCount - 1) / pageSize * pageSize, startIndex + pageSize);
		endIndex = startIndex + pageSize - 1;
		displayedItemsChanged();

	}

	@Override
	public void lastPage() {
		startIndex = (itemCount - 1) / pageSize * pageSize;
		endIndex = startIndex + pageSize - 1;
		displayedItemsChanged();
	}

	@Override
	public int getCurrentPage() {
		return (startIndex) / pageSize + 1;
	}

	@Override
	public int getTotalPage() {

		return (itemCount - 1) / pageSize + 1;
	}

	@Override
	public void jumpToItem(final int itemIndex) {
		if (itemIndex>=itemCount) {
			throw new IndexOutOfBoundsException(String.format("%d: out of upper bounds",itemIndex));
		}
		if (itemIndex<0) {
			throw new IndexOutOfBoundsException(String.format("%d: out of lower bounds",itemIndex));
		}
		startIndex = itemIndex / pageSize * pageSize;
		endIndex = startIndex + pageSize - 1;
		displayedItemsChanged();
	}
	
	/**
	 * This method is called when displayed items change, so at paging, jumping. Override it, if external item reload is needed
	 */
	public void displayedItemsChanged() {
		
	}
}
