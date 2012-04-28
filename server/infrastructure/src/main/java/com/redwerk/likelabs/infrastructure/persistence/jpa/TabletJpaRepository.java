package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TabletJpaRepository implements TabletRepository {

    private static final String GET_TABLETS_FOR_POINT =
            "select t from Tablet t where t.point.id = :pointId order by t.login";

    private static final String GET_TABLET_BY_LOGIN = "select t from Tablet t where t.login = :login";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Tablet> entityRepository;

    @Override
    public Tablet find(Long id) {
        return getEntityRepository().findById(id);
    }

    @Override
    public Tablet find(String login) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("login", login);
        return getEntityRepository().findSingleEntity(GET_TABLET_BY_LOGIN, parameters);
    }

    @Override
    public List<Tablet> findAll(Point point) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pointId", point.getId());
        return getEntityRepository().findEntityList(GET_TABLETS_FOR_POINT, parameters);
    }

    @Override
    public void add(Tablet tablet) {
        getEntityRepository().add(tablet);
    }

    @Override
    public void remove(Tablet tablet) {
        getEntityRepository().remove(tablet);
    }

    private EntityJpaRepository<Tablet> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Tablet>(em);
        }
        return entityRepository;
    }

}
