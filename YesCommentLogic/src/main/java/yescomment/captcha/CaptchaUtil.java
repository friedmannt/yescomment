package yescomment.captcha;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import yescomment.util.StringUtil;

public class CaptchaUtil {

	
	public static final int DEFAULT_IMAGE_WIDTH=400;
	
	public static final int DEFAULT_IMAGE_HEIGTH=100;
	
	
	public static final int FONT_SIZE = 80;
	

	
	public static BufferedImage getImageOfString(@NotNull String captcha, @NotNull int imageWidth, @NotNull int imageHeight) {
		BufferedImage background = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);//support alpha
		// get graphics
		Graphics2D graph = background.createGraphics();
		graph.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// calcul text position
		final Font font = new Font(Font.SERIF,0,FONT_SIZE);
		graph.setComposite(AlphaComposite.Clear);
		graph.fillRect(0, 0, imageWidth, imageHeight);
		graph.setComposite(AlphaComposite.Src);
		graph.setPaint(Color.BLACK);
		graph.setFont(font);
		

		
		AffineTransform orig = graph.getTransform();
		char[] captchaChars=captcha.toCharArray();
		for (int i=0;i<captchaChars.length;i++) {
			//reset rotation
			graph.setTransform(orig);
			//a pseudorandom number based on char value and position
			int numberForChar=captchaChars[i]*(i+1);
			int rotationDegree=numberForChar%60-30;//between -30 and +30
			double radians=Math.toRadians(rotationDegree);
			//0,0 is the left top, 0,imageheight is the left bottom. But letters go below 0 line, so 10 px is elevated
			//all chars are shifted on x axis with 1/8 of total width
			final int charposx = i*(imageWidth/captcha.length());
			final int charposy = imageHeight-imageHeight/4;
			//rotate at charpos
			graph.rotate(radians,charposx,charposy);
			// one char
			graph.drawChars(captchaChars,i,1, charposx, charposy);
			/*
			//random black dots for each char 10 times 
			for (int j=1;j<=10;j++) {
				int randomDotX=numberForChar*i%imageWidth;
				int randomDotY=numberForChar*i%imageHeight;
				graph.drawOval(randomDotX, randomDotY, 1, 1)	;	
			}*/
			
		}

		graph.dispose();
		return background;

	}

	public static void main(String[] a) throws IOException {
	
		
		// the output stream to render the captcha image as jpeg into
		FileOutputStream pngOutputStream = new FileOutputStream("captchatest.png");

		// get the session id that will identify the generated captcha.
		// the same id must be used to validate the response, the session id
		// is a good candidate!
		String captchaId = StringUtil.getRandomString(8, new Random());
		// call the ImageCaptchaService getChallenge method
		BufferedImage challenge = getImageOfString(captchaId, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGTH);

		// a jpeg encoder

		ImageIO.write(challenge, "png", pngOutputStream);
		pngOutputStream.flush();

		pngOutputStream.close();

	}

}
