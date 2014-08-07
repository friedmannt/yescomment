package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;


import yescomment.util.StringUtil;

public class StringUtilTest {

	
	
	@Test
	public void testRandomString() {
		String randomString = StringUtil.getRandomString(6, new Random());
		assertEquals(6, randomString.length());
	}
	
	@Test
	public void testStringNTimes() {
		String threeTimes = StringUtil.stringNTimes("hello", 3);
		assertEquals("hellohellohello", threeTimes);
	}
	@Test
	public void testStringMaxLength() {
		String maxedToThree = StringUtil.maximizeStringLength("hello", 3);
		String maxedToFive = StringUtil.maximizeStringLength("hello", 5);
		String maxedToTen = StringUtil.maximizeStringLength("hello", 10);
		assertEquals("hel", maxedToThree);
		assertEquals("hello", maxedToFive);
		assertEquals("hello", maxedToTen);
	}
}
