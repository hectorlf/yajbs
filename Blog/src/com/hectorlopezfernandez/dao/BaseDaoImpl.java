package com.hectorlopezfernandez.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hectorlopezfernandez.model.PersistentObject;

public abstract class BaseDaoImpl {

	private final static Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	private final EntityManager em;
	
	protected BaseDaoImpl(EntityManager em) {
		if (em == null) throw new IllegalArgumentException("Se ha intentado crear un dao con una referencia nula al EntityManager de persistencia. Se debería revisar la configuración y el AppInitializerContextListener.");
		this.em = em;
	}
	
	final protected <T extends PersistentObject> T get(Serializable id, Class<T> cls) {
		assert(id != null);
		assert(cls != null);
		logger.debug("Recuperando entidad {} con id: {}", cls.getSimpleName(), id);
		T obj = em.find(cls, id);
		return obj;
	}
	final protected <T extends PersistentObject> T getReference(Serializable id, Class<T> cls) {
		assert(id != null);
		assert(cls != null);
		logger.debug("Recuperando referencia a entidad {} con id: {}", cls.getSimpleName(), id);
		T obj = em.getReference(cls, id);
		return obj;
	}

	final protected <T extends PersistentObject> List<T> find(String query, Map<String,Object> namedParams, Class<T> cls) {
		assert(query != null && query.length() > 0);
		assert(cls != null);
		logger.debug("Buscando entidades {} mediante JPQL: {}", cls.getSimpleName(), query);
		TypedQuery<T> q = em.createQuery(query, cls);
		setParameters(q, namedParams);
		List<T> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}
	
