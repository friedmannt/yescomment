package yescomment.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TranslationTest {

	static Map<String,Properties> propertiesOfLanguages=new HashMap<String, Properties>();
	
	private static Properties loadPropertiesOfLanguage(String langCode) throws IOException {
		String fileName="/yescomment/localization/yescomment_"+langCode+".properties";
		Properties properties=new Properties();
		properties.load(TranslationTest.class.getResourceAsStream(fileName));
		return properties;
	}
	
	@BeforeClass
	public static void fillPropertiesOfLanguages() throws IOException {
			
			propertiesOfLanguages.put("en", loadPropertiesOfLanguage("en"));
			propertiesOfLanguages.put("hu", loadPropertiesOfLanguage("hu"));
			propertiesOfLanguages.put("fr", loadPropertiesOfLanguage("fr"));
			
		
	}

	
	
	@Test
	public void testSameKeys() throws IOException {
		//en properties are the standard
		Properties enProperties=propertiesOfLanguages.get("en");
		for (String otherLanguage:propertiesOfLanguages.keySet()) {
			Properties otherProperties=propertiesOfLanguages.get(otherLanguage);
			
			for(Object key:enProperties.keySet()) {
				Assert.assertTrue(key+ " difference found in en not found in " +otherLanguage, otherProperties.containsKey(key));
			}
			for(Object key:otherProperties.keySet()) {
				Assert.assertTrue(key+ " difference not found in en found in " +otherLanguage, enProperties.containsKey(key));
			}
			
		}
		
	}

	
}
