package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface CompanyRepository {

    Company get(long id);
    
    Company find(String name);
    
    List<Company> findAll(int offset, int limit);

    List<Company> findAll(User admin, int offset, int limit);
    
    int getCount();

    void add(Company company);

    void remove(Company company);

}
