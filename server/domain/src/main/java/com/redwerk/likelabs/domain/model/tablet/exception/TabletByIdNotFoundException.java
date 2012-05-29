package com.redwerk.likelabs.domain.model.tablet.exception;

public class TabletByIdNotFoundException extends TabletNotFoundException {

    private final long id;

    public TabletByIdNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
