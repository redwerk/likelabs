package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.service.sn.ImageSource;
import com.redwerk.likelabs.domain.service.sn.ImageSourceFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageSourceFactoryImpl implements ImageSourceFactory {

    @Override
    public ImageSource createImageSource(Photo photo) {
        // TODO: add implementation
        return null;
    }

}
