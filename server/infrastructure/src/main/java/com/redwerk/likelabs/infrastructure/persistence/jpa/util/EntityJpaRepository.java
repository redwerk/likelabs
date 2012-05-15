package com.redwerk.likelabs.infrastructure.persistence.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EntityJpaRepository<T, ID extends Serializable> {

    private final EntityManager em;

    private final Class<T> entityClass;

    public EntityJpaRepository(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    public T findById(ID id) {
        return em.find(entityClass, id);
    }

    public T findSingleEntity(String queryString, Map<String, Object> parameters) {
        try {
            return getQuery(queryString, parameters).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<T> findEntityList(String queryString, Map<String, Object> parameters, int offset, int count) {
        return getQueryWithInterval(getQuery(queryString, parameters), offset, count).getResultList();
    }
    
    public List<T> findEntityList(String queryString, int offset, int count) {
        return getQueryWithInterval(em.createQuery(queryString, entityClass), offset, count).getResultList();
    }

    private TypedQuery<T> getQueryWithInterval(TypedQuery<T> query, int offset, int count) {
        if (offset >= 0) {
            query.setFirstResult(offset);
        }
        if (count >= 0) {
            query.setMaxResults(count);
        }
        return query;
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

    private TypedQuery<T> getQuery(String queryString, Map<String, Object> parameters) {
        TypedQuery<T> query = em.createQuery(queryString, entityClass);
        for (Map.Entry<String, Object> p: parameters.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
        }
        return query;
    }

}
