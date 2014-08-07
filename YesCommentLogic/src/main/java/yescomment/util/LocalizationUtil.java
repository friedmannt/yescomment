package yescomment.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.constraints.NotNull;

public class LocalizationUtil {
	static Map<Locale, ResourceBundle> resourceBundles = new HashMap<Locale, ResourceBundle>();

	private static String getEnumResourceBundleKey(@NotNull Enum<?> enumValue) {
		return enumValue.getClass().getSimpleName().toLowerCase() + "_" + enumValue.toString().toLowerCase();

	}

	public static String getTranslation(@NotNull String key, @NotNull Locale locale) {

		if (resourceBundles.get(locale) == null) {
			resourceBundles.put(locale, ResourceBundle.getBundle("yescomment.localization.yescomment", locale));
		}
		return resourceBundles.get(locale).getString(key);
	}

	public static String getEnumTranslation(@NotNull Enum<?> enumValue, @NotNull Locale locale) {
		if (enumValue == null) {
			return null;
		}
		String enumKey = getEnumResourceBundleKey(enumValue);

		return getTranslation(enumKey, locale);
	}

}
