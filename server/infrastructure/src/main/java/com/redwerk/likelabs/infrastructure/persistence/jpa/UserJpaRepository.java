package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserJpaRepository extends UserRepository {

    private static final String GET_ALL_USERS = "select u from User u join u.accounts a order by a.name, u.phone";


    private static final String REGULAR_USERS_FILTER =
            "from User u where u.systemAdmin = false and not exists (select a.id from Company c join c.admins a where a.id = u.id)";

    private static final String GET_REGULAR_USERS = "select u " + REGULAR_USERS_FILTER + " order by u.phone";

    private static final String GET_REGULAR_USERS_COUNT = "select count(u) " + REGULAR_USERS_FILTER;


    private static final String GET_USER_BY_PHONE = "select u from User u where u.phone = :phone";

    private static final String GET_USER_BY_SOCIAL_ACCOUNT =
            "select distinct u from User u join u.accounts a where a.type = :snType and a.accountId = :accountId";

    private static final String GET_CLIENTS_FOR_POINT =
            "select u from User u where exists (select r.id from Review r where r.point.id = :pointId and r.author.id = u.id)";

    private static final String GET_USERS_WITHOUT_EMAIL = "select u from User u where u.email is null";

    @PersistenceContext
    private EntityManager em;
    
    private EntityJpaRepository<User, Long> entityRepository;

    @Override
    protected User findInternal(long id) {
        return getEntityRepository().findById(id);
    }

    @Override
    protected User findInternal(SocialNetworkType snType, String accountId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("snType", snType);
        parameters.put("accountId", accountId);
        return getEntityRepository().findSingleEntity(GET_USER_BY_SOCIAL_ACCOUNT, parameters);
    }
    
    @Override
    public User find(String phone) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("phone", phone);
        return getEntityRepository().findSingleEntity(GET_USER_BY_PHONE, parameters);
    }

    @Override
    public List<User> findAll(Pager pager) {
        return getEntityRepository().findEntityList(GET_ALL_USERS, pager);
    }

    @Override
    public List<User> findRegular(Pager pager) {
        return getEntityRepository().findEntityList(GET_REGULAR_USERS, pager);
    }

    @Override
    public List<User> findClients(Point point) {
        return getEntityRepository().findEntityList(
                GET_CLIENTS_FOR_POINT,
                Collections.<String, Object>singletonMap("pointId", point.getId()),
                Pager.ALL_RECORDS);
    }

    @Override
    public List<User> findUsersWithoutEmail() {
        return getEntityRepository().findEntityList(GET_USERS_WITHOUT_EMAIL, Pager.ALL_RECORDS);
    }

    @Override
    public int getCount() {
        return getEntityRepository().getCount();
    }

    @Override
    public int getRegularCount() {
        return getEntityRepository().getCount(GET_REGULAR_USERS_COUNT);
    }

    @Override
    public void addInternal(User user) {
        getEntityRepository().add(user);
    }

    @Override
    protected void removeInternal(User user) {
        getEntityRepository().remove(user);
    }

    private EntityJpaRepository<User, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<User, Long>(em, User.class);
        }
        return entityRepository;
    }

}
