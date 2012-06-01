package com.redwerk.likelabs.domain.model.company;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;

import java.util.List;

public interface CompanyRepository {

    Company get(long id);
    
    Company find(String name);
    

    List<Company> findAll(Pager pager);

    List<Company> findForAdmin(User admin, Pager pager);

    List<Company> findForClient(User client, Pager pager);


    int getCount();

    int getCountForAdmin(User admin);

    int getCountForClient(User client);


    void add(Company company);

    void remove(Company company);

}
