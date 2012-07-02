package com.redwerk.likelabs.infrastructure.persistence.jpa;

import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.post.Post;
import com.redwerk.likelabs.domain.model.post.PostRepository;

import java.util.List;

public class PostJpaRepository implements PostRepository {

    @Override
    public List<Post> findAll(Company company) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
