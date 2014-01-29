package yescomment.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationUtil {
	static Map<Locale, ResourceBundle> resourceBundles = new HashMap<Locale, ResourceBundle>();

	private static String getEnumResourceBundleKey(Enum<?> enumValue) {
		return enumValue.getClass().getSimpleName().toLowerCase() + "_"
				+ enumValue.toString().toLowerCase();

	}

	private static String getNumberBundleKey(Integer number) {
		
		return "number_"+number;
	}
	
	public static String getTranslation(String key, Locale locale) {
	
		if (resourceBundles.get(locale) == null) {
			resourceBundles.put(locale, ResourceBundle.getBundle(
					"yescomment.localization.yescomment", locale));
		}
		return resourceBundles.get(locale).getString(key);
	}
	public static String getEnumTranslation(Enum<?> enumValue, Locale locale) {
		if (enumValue == null) {
			return null;
		}
		String enumKey = getEnumResourceBundleKey(enumValue);

		return getTranslation(enumKey,locale);
	}
	
	public static String getNumberTranslation(Integer number, Locale locale) {
		if (number == null) {
			return null;
		}
		String enumKey = getNumberBundleKey(number);

		return getTranslation(enumKey,locale);
	}


	
}
