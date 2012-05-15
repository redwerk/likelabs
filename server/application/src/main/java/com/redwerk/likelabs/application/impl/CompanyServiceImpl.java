package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.dto.CompanyAdminData;
import com.redwerk.likelabs.application.dto.CompanyData;
import com.redwerk.likelabs.application.dto.CompanyExtendedData;
import com.redwerk.likelabs.application.dto.Pager;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public List<CompanyExtendedData> getCompanies(Pager pager) {
        return getExtCompanies(companyRepository.findAll(pager.getOffset(), pager.getCount()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyExtendedData> getCompanies(long adminId, Pager pager) {
        User admin = userRepository.get(adminId);
        return getExtCompanies(companyRepository.findAll(admin, pager.getOffset(), pager.getCount()));
    }

    private List<CompanyExtendedData> getExtCompanies(List<Company> companies) {
        List<CompanyExtendedData> extCompanies = new ArrayList<CompanyExtendedData>(companies.size());
        for (Company c: companies) {
            // TODO: make separate queries
            int pointsNum = pointRepository.findAll(c, -1, -1).size();
            int reviewsNum = reviewRepository.findAll(c, -1, -1).size();
            extCompanies.add(new CompanyExtendedData(c, pointsNum, reviewsNum));
        }
        return extCompanies;
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompany(long companyId) {
        return getLoadedCompany(companyRepository.get(companyId));
    }
    
    private Company getLoadedCompany(Company company) {
        company.getSocialPages();
        company.getAdmins();
        company.getSampleReviews();
        return company;
    }

    @Override
    @Transactional
    public Company createCompany(CompanyData companyData, List<String> pageUrls, List<CompanyAdminData> admins, List<Point> points) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void updateCompany(long companyId, CompanyData companyData) {
        Company company = companyRepository.get(companyId);
        company.setName(companyData.getName());
        company.setPhone(companyData.getPhone());
        company.setEmail(companyData.getEmail());
    }

    @Override
    @Transactional
    public CompanySocialPage attachPage(long companyId, String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void detachPage(long companyId, String pageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void createAdmin(long companyId, CompanyAdminData admin) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional
    public void removeAdmin(long companyId, long adminId) {
        Company company = companyRepository.get(companyId);
        User admin = userRepository.get(adminId);
        company.removeAdmin(admin);
    }

    @Override
    @Transactional
    public void deleteCompany(long companyId) {
        companyRepository.remove(companyRepository.get(companyId));
    }
}
