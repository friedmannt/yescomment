package yescomment.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import yescomment.model.AbstractEntity;

public interface EntityManagerInterface<T extends AbstractEntity> extends Serializable {

	T create(T entity);

	T merge(T entity);

	void remove(T entity);

	T find(String id);

	List<T> findAll();

	int count();
	
	/**
	 * Returns all entity ids
	 * @return all ids
	 */
	List<String> findAllIds();
	
	
	/**
	 * returs entity list, with given ids
	 * @param ids
	 * @return the list of entities
	 */
	List<T> find (List<String> ids);

	
	/**
	 * Gets all entities ordered by attributename
	 * @param attributeName the name of the ordering attribute
	 * @param attributeClass the class of the attribute, can be String.class, Number.class,Date.class
	 * @param isAscending if true, ascending, if false, descending
	 * @return
	 */
	List<T> getEntitiesOrdered(String attributeName, Class<?> attributeClass,boolean isAscending);
	
	/**
	 * Gets all entities ordered by attributename
	 * @param attributeName  the name of the ordering attribute
	 * @param attributeClass the class of the attribute, can be String.class, Number.class,Date.class
	 * @param isAscending if true, ascending, if false, descending
	 * @param maxResults limiting results to n
	 * @return
	 */
	List<T> getEntitiesOrdered(String attributeName,Class<?> attributeClass, boolean isAscending, Integer maxResults);
	
	/**
	 * Gets all entities ordered by attributename
	 * @param attributeName the name of the ordering attribute
	 * @param attributeClass the class of the attribute, can be String.class, Number.class,Date.class
	 * @param isAscending  if true, ascending, if false, descending
	 * @param minimumCreateDate entity create date shoulb be greater than or equal the minimum date (inclusive)
	 * @return
	 */
	List<T> getEntitiesOrdered(String attributeName,Class<?> attributeClass, boolean isAscending,Date minimumCreateDate);
	
	/**
	 * Gets all entities ordered by attributename
	 * @param attributeName the name of the ordering attribute
	 * @param attributeClass the class of the attribute, can be String.class, Number.class,Date.class
	 * @param isAscending if true, ascending, if false, descending
	 * @param maxResults limiting results to n
	 * @param minimumCreateDate  entity create date shoulb be greater than or equal the minimum date (inclusive)
	 * @return
	 */
	List<T> getEntitiesOrdered(String attributeName,Class<?> attributeClass, boolean isAscending,Integer maxResults,Date minimumCreateDate);

	
	
}
