package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.application.dto.company.CompanyPageData;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.application.dto.point.PointData;
import com.redwerk.likelabs.application.impl.point.PointFactory;
import com.redwerk.likelabs.application.impl.registration.CodeGenerator;
import com.redwerk.likelabs.application.impl.registration.PasswordGenerator;
import com.redwerk.likelabs.application.messaging.EmailService;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.model.company.exception.PageNotFoundException;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserFactory;
import com.redwerk.likelabs.domain.model.user.UserRepository;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static final String APP_DOMAIN_MSG =  "app.domain";

    private static final String ADMIN_ACTIVATION_LINK_TEMPLATE = "{0}/companyadmin/activate?userId={1}&code={2}";

    private static final String ADMIN_REGISTRATION_SMS_MSG = "message.sms.registration";

    private static final String ADMIN_ACTIVATION_EMAIL_BODY = "message.email.registration.admin.body";
    private static final String ADMIN_ACTIVATION_EMAIL_SUBJECT = "message.email.registration.admin.subject";
    private static final String ADMIN_ACTIVATION_EMAIL_FROM = "message.email.registration.from";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TabletRepository tabletRepository;

    @Autowired
    GatewayFactory gatewayFactory;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private MessageTemplateService messageService;


    @Override
    @Transactional(readOnly = true)
    public Report<CompanyReportItem> getCompanies(Pager pager) {
        return new Report<CompanyReportItem>(
                getReportItems(companyRepository.findAll(pager)), companyRepository.getCount());
    }

    @Override
    @Transactional(readOnly = true)
    public Report<CompanyReportItem> getCompaniesForAdmin(long adminId, Pager pager) {
        User admin = userRepository.get(adminId);
        return new Report<CompanyReportItem>(
                getReportItems(companyRepository.findForAdmin(admin, pager)), companyRepository.getCountForAdmin(admin));
    }

    private List<CompanyReportItem> getReportItems(List<Company> companies) {
        List<CompanyReportItem> items = new ArrayList<CompanyReportItem>(companies.size());
        for (Company c: companies) {
            int pointsNum = pointRepository.getCount(c);
            int reviewsNum = reviewRepository.getQuery().setCompanyIds(Arrays.asList(c.getId())).getCount();
            items.add(new CompanyReportItem(c, pointsNum, reviewsNum));
        }
        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> getCompaniesForClient(long clientId) {
        User client = userRepository.get(clientId);
        return companyRepository.findForClient(client, Pager.ALL_RECORDS);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCompaniesCount() {
        return companyRepository.getCount();
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompany(long companyId) {
        return getLoadedCompany(companyRepository.get(companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public Company getCompanyForTablet(long tabletId) {
        Tablet tablet = tabletRepository.get(tabletId);
        return getLoadedCompany(tablet.getPoint().getCompany());
    }

    private Company getLoadedCompany(Company company) {
        company.getAdmins().size();
        company.getSampleReviews().size();
        return company;
    }

    @Override
    @Transactional
    public Company createCompany(long creatorId, CompanyData companyData, byte[] logo, List<CompanyPageData> pages,
                                 List<CompanyAdminData> admins, List<PointData> points) {
        User creator = userRepository.get(creatorId);
        Validate.isTrue(creator.isSystemAdmin(), "Only system administrators can create new company");

        Company company = new Company(companyData.getName(), companyData.getPhone(), companyData.getEmail());
        company.setLogo(logo);
        company.setModerateReviews(companyData.isModerateReviews());

        for (CompanyPageData pageData: pages) {
            attachPageInternal(company, pageData);
        }
        for (CompanyAdminData adminData: admins) {
            createAdminInternal(company, adminData);
        }
        PointFactory pointFactory = new PointFactory(pointRepository);
        for (PointData pointData: points) {
            pointFactory.createPoint(company, pointData);
        }

        companyRepository.add(company);
        return company;
    }

    @Override
    @Transactional
    public Company createCompany(long creatorId, CompanyData companyData, CompanyAdminData admin) {
        User creator = userRepository.get(creatorId);
        Validate.isTrue(creator.isSystemAdmin(), "Only system administrators can create new company");

        Company company = new Company(companyData.getName(), companyData.getPhone(), companyData.getEmail());
        company.setModerateReviews(companyData.isModerateReviews());

        createAdminInternal(company, admin);

        companyRepository.add(company);
        return company;
    }

    @Override
    @Transactional
    public void updateCompany(long companyId, CompanyData companyData, byte[] logo) {
        Company company = companyRepository.get(companyId);
        updateCompanyData(company, companyData);
        company.setLogo(logo);
    }

    @Override
    @Transactional
    public void updateCompany(long companyId, CompanyData companyData) {
        Company company = companyRepository.get(companyId);
        updateCompanyData(company, companyData);
    }

    private void updateCompanyData(Company company, CompanyData companyData) {
        company.setName(companyData.getName());
        company.setPhone(companyData.getPhone());
        company.setEmail(companyData.getEmail());
        company.setModerateReviews(companyData.isModerateReviews());
    }

    @Override
    @Transactional
    public CompanySocialPage attachPage(long companyId, CompanyPageData pageData) {
        if (pageData == null) {
            throw new IllegalArgumentException("pageData cannot be null");
        }
        return attachPageInternal(companyRepository.get(companyId), pageData);
    }

    private CompanySocialPage attachPageInternal(Company company, CompanyPageData pageData) {
        CompanySocialPage page = gatewayFactory.getGateway(pageData.getSnType()).getCompanyPage(pageData.getUrl());
        company.addSocialPage(page);
        return page;
    }

    @Override
    @Transactional
    public void detachPage(long companyId, String pageId) {
        Company company = companyRepository.get(companyId);
        for (CompanySocialPage page: company.getSocialPages()) {
            if (page.getPageId().equals(pageId)) {
                company.removeSocialPage(page);
                return;
            }
        }
        throw new PageNotFoundException(pageId);
    }

    @Override
    @Transactional
    public void createAdmin(long companyId, CompanyAdminData adminData) {
        createAdminInternal(companyRepository.get(companyId), adminData);
    }

    private void createAdminInternal(Company company, CompanyAdminData adminData) {
        User admin = createAdminUser(adminData.getPhone(), adminData.getEmail());
        company.addAdmin(admin);
        sendSmsWithPassword(admin);
        sendActivationEmail(admin);
    }

    private User createAdminUser(String phone, String email) {
        User user = new UserFactory().createUser(phone, passwordGenerator.getPassword(phone));
        user.setEmail(email);
        user.setPublishInSN(false);
        userRepository.add(user);
        return user;
    }
    
    private void sendSmsWithPassword(User receiver) {
        String msg = messageService.getMessage(
                ADMIN_REGISTRATION_SMS_MSG, messageService.getMessage(APP_DOMAIN_MSG), receiver.getPhone(), receiver.getPassword());
        smsService.sendMessage(receiver.getPhone(), msg);
    }
    
    private void sendActivationEmail(User receiver) {
        String email = receiver.getEmail();
        String activationLink = MessageFormat.format(ADMIN_ACTIVATION_LINK_TEMPLATE,
                messageService.getMessage(APP_DOMAIN_MSG), receiver.getId(),
                codeGenerator.getAdminActivationCode(receiver.getId(), email, receiver.getPhone()));
        emailService.sendMessage(email, messageService.getMessage(ADMIN_ACTIVATION_EMAIL_FROM),
                messageService.getMessage(ADMIN_ACTIVATION_EMAIL_SUBJECT),
                messageService.getMessage(ADMIN_ACTIVATION_EMAIL_BODY, activationLink));
    }

    @Override
    @Transactional
    public void removeAdmin(long companyId, long adminId) {
        Company company = companyRepository.get(companyId);
        User admin = userRepository.get(adminId);
        company.removeAdmin(admin, companyRepository, userRepository);
    }

    @Override
    @Transactional
    public void deleteCompany(long companyId) {
        companyRepository.remove(companyRepository.get(companyId));
    }
}
