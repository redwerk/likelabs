package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoNotFoundException;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
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
public class PhotoJpaRepository implements PhotoRepository {

    private static final String USER_AND_STATUS_FILTER =
            "from Photo p where p.user.id = :userId and p.status = :status";

    private static final String GET_PHOTOS_BY_USER_AND_STATUS =
            "select p " + USER_AND_STATUS_FILTER + " order by p.createdDT desc, p.id";

    private static final String GET_PHOTOS_COUNT_BY_USER_AND_STATUS =
            "select count(p) " + USER_AND_STATUS_FILTER;


    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Photo, Long> entityRepository;

    @Override
    public Photo get(long id) {
        Photo photo = getEntityRepository().findById(id);
        if (photo == null) {
            throw new PhotoNotFoundException(id);
        }
        return photo;
    }

    @Override
    public List<Photo> findAll(User user, PhotoStatus status, Pager pager) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user.getId());
        parameters.put("status", status);
        return getEntityRepository().findEntityList(GET_PHOTOS_BY_USER_AND_STATUS, parameters, pager);
    }

    @Override
    public int getCount(User user, PhotoStatus status) {
        return getEntityRepository().getCount(GET_PHOTOS_COUNT_BY_USER_AND_STATUS);
    }

    @Override
    public void add(Photo photo) {
        getEntityRepository().add(photo);
    }

    @Override
    public void remove(Photo photo) {
        getEntityRepository().remove(photo);
    }

    private EntityJpaRepository<Photo, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Photo, Long>(em, Photo.class);
        }
        return entityRepository;
    }

}
