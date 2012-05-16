package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import com.redwerk.likelabs.domain.model.tablet.exception.TabletNotFoundException;
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

    private EntityJpaRepository<Tablet, Long> entityRepository;

    @Override
    public Tablet get(long id) {
        Tablet tablet = getEntityRepository().findById(id);
        if (tablet == null) {
            throw new TabletNotFoundException(id);
        }
        return tablet;
    }

    @Override
    public Tablet find(String login) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("login", login);
        return getEntityRepository().findSingleEntity(GET_TABLET_BY_LOGIN, parameters);
    }

    @Override
    public List<Tablet> findAll(Point point, int offset, int limit) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("pointId", point.getId());
        return getEntityRepository().findEntityList(GET_TABLETS_FOR_POINT, parameters, offset, limit);
    }

    @Override
    public void add(Tablet tablet) {
        getEntityRepository().add(tablet);
    }

    @Override
    public void remove(Tablet tablet) {
        getEntityRepository().remove(tablet);
    }

    private EntityJpaRepository<Tablet, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Tablet, Long>(em, Tablet.class);
        }
        return entityRepository;
    }

}
