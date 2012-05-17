package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Address;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import com.redwerk.likelabs.domain.model.point.exception.PointNotFoundException;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PointJpaRepository implements PointRepository {

   private static final String COMPANY_FILTER = "from Point p join p.addresses a where p.company.id = :companyId";

    private static final String GET_POINTS_FOR_COMPANY = "select p " + COMPANY_FILTER;

    private static final String GET_POINTS_COUNT_FOR_COMPANY = "select count(*) " + COMPANY_FILTER;

    private static final String GET_ORDERED_POINTS_FOR_COMPANY =
            GET_POINTS_FOR_COMPANY + " order by a.country, a.state, a.city, a.addressLine1, a.addressLine2";

    private static final String GET_POINT_BY_ADDRESS = GET_POINTS_FOR_COMPANY + " and a = :address"; 

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Point, Long> entityRepository;


    @Override
    public Point get(long id) {
        Point point = getEntityRepository().findById(id);
        if (point == null) {
            throw new PointNotFoundException(id);
        }
        return point;
    }

    @Override
    public Point find(Company company, Address address) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("companyId", company.getId());
        parameters.put("address", address);
        return getEntityRepository().findSingleEntity(GET_POINT_BY_ADDRESS, parameters);
    }

    @Override
    public List<Point> findAll(Company company, Pager pager) {
        return getEntityRepository().findEntityList(
                GET_ORDERED_POINTS_FOR_COMPANY,
                Collections.<String, Object>singletonMap("companyId", company.getId()),
                pager);
    }

    @Override
    public int getCount(Company company) {
        return getEntityRepository().getCount(
                GET_POINTS_COUNT_FOR_COMPANY,
                Collections.<String, Object>singletonMap("companyId", company.getId()));
    }

    @Override
    public void add(Point point) {
        getEntityRepository().add(point);
    }

    @Override
    public void remove(Point point) {
        getEntityRepository().remove(point);
    }

    private EntityJpaRepository<Point, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Point, Long>(em, Point.class);
        }
        return entityRepository;
    }

}
