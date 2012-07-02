package com.redwerk.likelabs.domain.model.post;

import com.redwerk.likelabs.domain.model.company.Company;

import java.util.List;

public interface PostRepository {
    
    List<Post> findAll(Company company, PostTypeFilter postType);

    void add(Post post);
    
    void remove(Post post);
    
}
