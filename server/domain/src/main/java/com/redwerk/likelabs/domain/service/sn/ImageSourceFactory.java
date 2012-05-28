package com.redwerk.likelabs.domain.service.sn;

import com.redwerk.likelabs.domain.model.photo.Photo;

public interface ImageSourceFactory {
    
    ImageSource createImageSource(Photo photo);

}
