package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Address;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PointJpaRepository implements PointRepository {

    private static final String GET_POINTS_FOR_COMPANY = "select p from Point p where p.company.id = :companyId";

    private static final String GET_ORDERED_POINTS_FOR_COMPANY = GET_POINTS_FOR_COMPANY + " order by p.address";

    private static final String GET_POINT_BY_ADDRESS = GET_POINTS_FOR_COMPANY + " and p.address = :address";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Point> entityRepository;


    @Override
    public Point find(Long id) {
        return getEntityRepository().findById(Point.class, id);
    }

    @Override
    public Point find(Company company, Address address) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("companyId", company.getId());
        parameters.put("address", address);
        return getEntityRepository().findSingleEntity(GET_POINT_BY_ADDRESS, parameters);
    }

    @Override
    public List<Point> findAll(Company company) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("companyId", company.getId());
        return getEntityRepository().findEntityList(GET_ORDERED_POINTS_FOR_COMPANY, parameters);
    }

    @Override
    public void add(Point point) {
        getEntityRepository().add(point);
    }

    @Override
    public void remove(Point point) {
        getEntityRepository().remove(point);
    }

    private EntityJpaRepository<Point> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Point>(em);
        }
        return entityRepository;
    }

}