	final protected <T extends PersistentObject> List<T> find(String query, Map<String,Object> namedParams, Class<T> cls, int firstResult, int maxResults) {
		assert(query != null && query.length() > 0);
		assert(cls != null);
		assert(firstResult >= 0);
		assert(maxResults > 1);
		logger.debug("Listando entidades {} mediante JPQL: {}", cls.getSimpleName(), query);
		logger.debug("Rango de {} elementos partiendo del elemento número {}", maxResults, firstResult);
		TypedQuery<T> q = em.createQuery(query, cls).setFirstResult(firstResult).setMaxResults(maxResults);
		setParameters(q, namedParams);
		List<T> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	/**
	 * @throws NoResultException, cuando no se encuentra ningun resultado
	 * @throws NonUniqueResultException, cuando se encuentran varios resultados y no se puede devolver solo uno
	 */
	final protected <T extends PersistentObject> T findUnique(String query, Map<String,Object> namedParams, Class<T> cls) throws NoResultException, NonUniqueResultException {
		assert(query != null && query.length() > 0);
		assert(cls != null);
		logger.debug("Recuperando entidad {} mediante JPQL: {}", cls.getSimpleName(), query);
		TypedQuery<T> q = em.createQuery(query, cls);
		setParameters(q, namedParams);
		T result = q.getSingleResult();
		return result;
	}
	
	/**
	 * @throws NoResultException, cuando no se encuentra ningun resultado
	 * @throws NonUniqueResultException, cuando se encuentran varios resultados y no se puede devolver solo uno
	 */
	final protected Long findUniqueId(String query, Map<String,Object> namedParams) throws NoResultException, NonUniqueResultException {
		assert(query != null && query.length() > 0);
		logger.debug("Recuperando campo Long mediante JPQL: {}", query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);
		setParameters(q, namedParams);
		Long result = q.getSingleResult();
		return result;
	}

	final protected List<Object[]> list(String query, Map<String,Object> namedParams) {
		assert(query != null && query.length() > 0);
		logger.debug("Listando campos individuales mediante JPQL: {}", query);
		TypedQuery<Object[]> q = em.createQuery(query, Object[].class);
		setParameters(q, namedParams);
		List<Object[]> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}
	
	final protected List<Object[]> list(String query, Map<String,Object> namedParams, int firstResult, int maxResults) {
		assert(query != null && query.length() > 0);
		assert(firstResult >= 0);
		assert(maxResults > 1);
		logger.debug("Listando campos individuales mediante JPQL: {}", query);
		logger.debug("Rango de {} elementos partiendo del elemento número {}", maxResults, firstResult);
		TypedQuery<Object[]> q = em.createQuery(query, Object[].class).setFirstResult(firstResult).setMaxResults(maxResults);
		setParameters(q, namedParams);
		List<Object[]> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}
	
	final protected List<Long> listIds(String query, Map<String,Object> namedParams) {
		assert(query != null && query.length() > 0);
		logger.debug("Listando campos Long mediante JPQL: {}", query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);
		setParameters(q, namedParams);
		List<Long> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	final protected Long count(String query, Map<String,Object> namedParams) {
		assert(query != null && query.length() > 0);
		logger.debug("Contando objetos mediante JPQL: {}", query);
		TypedQuery<Long> q = em.createQuery(query, Long.class);
		setParameters(q, namedParams);
		Long result = q.getSingleResult();
		return result;
	}

	final protected <T extends PersistentObject> Long save(T obj) {
		assert(obj != null);
		logger.debug("Persistiendo entidad {} en base de datos", obj.getClass().getSimpleName());
		em.persist(obj);
		return obj.getId();
	}

	final protected <T extends PersistentObject> void delete(T obj) {
		assert(obj != null);
		logger.debug("Borrando entidad {} de la base de datos", obj.getClass().getSimpleName());
		em.remove(obj);
	}

	
	/*
	 * NamedQuery methods
	 */

	final protected <T extends PersistentObject> List<T> findNamed(String queryName, Map<String,Object> namedParams, Class<T> cls) {
		assert(queryName != null && queryName.length() > 0);
		assert(cls != null);
		logger.debug("Buscando entidades {} mediante NamedQuery: {}", cls.getSimpleName(), queryName);
		TypedQuery<T> q = em.createNamedQuery(queryName, cls);
		setParameters(q, namedParams);
		List<T> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}

	/**
	 * @throws NoResultException, cuando no se encuentra ningun resultado
	 * @throws NonUniqueResultException, cuando se encuentran varios resultados y no se puede devolver solo uno
	 */
	final protected <T extends PersistentObject> T findNamedUnique(String queryName, Map<String,Object> namedParams, Class<T> cls) throws NoResultException, NonUniqueResultException {
		assert(queryName != null && queryName.length() > 0);
		logger.debug("Recuperando entidad mediante NamedQuery: {}", queryName);
		TypedQuery<T> q = em.createNamedQuery(queryName, cls);
		setParameters(q, namedParams);
		T result = q.getSingleResult();
		return result;
	}
	
	/**
	 * @throws NoResultException, cuando no se encuentra ningun resultado
	 * @throws NonUniqueResultException, cuando se encuentran varios resultados y no se puede devolver solo uno
	 */
	final protected Long findNamedUniqueId(String queryName, Map<String,Object> namedParams) throws NoResultException, NonUniqueResultException {
		assert(queryName != null && queryName.length() > 0);
		logger.debug("Recuperando campo Long mediante NamedQuery: {}", queryName);
		TypedQuery<Long> q = em.createNamedQuery(queryName, Long.class);
		setParameters(q, namedParams);
		Long result = q.getSingleResult();
		return result;
	}

	final protected List<Long> listNamedIds(String queryName, Map<String,Object> namedParams) {
		assert(queryName != null && queryName.length() > 0);
		logger.debug("Listando campos Long mediante NamedQuery: {}", queryName);
		TypedQuery<Long> q = em.createNamedQuery(queryName, Long.class);
		setParameters(q, namedParams);
		List<Long> results = q.getResultList();
		if (results.size() == 0) return Collections.emptyList();
		return results;
	}
	
	
	/* 
	 * Utility methods
	 */
	
	private void setParameters(Query q, Map<String,Object> namedParams) {
		if (q == null || namedParams == null || namedParams.size() == 0) return;
		for (String key : namedParams.keySet()) {
			Object o = namedParams.get(key);
			q.setParameter(key, o);
		}
	}

}