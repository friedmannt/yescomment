package yescomment.test;

import java.io.IOException;

import org.junit.Test;

import yescomment.captcha.Captcha;
import yescomment.captcha.CaptchaOption;
import static org.junit.Assert.*;

public class CaptchaTest {

	@Test
	public void testCorrectCaptcha() throws IOException {
			Captcha captcha=new Captcha(100);
			assertTrue(captcha.answer(captcha.getCorrectCaptchaOption()));

	}
	
	@Test
	public void testInCorrectCaptcha() throws IOException {
		Captcha captcha=new Captcha(100);
		int indexOfCorrectCaptcha=captcha.getPossibleCaptchaOptions().indexOf(captcha.getCorrectCaptchaOption());
		for (int i=0;i<100;i++) {
			if (i!=indexOfCorrectCaptcha) {
				CaptchaOption incorrectCaptchaOption=captcha.getPossibleCaptchaOptions().get(i);
				assertFalse(captcha.answer(incorrectCaptchaOption));
			}
		}

	}
	
	@Test
	public void testNoOptionsAreSame() throws IOException {
		Captcha captcha=new Captcha(100);
		for (int i=0;i<100;i++) {
			for (int j=0;j<100;j++) {	
				if (i!=j) {
					CaptchaOption iCaptchaOption=captcha.getPossibleCaptchaOptions().get(i);
					CaptchaOption jCaptchaOption=captcha.getPossibleCaptchaOptions().get(j);
					assertFalse(iCaptchaOption.equals(jCaptchaOption));
				}
				
				
			}
		}

	}

	
}
