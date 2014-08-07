package yescomment.util;

public interface Paginator {

	void firstPage();

	void prevPage();

	void nextPage();

	void lastPage();

	int getCurrentPage();
	
	int getTotalPage();

	void jumpToItem(int itemIndex);

	

	
	
}
