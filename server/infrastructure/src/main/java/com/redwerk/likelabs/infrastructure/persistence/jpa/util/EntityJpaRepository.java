package com.redwerk.likelabs.infrastructure.persistence.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public class EntityJpaRepository<T> {

    private final EntityManager em;

    private final Class<T> entityClass;


    @SuppressWarnings("unchecked")
    public EntityJpaRepository(EntityManager em) {
        this.em = em;
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public T findSingleEntity(String queryString, Map<String, Object> parameters) {
        try {
            Query query = em.createQuery(queryString);
            for (Map.Entry<String, Object> p: parameters.entrySet()) {
                query.setParameter(p.getKey(), p.getValue());
            }
            return (T) query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<T> findEntityList(String queryString) {
        return em.createQuery(queryString, entityClass).getResultList();
    }

    public void add(T entity) {
        em.persist(entity);
        em.flush();
        em.refresh(entity);
    }

    public void remove(Object entity) {
        em.remove(entity);
        em.flush();
    }

    public void remove(Object[] entities) {
        for (Object e: entities) {
           em.remove(e);
        }
        em.flush();
    }

}
