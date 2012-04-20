package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.dto.CompanyData;
import com.redwerk.likelabs.domain.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Override
    public Company getCompany(long companyId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Company getCompany(String companyName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Company> getAllCompanies() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Company createCompany(CompanyData companyData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteCompany(long companyId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCompany(long companyId, CompanyData companyData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
