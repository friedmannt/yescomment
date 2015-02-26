package yescomment.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yescomment.managedbean.UserSessionBean;

@WebServlet(name = "ImageCaptchaServlet", urlPatterns = "/captcha/*",loadOnStartup=1)
public class ImageCaptchaServlet extends HttpServlet {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		
		HttpSession session= httpServletRequest.getSession();
		UserSessionBean userSessionBean =(UserSessionBean) session.getAttribute("userSessionBean");
		byte[] captchaChallengeAsPng = null;
		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

		final CaptchaWrapper captchaWrapper = userSessionBean.getCaptchaWrapper();
		final Captcha captcha = captchaWrapper.getCaptcha();
		BufferedImage challenge = CaptchaUtil.getImageOfString(captcha.getChallenge(), CaptchaUtil.DEFAULT_IMAGE_WIDTH, CaptchaUtil.DEFAULT_IMAGE_HEIGTH);
		//write to png, it supports alpha
		ImageIO.write(challenge, "png", pngOutputStream);
		pngOutputStream.flush();
		captchaChallengeAsPng = pngOutputStream.toByteArray();
		pngOutputStream.close();
		//do not cache
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/png");
		ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsPng);
		responseOutputStream.flush();
		responseOutputStream.close();

	}
}