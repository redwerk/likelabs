package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import com.redwerk.likelabs.domain.model.tablet.exception.TabletByApiKeyNotFoundException;
import com.redwerk.likelabs.domain.model.tablet.exception.TabletByIdNotFoundException;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TabletJpaRepository implements TabletRepository {

    private static final String POINT_FILTER = "from Tablet t where t.point.id = :pointId";

    private static final String GET_TABLETS_FOR_POINT = "select t " + POINT_FILTER + " order by t.login";

    private static final String GET_TABLETS_COUNT_FOR_POINT = "select count(t) " + POINT_FILTER;

    private static final String GET_TABLET_BY_LOGIN = "select t from Tablet t where t.login = :login";

    private static final String GET_TABLET_BY_API_KEY = "select t from Tablet t where t.apiKey = :apiKey";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Tablet, Long> entityRepository;

    @Override
    public Tablet get(long id) {
        Tablet tablet = getEntityRepository().findById(id);
        if (tablet == null) {
            throw new TabletByIdNotFoundException(id);
        }
        return tablet;
    }

    @Override
    public Tablet get(String apiKey) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("apiKey", apiKey);
        Tablet tablet = getEntityRepository().findSingleEntity(GET_TABLET_BY_API_KEY, parameters);
        if (tablet == null) {
            throw new TabletByApiKeyNotFoundException(apiKey);
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
    public List<Tablet> findAll(Point point, Pager pager) {
        return getEntityRepository().findEntityList(
                GET_TABLETS_FOR_POINT,
                Collections.<String, Object>singletonMap("pointId", point.getId()),
                pager);
    }

    @Override
    public int getCount(Point point) {
        return getEntityRepository().getCount(
                GET_TABLETS_COUNT_FOR_POINT,
                Collections.<String, Object>singletonMap("pointId", point.getId()));
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
