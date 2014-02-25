package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import yescomment.util.ListPaginator;

public class PaginationTest {

	
	static List<Integer> model;
	
	@BeforeClass 
	public static void populateModel () {
		List<Integer> numbers=new ArrayList<Integer>();
		for (int i=1;i<=100;i++) {
			numbers.add(i);
		}
		model=Collections.unmodifiableList(numbers);
	}
	
	@Test
	public void testTotalPage1() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		assertEquals( 10,paginator.getTotalPage()) ;
	}
	
	@Test
	public void testTotalPage2() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 9);
		assertEquals( 12,paginator.getTotalPage()) ;
	}

	@Test
	public void testInitialPage() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9,10), paginator.getCurrentViewOfModel());
	}
	

	@Test
	public void testFirstLast() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		paginator.last();
		assertEquals(Arrays.asList(91,92,93,94,95,96,97,98,99,100), paginator.getCurrentViewOfModel());
		paginator.first();
		assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9,10), paginator.getCurrentViewOfModel());
	}
	
	@Test
	public void testPrevNext() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		paginator.next();
		assertEquals(Arrays.asList(11,12,13,14,15,16,17,18,19,20), paginator.getCurrentViewOfModel());
		paginator.previous();
		assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9,10), paginator.getCurrentViewOfModel());
	}
	
	@Test
	public void testPrevNextMultipleTimes() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		for (int i=1;i<20;i++) {
			paginator.next();	
		}
		assertEquals(Arrays.asList(91,92,93,94,95,96,97,98,99,100), paginator.getCurrentViewOfModel());
		for (int i=1;i<20;i++) {
			paginator.previous();	
		}
		assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9,10), paginator.getCurrentViewOfModel());
	}

	@Test
	public void testChangeModel() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		paginator.last();
		assertEquals(10, paginator.getCurrentPage());
		List<Integer> newmodel=new ArrayList<Integer>();
		for (int i=1;i<50;i++) {
			newmodel.add(i);
		}
		paginator.setModel(newmodel);
		assertEquals(5, paginator.getCurrentPage());
		paginator.setModel(model);
		assertEquals(5, paginator.getCurrentPage());
	}
	
	@Test
	public void testPageSizeChange() {
		ListPaginator<Integer> paginator=new ListPaginator<Integer>(model, 10);
		paginator.setPageSize(5);
		assertEquals(1, paginator.getCurrentPage());
		assertEquals(Arrays.asList(1,2,3,4,5), paginator.getCurrentViewOfModel());
		paginator.last();
		assertEquals(20, paginator.getCurrentPage());
		assertEquals(Arrays.asList(96,97,98,99,100), paginator.getCurrentViewOfModel());

		
	}

	
	
}
