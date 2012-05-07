package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoRepository;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
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

    private static final String GET_PHOTOS_BY_USER_AND_STATUS =
            "select p from Photo p where p.user.id = :userId and p.status = :status order by p.createdDT desc, p.id";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Photo> entityRepository;

    @Override
    public Photo find(Long id) {
        return getEntityRepository().findById(Photo.class, id);
    }

    @Override
    public List<Photo> findAll(User user, PhotoStatus status) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("userId", user.getId());
        parameters.put("status", status);
        return getEntityRepository().findEntityList(GET_PHOTOS_BY_USER_AND_STATUS, parameters);
    }

    @Override
    public void add(Photo photo) {
        getEntityRepository().add(photo);
    }

    @Override
    public void remove(Photo photo) {
        getEntityRepository().remove(photo);
    }

    private EntityJpaRepository<Photo> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Photo>(em);
        }
        return entityRepository;
    }

}
