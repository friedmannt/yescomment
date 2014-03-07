package yescomment.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.NotNull;

/**
 * Creates a sorted treemap based on paramater map, sorted by value ascending or desceding
 * @author Friedmann Tamás
 *
 */
public class MapSorter {
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(
			@NotNull final Map<K, V> map, final boolean ascending) {
		Comparator<K> valueComparatorAscending = new Comparator<K>() {
			@Override
			public int compare(K k1, K k2) {
				int res=  map.get(k1).compareTo(map.get(k2));
				return res != 0 ? res : 1; // Special fix to preserve items with equal values
				
			}
		};
		Comparator<K> valueComparatorDescending = new Comparator<K>() {
			@Override
			public int compare(K k1, K k2) {
				int res=  map.get(k2).compareTo(map.get(k1));
				return res != 0 ? res : 1; // Special fix to preserve items with equal values
			
				
			}
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(ascending?valueComparatorAscending:valueComparatorDescending);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
	
	
	
}