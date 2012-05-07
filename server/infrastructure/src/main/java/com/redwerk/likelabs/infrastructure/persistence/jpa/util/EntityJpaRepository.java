package com.redwerk.likelabs.infrastructure.persistence.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class EntityJpaRepository<T> {

    private final EntityManager em;

    public EntityJpaRepository(EntityManager em) {
        this.em = em;
    }

    public T findById(Class<T> entityClass, Long id) {
        return em.find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public T findSingleEntity(String queryString, Map<String, Object> parameters) {
        try {
            return (T) getQuery(queryString, parameters).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findEntityList(String queryString, Map<String, Object> parameters) {
        return getQuery(queryString, parameters).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> findEntityList(String queryString) {
        return em.createQuery(queryString).getResultList();
    }

     public void add(T entity) {
        em.persist(entity);
        em.flush();
        em.refresh(entity);
    }

    public void remove(T entity) {
        em.remove(entity);
        em.flush();
    }

    private Query getQuery(String queryString, Map<String, Object> parameters) {
        Query query = em.createQuery(queryString);
        for (Map.Entry<String, Object> p: parameters.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
        }
        return query;
    }

}
