package com.redwerk.likelabs.application;

import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.application.dto.TabletData;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import java.util.List;

public interface TabletService {
    
    List<Tablet> getTablets(long pointId, Pager pager);
    
    Tablet getTablet(long tabletId);
    
    Tablet createTablet(long pointId, TabletData tabletData);
    
    void updateTablet(long tabletId, TabletData tabletData);
    
    void deleteTablet(long tabletId);
    
}
