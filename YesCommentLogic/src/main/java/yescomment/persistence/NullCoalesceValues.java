package yescomment.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * At query ordering, null values will be coalesced to default
 * @author Friedmann Tamás
 *
 */
public class NullCoalesceValues {
	
	private static Map<Class<?>,Object> coalescedDefaultValuesForClasses= new HashMap<Class<?>, Object>();
	
	static {
		Number zero = 0;
		coalescedDefaultValuesForClasses.put(Number.class, zero);
		Date epoch=new Date(0);
		coalescedDefaultValuesForClasses.put(Date.class, epoch);
		String empty="";
		coalescedDefaultValuesForClasses.put(String.class, empty);
		
	}
	
	public static Object getCoalescedDefaultValueForClass(Class<?> objectClass) {
		return coalescedDefaultValuesForClasses.get(objectClass);
	}
}
