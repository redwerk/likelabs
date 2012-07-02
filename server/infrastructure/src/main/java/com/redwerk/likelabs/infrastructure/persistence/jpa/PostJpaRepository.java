package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.post.Post;
import com.redwerk.likelabs.domain.model.post.PostRepository;
import com.redwerk.likelabs.domain.model.post.PostTypeFilter;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.infrastructure.persistence.jpa.util.EntityJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostJpaRepository implements PostRepository {

    private static final String POSTS_QUERY =
            "select p from %s p where p.review.point.company.id = :companyId order by p.created desc";

    @PersistenceContext
    private EntityManager em;

    private EntityJpaRepository<Post, Long> entityRepository;

    private static final Map<PostTypeFilter, String> entityNames = new HashMap<PostTypeFilter, String>() {{
        put(PostTypeFilter.ALL, "Post");
        put(PostTypeFilter.EMAIL, "EmailPost");
        put(PostTypeFilter.SN_WALL, "SNPost");
    }};


    @Override
    public List<Post> findAll(Company company, PostTypeFilter postType) {
        List<Post> res =  getEntityRepository().findEntityList(String.format(POSTS_QUERY, entityNames.get(postType)),
                Collections.<String, Object>singletonMap("companyId", company.getId()),
                Pager.ALL_RECORDS);
        return res;
    }

    @Override
    public void add(Post post) {
        getEntityRepository().add(post);
    }

    @Override
    public void remove(Post post) {
        getEntityRepository().remove(post);
    }

    private EntityJpaRepository<Post, Long> getEntityRepository() {
        if (entityRepository == null) {
            entityRepository = new EntityJpaRepository<Post, Long>(em, Post.class);
        }
        return entityRepository;
    }

}
