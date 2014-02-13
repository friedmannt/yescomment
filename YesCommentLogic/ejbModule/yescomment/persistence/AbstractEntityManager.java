package yescomment.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import yescomment.model.AbstractEntity;

public abstract class AbstractEntityManager<T extends AbstractEntity> implements EntityManagerInterface<T> {

	
	protected abstract EntityManager getEntityManager();
	
	private Class<T> entityClass;

	public AbstractEntityManager(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T create(T entity) {
		
		EntityManager em = getEntityManager();
		
		em.persist(entity);
		
		//em.close();
		notifyEntityCreation(entity);
		return entity;

	}
	@Override
	public T merge(T entity) {
		notifyEntityModification(entity);
		EntityManager em =  getEntityManager();
		
		T mergedEntity = em.merge(entity);
		
		//em.close();
		return mergedEntity;

	}
	@Override
	public void remove(T entity) {
		notifyEntityDeletion(entity);
		EntityManager em = getEntityManager();
		
		em.remove(em.merge(entity));
		
		
		//em.close();
	}
	@Override
	public T find(Long id) {
		EntityManager em =  getEntityManager();
		return em.find(entityClass, id);
	}
	@Override
	public List<T> findAll() {
		EntityManager em =  getEntityManager();
		javax.persistence.criteria.CriteriaQuery<T> cq = em
				.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}
	@Override
	public int count() {
		EntityManager em =  getEntityManager();
		javax.persistence.criteria.CriteriaQuery<Long> cq = em
				.getCriteriaBuilder().createQuery(Long.class);
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(em.getCriteriaBuilder().count(rt));
		javax.persistence.Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}
	
	
	protected abstract void notifyEntityCreation(T entity);
	
	protected abstract void notifyEntityModification(T entity);
	
	protected abstract void notifyEntityDeletion(T entity);

}
