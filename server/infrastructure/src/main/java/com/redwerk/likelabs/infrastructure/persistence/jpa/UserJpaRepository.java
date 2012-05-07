package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserJpaRepository implements UserRepository {

    private static final String GET_ALL_USERS = "select u from User u order by u.phone";

    private static final String GET_USER_BY_PHONE = "select u from User u where u.phone = :phone";

    @PersistenceContext
    private EntityManager em;
    
    private EntityJpaRepository<User> entityRepository;

    @Override
    public User find(Long id) {
      return getEntityRepository().findById(User.class, id);
    }

    @Override
    public User find(String phone) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("phone", phone);
        return getEntityRepository().findSingleEntity(GET_USER_BY_PHONE, parameters);
    }

    @Override
    public List<User> findAll() {
        return getEntityRepository().findEntityList(GET_ALL_USERS);
    }

    @Override
    public void add(User user) {
        getEntityRepository().add(user);
    }

    @Override
    public void remove(User user) {
        getEntityRepository().remove(user);
    }

    private EntityJpaRepository<User> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<User>(em);
        }
        return entityRepository;
    }

}
