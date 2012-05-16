package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface CompanyRepository {

    Company get(long id);
    
    Company find(String name);
    
    List<Company> findAll(Pager pager);

    List<Company> findAll(User admin, Pager pager);
    
    int getCount();

    int getCount(User admin);

    void add(Company company);

    void remove(Company company);

}
