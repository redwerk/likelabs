package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.CompanyAdminData;
import com.redwerk.likelabs.application.dto.CompanyData;
import com.redwerk.likelabs.application.dto.CompanyExtendedData;
import com.redwerk.likelabs.application.dto.Pager;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.Point;

import java.util.List;

public interface CompanyService {

    List<CompanyExtendedData> getCompanies(Pager pager);
    
    List<CompanyExtendedData> getCompanies(long adminId, Pager pager);
    
    Company getCompany(long companyId);
    
    
    Company createCompany(CompanyData companyData, List<String> pageUrls,
            List<CompanyAdminData> admins, List<Point> points);
    
    void updateCompany(long companyId, CompanyData companyData);
    
    CompanySocialPage attachPage(long companyId, String url);
    
    void detachPage(long companyId, String pageId);
    
    void createAdmin(long companyId, CompanyAdminData admin);
    
    void removeAdmin(long companyId, String userId);
    
    
    void deleteCompany(long companyId);

}
