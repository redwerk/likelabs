package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.company.exception.CompanyNotFoundException;
import com.redwerk.likelabs.domain.model.query.Pager;
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

    private static final String ADMIN_FILTER = "from Company c join c.admins a where a.id = :adminId";

    private static final String GET_COMPANIES_BY_ADMIN = "select c " + ADMIN_FILTER + " order by c.name";

    private static final String GET_COMPANIES_COUNT_BY_ADMIN = "select count(c) " + ADMIN_FILTER;

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Company, Long> entityRepository;


    @Override
    public Company get(long id) {
        Company company = getEntityRepository().findById(id);
        if (company == null) {
            throw new CompanyNotFoundException(id);
        }
        return company;
    }

    @Override
    public Company find(String name) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        return getEntityRepository().findSingleEntity(GET_COMPANY_BY_NAME, parameters);
    }

    @Override
    public List<Company> findAll(Pager pager) {
        return getEntityRepository().findEntityList(GET_ALL_COMPANIES, pager);
    }

    @Override
    public List<Company> findAll(User admin, Pager pager) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("adminId", admin.getId());
        return getEntityRepository().findEntityList(GET_COMPANIES_BY_ADMIN, parameters, pager);
    }

    @Override
    public int getCount() {
        return getEntityRepository().getCount();
    }

    @Override
    public int getCount(User admin) {
        return getEntityRepository().getCount(GET_COMPANIES_COUNT_BY_ADMIN);
    }

    @Override
    public void add(Company company) {
        getEntityRepository().add(company);
    }

    @Override
    public void remove(Company company) {
        getEntityRepository().remove(company);
    }

    private EntityJpaRepository<Company, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Company, Long>(em, Company.class);
        }
        return entityRepository;
    }

}
