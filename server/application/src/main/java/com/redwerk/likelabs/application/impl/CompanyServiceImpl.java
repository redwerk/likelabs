package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.dto.CompanyAdminData;
import com.redwerk.likelabs.application.dto.CompanyData;
import com.redwerk.likelabs.application.dto.CompanyExtendedData;
import com.redwerk.likelabs.application.dto.Pager;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Override
    public List<CompanyExtendedData> getCompanies(Pager pager) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<CompanyExtendedData> getCompanies(long adminId, Pager pager) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Company getCompany(long companyId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Company createCompany(CompanyData companyData, List<String> pageUrls, List<CompanyAdminData> admins, List<Point> points) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCompany(long companyId, CompanyData companyData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CompanySocialPage attachPage(long companyId, String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void detachPage(long companyId, String pageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createAdmin(long companyId, CompanyAdminData admin) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeAdmin(long companyId, String userId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteCompany(long companyId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
