package yescomment.captcha;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.validation.constraints.Max;

import com.sun.istack.NotNull;

public class Captcha {
	private CaptchaOption correctCaptchaOption;

	public CaptchaOption getCorrectCaptchaOption() {
		return correctCaptchaOption;
	}

	public void setCorrectCaptchaOption(CaptchaOption correctCaptchaOption) {
		this.correctCaptchaOption = correctCaptchaOption;
	}

	private List<CaptchaOption> possibleCaptchaOptions;

	public List<CaptchaOption> getPossibleCaptchaOptions() {
		return possibleCaptchaOptions;
	}

	public void setPossibleCaptchaOptions(List<CaptchaOption> possibleCaptchaOptions) {
		this.possibleCaptchaOptions = possibleCaptchaOptions;
	}

	

	public Captcha(@Max(100) int optionsCount) {
		super();
		Random random = new Random();

		possibleCaptchaOptions = new ArrayList<CaptchaOption>(optionsCount);
		while (possibleCaptchaOptions.size() < optionsCount) {
			CaptchaOption captchaOption = new CaptchaOption(CaptchaOption.Color.values()[random.nextInt(CaptchaOption.Color.values().length)], random.nextInt(100));
			if (!possibleCaptchaOptions.contains(captchaOption)) {
				possibleCaptchaOptions.add(captchaOption);
			}

		}
		int correctCaptchaOptionIndex = random.nextInt(optionsCount);
		correctCaptchaOption = possibleCaptchaOptions.get(correctCaptchaOptionIndex);

	}
	
	public boolean answer(@NotNull CaptchaOption captchaOption ) {
		return correctCaptchaOption.equals(captchaOption);
	}

}
