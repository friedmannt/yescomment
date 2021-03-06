package yescomment.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import yescomment.model.AbstractEntity;

public abstract class AbstractEntityManager<T extends AbstractEntity> implements EntityManagerInterface<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	protected abstract EntityManager getEntityManager();

	private Class<T> entityClass;

	public AbstractEntityManager(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T create(T entity) {

		EntityManager em = getEntityManager();

		em.persist(entity);


		notifyEntityCreation(entity);
		return entity;

	}

	@Override
	public T merge(T entity) {
		notifyEntityModification(entity);
		EntityManager em = getEntityManager();

		T mergedEntity = em.merge(entity);

		return mergedEntity;

	}

	@Override
	public void remove(T entity) {
		notifyEntityDeletion(entity);
		EntityManager em = getEntityManager();

		em.remove(em.merge(entity));

	}
	
	@Override
	public void remove(String id) {
		T entity=find(id);
		remove(entity);
	}

	@Override
	public T find(String id) {
		EntityManager em = getEntityManager();
		return em.find(entityClass, id);
	}

	@Override
	public List<T> findAll() {
		EntityManager em = getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = criteriaBuilder.createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}

	@Override
	public int count() {
		EntityManager em = getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<T> rt = cq.from(entityClass);
		cq.select(criteriaBuilder.count(rt));
		javax.persistence.Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	@Override
	public List<String> findAllIds() {

		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<T> r = cq.from(entityClass);
		cq.select(r.<String> get("id"));
		TypedQuery<String> tq = em.createQuery(cq);
		return tq.getResultList();
	}

	@Override
	public List<T> find(List<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		} else {
			EntityManager em = getEntityManager();
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = criteriaBuilder.createQuery(entityClass);
			Root<T> r = cq.from(entityClass);
			cq.select(r);
			cq.where(r.<String> get("id").in(ids));
			final TypedQuery<T> tq = em.createQuery(cq);
			return tq.getResultList();
		}
	}

	@Override
	public List<T> getEntitiesOrdered( String attributeName,Class<?> attributeClass, boolean isAscending) {

		return getEntitiesOrdered(attributeName,attributeClass, isAscending, null, null);
	}

	@Override
	public List<T> getEntitiesOrdered(String attributeName,Class<?> attributeClass, boolean isAscending, Date minimumCreateDate) {
		return getEntitiesOrdered(attributeName,attributeClass, isAscending, null, minimumCreateDate);
	}
	
	@Override
	public List<T> getEntitiesOrdered(String attributeName,Class<?> attributeClass, boolean isAscending, Integer maxResults) {
		return getEntitiesOrdered(attributeName,attributeClass,isAscending, maxResults, null);
	}


	@Override
	public List<T> getEntitiesOrdered(String attributeName,Class<?> attributeClass, boolean isAscending, Integer maxResults, Date minimumCreateDate) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

		CriteriaQuery<T> q = cb.createQuery(entityClass);

		Root<T> r = q.from(entityClass);
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		if (minimumCreateDate != null) {
			predicateList.add(cb.greaterThanOrEqualTo(r.<Date>get("createDate"), minimumCreateDate));
		}
		
		Predicate[] predicates = new Predicate[predicateList.size()];
		predicateList.toArray(predicates);
		q.where(predicates);
		Object nullCoalescedValue=NullCoalesceValues.getCoalescedDefaultValueForClass(attributeClass);
		q.orderBy(cb.desc( cb.coalesce( r.get(attributeName), cb.literal(nullCoalescedValue))));//if order by attribute is null, constant is applied
		TypedQuery<T> query = getEntityManager().createQuery(q);
		if (maxResults != null) {
			query.setMaxResults(maxResults);
		}

		return query.getResultList();
	}

	
	protected void notifyEntityCreation(T entity) {
		
	}
	
	protected  void notifyEntityModification(T entity) {
		
	}
	
	protected  void notifyEntityDeletion(T entity) {
		
	}
}
