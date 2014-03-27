package yescomment.util;

public interface Paginator {

	
	public void firstPage();
	public void lastPage();
	public void nextPage();
	public void prevPage();
	public int getCurrentPage();
	public int getTotalPage();
}
