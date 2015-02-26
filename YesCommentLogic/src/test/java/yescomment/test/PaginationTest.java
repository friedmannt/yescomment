package yescomment.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import yescomment.util.ListPaginator;

public class PaginationTest {

	
	
	@Test
	public void testTotalPage1() {
		ListPaginator paginator=new ListPaginator(10, 100);
		assertEquals( 10,paginator.getTotalPage()) ;
	}
	
	@Test
	public void testTotalPage2() {
		ListPaginator paginator=new ListPaginator( 9,100);
		assertEquals( 12,paginator.getTotalPage()) ;
	}

	@Test
	public void testInitialPage() {
		ListPaginator paginator=new ListPaginator( 10,100);
		assertEquals(1	, paginator.getStartIndex()+1);
		assertEquals(10	, paginator.getEndIndex()+1);
		
	}
	

	@Test
	public void testFirstLast() {
		ListPaginator paginator=new ListPaginator( 10,100);
		paginator.lastPage();
		assertEquals(91	, paginator.getStartIndex()+1);
		assertEquals(100	, paginator.getEndIndex()+1);

		
		paginator.firstPage();
		assertEquals(1	, paginator.getStartIndex()+1);
		assertEquals(10	, paginator.getEndIndex()+1);
	}
	
	@Test
	public void testPrevNext() {
		ListPaginator paginator=new ListPaginator( 10,100);
		paginator.nextPage();
		assertEquals(11	, paginator.getStartIndex()+1);
		assertEquals(20	, paginator.getEndIndex()+1);

		paginator.prevPage();
		assertEquals(1	, paginator.getStartIndex()+1);
		assertEquals(10	, paginator.getEndIndex()+1);

	}
	
	@Test
	public void testPrevNextMultipleTimes() {
		ListPaginator paginator=new ListPaginator( 10,100);
		for (int i=1;i<20;i++) {
			paginator.nextPage();	
		}
		assertEquals(91	, paginator.getStartIndex()+1);
		assertEquals(100	, paginator.getEndIndex()+1);
		for (int i=1;i<20;i++) {
			paginator.prevPage();	
		}
		assertEquals(1	, paginator.getStartIndex()+1);
		assertEquals(10	, paginator.getEndIndex()+1);

	}
	
	@Test
	public void testJumpToItem() {
		ListPaginator paginator=new ListPaginator( 11,100);
		paginator.jumpToItem(0);
		assertEquals(1, paginator.getCurrentPage());
		paginator.jumpToItem(3);
		assertEquals(1, paginator.getCurrentPage());
	
		paginator.jumpToItem(98);
		assertEquals(9, paginator.getCurrentPage());
		paginator.jumpToItem(99);
		assertEquals(10, paginator.getCurrentPage());
		
		
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testJumpToItemOutOfLoweBounds() {
		ListPaginator paginator=new ListPaginator( 11,100);
		paginator.jumpToItem(-1);
		
		
		
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testJumpToItemOutOfUpperBounds() {
		ListPaginator paginator=new ListPaginator( 11,100);
		paginator.jumpToItem(100);
		
		
		
	}
	
	@Test
	public void testCount() {
		ListPaginator paginator=new ListPaginator( 11,100);
		assertEquals(100, paginator.getItemCount());
		assertEquals(11, paginator.getPageSize());

	}

	

	
	
}
