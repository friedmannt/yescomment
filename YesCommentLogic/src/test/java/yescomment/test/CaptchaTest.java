package yescomment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Test;

import yescomment.captcha.Captcha;
import yescomment.captcha.CaptchaUtil;

public class CaptchaTest {

	@Test
	public void testCaptchaAnswer() {
		Captcha captcha = new Captcha(10);
		assertEquals(10, captcha.getChallenge().length());
		assertTrue(captcha.answer(captcha.getChallenge()));
		assertFalse(captcha.answer("----------"));

	}

	@Test
	public void testCaptchaImage() {
		BufferedImage image = CaptchaUtil.getImageOfString("abcdefgh", 200, 100);
		assertNotNull(image);
		assertEquals(200, image.getWidth());
		assertEquals(100, image.getHeight());
		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 100; y++) {
				int color = image.getRGB(x, y);
				int red = (color & 0x00ff0000) >> 16;
				int green = (color & 0x0000ff00) >> 8;
				int blue = color & 0x000000ff;
				int alpha = (color >> 24) & 0xff;
				assertEquals(0, red);
				assertEquals(0, green);
				assertEquals(0, blue);
			}
		}
	}

}
