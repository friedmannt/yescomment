package yescomment.test;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import yescomment.util.SHA256Encoder;

public class SHA256Test {

	
	@Test
	public void testSHA256Encoding() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",  SHA256Encoder.encodeHex("password"));
	}
	
}
