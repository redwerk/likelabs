package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompanyJpaRepository implements CompanyRepository {

    private static final String GET_ALL_COMPANIES = "select c from Company c order by c.name";

    private static final String GET_COMPANY_BY_NAME = "select c from Company c where c.name = :name order by c.id";

    private static final String GET_COMPANIES_BY_ADMIN =
            "select c from Company c where c.admins.id = :adminId order by c.name";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Company> entityRepository;


    @Override
    public Company find(Long id) {
        return getEntityRepository().findById(Company.class, id);
    }

    @Override
    public Company find(String name) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        return getEntityRepository().findSingleEntity(GET_COMPANY_BY_NAME, parameters);
    }

    @Override
    public List<Company> findAll() {
        return getEntityRepository().findEntityList(GET_ALL_COMPANIES);
    }

    @Override
    public List<Company> findAll(User admin) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("adminId", admin.getId());
        return getEntityRepository().findEntityList(GET_COMPANIES_BY_ADMIN, parameters);
    }

    @Override
    public void add(Company company) {
        getEntityRepository().add(company);
    }

    @Override
    public void remove(Company company) {
        getEntityRepository().remove(company);
    }

    private EntityJpaRepository<Company> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Company>(em);
        }
        return entityRepository;
    }

}
