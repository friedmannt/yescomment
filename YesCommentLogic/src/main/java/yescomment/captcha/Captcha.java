package yescomment.captcha;

import java.io.Serializable;
import java.util.Random;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import yescomment.util.StringUtil;



public class Captcha implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String challenge;



	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public Captcha(@Max(10) @Min(1) int length) {
		
		Random random = new Random();
		challenge=StringUtil.getRandomString(length, random);
	
		

	}
	/**
	 * complete equals, no trim, no case insensivity
	 * @param captchaTry
	 * @return
	 */
	public boolean answer(@NotNull String captchaTry ) {
		return captchaTry.equals(challenge);
	}

	@Override
	public String toString() {
		return "Captcha [answer=" + challenge + "]";
	}

}
