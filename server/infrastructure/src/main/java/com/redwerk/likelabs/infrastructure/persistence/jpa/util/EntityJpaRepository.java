package com.redwerk.likelabs.infrastructure.persistence.jpa.util;

import com.redwerk.likelabs.domain.model.query.Pager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EntityJpaRepository<T, ID extends Serializable> {

    private static final String COUNT_QUERY = "select count(e) from %s e";

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
            return getEntityQuery(queryString, parameters).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public int getCount() {
        return getCount(String.format(COUNT_QUERY, entityClass.getName()));
    }

    public int getCount(String queryString) {
        return getCount(queryString, Collections.<String, Object>emptyMap());
    }

    public int getCount(String queryString, Map<String, Object> parameters) {
        Query query = getQuery(queryString, parameters);
        return ((Long) query.getSingleResult()).intValue();
    }

    public List<T> findEntityList(String queryString, Map<String, Object> parameters, Pager pager) {
        return getQueryWithInterval(getEntityQuery(queryString, parameters), pager).getResultList();
    }
    
    public List<T> findEntityList(String queryString, Pager pager) {
        return getQueryWithInterval(em.createQuery(queryString, entityClass), pager).getResultList();
    }

    private TypedQuery<T> getQueryWithInterval(TypedQuery<T> query, Pager pager) {
        if (!Pager.ALL_RECORDS.equals(pager)) {
            query.setFirstResult(pager.getOffset());
            query.setMaxResults(pager.getLimit());
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

    private TypedQuery<T> getEntityQuery(String queryString, Map<String, Object> parameters) {
        TypedQuery<T> query = em.createQuery(queryString, entityClass);
        setParameters(query, parameters);
        return query;
    }

    private Query getQuery(String queryString, Map<String, Object> parameters) {
        Query query = em.createQuery(queryString);
        setParameters(query, parameters);
        return query;
    }
    
    private void setParameters(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> p: parameters.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
        }
    }

}
