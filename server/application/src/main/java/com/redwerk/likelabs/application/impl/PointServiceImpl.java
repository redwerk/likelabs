package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.dto.Pager;
import com.redwerk.likelabs.application.dto.PointData;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanyRepository;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Point> getPoints(long companyId, Pager pager) {
        Company company = companyRepository.get(companyId);
        return pointRepository.findAll(company, pager.getOffset(), pager.getCount());
    }

    @Override
    @Transactional(readOnly = true)
    public Point getPoint(long pointId) {
        return pointRepository.get(pointId);
    }

    @Override
    @Transactional
    public Point createPoint(long companyId, PointData pointData) {
        Company company = companyRepository.get(companyId);
        Point point = new Point(company, pointData.getAddress(), pointData.getPhone(), pointData.getEmail());
        pointRepository.add(point);
        return point;
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
