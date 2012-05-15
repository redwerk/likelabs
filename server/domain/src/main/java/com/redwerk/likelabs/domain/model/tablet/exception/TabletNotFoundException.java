package com.redwerk.likelabs.domain.model.tablet.exception;

public class TabletNotFoundException extends RuntimeException {

    private final long tabletId;

    public TabletNotFoundException(long tabletId) {
        this.tabletId = tabletId;
    }

    public long getTabletId() {
        return tabletId;
    }

}
