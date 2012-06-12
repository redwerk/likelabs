package com.redwerk.likelabs.application;

import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.application.dto.company.CompanyPageData;
import com.redwerk.likelabs.application.dto.point.PointData;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.Point;

import java.util.List;

public interface CompanyService {

    Report<CompanyReportItem> getCompanies(Pager pager);

    Report<CompanyReportItem> getCompaniesForAdmin(long adminId, Pager pager);
    
    List<Company> getCompaniesForClient(long clientId);

    int getCompaniesCount();

    Company getCompany(long companyId);

    Company getCompanyForTablet(long tabletId);


    Company createCompany(long creatorId, CompanyData companyData, byte[] logo, List<CompanyPageData> pages,
                          List<CompanyAdminData> admins, List<PointData> points);

    Company createCompany(long creatorId, CompanyData companyData, CompanyAdminData admin);

    
    void updateCompany(long companyId, CompanyData companyData, byte[] logo);

    void updateCompany(long companyId, CompanyData companyData);
    

    CompanySocialPage attachPage(long companyId, CompanyPageData pageData);

    void detachPage(long companyId, String pageId);
    

    void createAdmin(long companyId, CompanyAdminData adminData);
    
    void removeAdmin(long companyId, long adminId);
    
    
    void deleteCompany(long companyId);

}
