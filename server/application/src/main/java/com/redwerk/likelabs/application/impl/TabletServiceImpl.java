package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.application.dto.TabletData;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.point.PointRepository;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.domain.model.tablet.TabletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TabletServiceImpl implements TabletService {

    @Autowired
    private TabletRepository tabletRepository;

    @Autowired
    private PointRepository pointRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Tablet> getTablets(long pointId, Pager pager) {
        Point point = pointRepository.get(pointId);
        return tabletRepository.findAll(point, pager);
    }

    @Override
    @Transactional(readOnly = true)
    public Tablet getTablet(long tabletId) {
        return  tabletRepository.get(tabletId);
    }

    @Override
    @Transactional
    public Tablet createTablet(long pointId, TabletData tabletData) {
        Point point = pointRepository.get(pointId);
        Tablet tablet = new Tablet(point, tabletData.getLogin(), tabletData.getLoginPassword(), tabletData.getLogoutPassword());
        tabletRepository.add(tablet);
        return tablet;
    }

    @Override
    @Transactional
    public void updateTablet(long tabletId, TabletData tabletData) {
        Tablet tablet = tabletRepository.get(tabletId);
        tablet.setLoginPassword(tabletData.getLoginPassword());
        tablet.setLogoutPassword(tabletData.getLogoutPassword());
    }

    @Override
    @Transactional
    public void deleteTablet(long tabletId) {
        Tablet tablet = tabletRepository.get(tabletId);
        tabletRepository.remove(tablet);
    }

    @Override
    public Tablet getTablet(String login, String loginPassword) {
        Tablet tablet = tabletRepository.find(login);
        return (tablet != null && tablet.getLoginPassword().equals(loginPassword)) ? tablet : null;
    }

    @Override
    public boolean canLogout(long tabletId, String logoutPassword) {
        Tablet tablet = tabletRepository.get(tabletId);
        return tablet.getLogoutPassword().equals(logoutPassword);
    }

    @Override
    public boolean apiKeyIsValid(long tabletId, String apiKey) {
        Tablet tablet = tabletRepository.get(tabletId);
        return tablet.getApiKey().equals(apiKey);
    }

}
