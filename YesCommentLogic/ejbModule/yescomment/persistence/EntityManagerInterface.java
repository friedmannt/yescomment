package yescomment.persistence;

import java.io.Serializable;
import java.util.List;

import yescomment.model.AbstractEntity;

public interface EntityManagerInterface<T extends AbstractEntity> extends Serializable {

	T create(T entity);

	T merge(T entity);

	void remove(T entity);

	T find(String id);

	List<T> findAll();

	int count();
	
	List<String> findAllIds();
	
	List<T> find (List<String> ids);

	List<T> getEntitiesOrdered(String attributeName, boolean isAscending, Integer maxResults);
	
	List<T> getEntitiesOrdered(String attributeName, boolean isAscending);

	
}
