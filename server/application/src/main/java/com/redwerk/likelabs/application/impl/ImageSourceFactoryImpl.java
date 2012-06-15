package com.redwerk.likelabs.application.impl;

import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.service.sn.ImageSource;
import com.redwerk.likelabs.domain.service.sn.ImageSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ImageSourceFactoryImpl implements ImageSourceFactory {

    private static final String PHOTO_URL_TEMPLATE = "{0}/public/photo/{1}";

    private static final String APPLICATION_DOMAIN =  "app.domain";

    @Autowired
    private MessageTemplateService messageService;


    @Override
    public ImageSource createImageSource(final Photo photo) {
        return new ImageSource() {
            @Override
            public String getImageUrl() {
                return  MessageFormat.format(PHOTO_URL_TEMPLATE, messageService.getMessage(APPLICATION_DOMAIN), photo.getId());
            }

            @Override
            public byte[] getImageBytes() {
                return photo.getImage();
            }
        };
    }

}
