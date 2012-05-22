package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.Point;

import java.util.List;

public interface CompanyService {

    Report<CompanyReportItem> getCompanies(Pager pager);

    Report<CompanyReportItem> getCompanies(long adminId, Pager pager);

    int getCompaniesCount();

    int getCompaniesCount(long adminId);

    Company getCompany(long companyId);

    Company getCompanyForTablet(long tabletId);

    
    Company createCompany(CompanyData companyData, List<String> pageUrls,
            List<CompanyAdminData> admins, List<Point> points);
    
    void updateCompany(long companyId, CompanyData companyData);
    
    CompanySocialPage attachPage(long companyId, SocialNetworkType snType, String url);
    
    void detachPage(long companyId, String pageId);
    
    void createAdmin(long companyId, CompanyAdminData adminData);
    
    void removeAdmin(long companyId, long adminId);
    
    
    void deleteCompany(long companyId);

}
