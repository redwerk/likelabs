package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface CompanyRepository {

    Company find(Long id);
    
    Company find(String name);
    
    List<Company> findAll();

    List<Company> findAll(User admin);

    void add(Company company);

    void remove(Company company);

}
