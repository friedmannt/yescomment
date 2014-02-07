package yescomment.util;

public class NumberUtil {



	/**
	 * min=1
	 * @param max 
	 * @return
	 */
	public static int getRandomInt(int upperExclusiveLimit) {
		return (int) (Math.random() * upperExclusiveLimit);
	}
}
