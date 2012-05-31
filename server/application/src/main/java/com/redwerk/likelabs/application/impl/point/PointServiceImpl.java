package com.redwerk.likelabs.application.impl.point;

import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.point.PointData;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    @Transactional(readOnly = true)
    public Report<Point> getPoints(long companyId, Pager pager) {
        Company company = companyRepository.get(companyId);
        return new Report<Point>(
                pointRepository.findAll(company, pager),
                pointRepository.getCount(company));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Point> getPoints(long companyId, long clientId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public Point getPoint(long pointId) {
        return pointRepository.get(pointId);
    }

    @Override
    @Transactional
    public Point createPoint(long companyId, PointData pointData) {
        if (pointData == null) {
            throw new IllegalArgumentException("pointData cannot be null");
        }
        PointFactory factory = new PointFactory(pointRepository);
        return factory.createPoint(companyRepository.get(companyId), pointData);
    }

    @Override
    @Transactional
    public void updatePoint(long pointId, PointData pointData) {
        Point point = pointRepository.get(pointId);
        point.setAddress(pointData.getAddress());
        point.setPhone(pointData.getPhone());
        point.setEmail(pointData.getEmail());
    }

    @Override
    @Transactional
    public void deletePoint(long pointId) {
        Point point = pointRepository.get(pointId);
        pointRepository.remove(point);
    }

}
