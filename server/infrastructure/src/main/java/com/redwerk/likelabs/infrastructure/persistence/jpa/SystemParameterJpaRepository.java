package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.settings.SystemParameter;
import com.redwerk.likelabs.domain.model.settings.SystemParameterRepository;
import com.redwerk.likelabs.domain.model.settings.SystemParameterType;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SystemParameterJpaRepository implements SystemParameterRepository {

    private static final String GET_ALL_PARAMETERS = "select p from SystemParameter p";

    private static final String GET_PARAMETER_BY_TYPE = "select p from SystemParameter p where p.type = :type";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<SystemParameter> entityRepository;


    @Override
    public SystemParameter find(SystemParameterType type) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", type);
        return getEntityRepository().findSingleEntity(GET_PARAMETER_BY_TYPE, parameters);
    }

    @Override
    public List<SystemParameter> findAll() {
        return getEntityRepository().findEntityList(GET_ALL_PARAMETERS);
    }

    @Override
    public void add(SystemParameter parameter) {
        getEntityRepository().add(parameter);
    }

    @Override
    public void remove(SystemParameter parameter) {
        getEntityRepository().remove(parameter);
    }

    private EntityJpaRepository<SystemParameter> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<SystemParameter>(em);
        }
        return entityRepository;
    }

}
