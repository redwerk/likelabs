package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.application.dto.Pager;
import com.redwerk.likelabs.application.dto.TabletData;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TabletServiceImpl implements TabletService {

    @Override
    public List<Tablet> getTablets(long pointId, Pager pager) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tablet getTablet(long tabletId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tablet createTablet(long pointId, TabletData tabletData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTablet(long tabletId, TabletData tabletData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteTablet(long tabletId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
