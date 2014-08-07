package yescomment.util;

import java.util.Random;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StringUtil {
	static final char[] alphabetCharsLowerCase = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	static final char[] alphabetCharsUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	static final char[] numberChars = "1234567890".toCharArray();

	/**
	 * alphanumeric uppercase, lowercase,numbers
	 */
	static final char[] availableChars = new char[alphabetCharsLowerCase.length + alphabetCharsUpperCase.length + numberChars.length];
	static {
		// add all lowercase alphabet
		System.arraycopy(alphabetCharsLowerCase, 0, availableChars, 0, alphabetCharsLowerCase.length);
		// add all uppercase alphabet
		System.arraycopy(alphabetCharsUpperCase, 0, availableChars, alphabetCharsLowerCase.length, alphabetCharsUpperCase.length);
		// add all digit
		System.arraycopy(numberChars, 0, availableChars, alphabetCharsLowerCase.length + alphabetCharsUpperCase.length, numberChars.length);
	}

	/**
	 * Returns a random character from the avaliable characters
	 * 
	 * @param random
	 * @return
	 */
	public static char getRandomChar(@NotNull Random random) {
		return availableChars[random.nextInt(availableChars.length)];
	}

	/**
	 * Returns a random string with the given size
	 * 
	 * @param size
	 * @param random
	 * @return
	 */
	public static String getRandomString(@NotNull @Max(8) @Min(1) int size, @NotNull Random random) {
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			sb.append(getRandomChar(random));
		}
		return sb.toString();
	}

	/**
	 * returns the original string concatenated with itself n times
	 * 
	 * @param originalString
	 * @param n
	 * @return
	 */
	public static String stringNTimes(@NotNull String originalString, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(originalString);
		}
		return sb.toString();

	}

	/**
	 * Maximizes string length. If original string is longer, the first n chars
	 * remain
	 * 
	 * @param originalString
	 * @param maxLength
	 * @return
	 */
	public static String maximizeStringLength(@NotNull String originalString, int maxLength) {
		if (originalString.length() <= maxLength) {
			return originalString;
		} else {
			return originalString.substring(0, maxLength);
		}
	}
	
}
