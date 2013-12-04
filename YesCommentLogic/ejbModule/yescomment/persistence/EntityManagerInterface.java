package yescomment.persistence;

import java.io.Serializable;
import java.util.List;

import yescomment.model.AbstractEntity;

public interface EntityManagerInterface<T extends AbstractEntity> extends Serializable {

	T create(T entity);

	T merge(T entity);

	void remove(T entity);

	T find(Long id);

	List<T> findAll();

	int count();

	T save(T entity);
}
