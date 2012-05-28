package com.redwerk.likelabs.web.rest.dto.utils;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.redwerk.likelabs.domain.service.dto.PhotoData;
import com.redwerk.likelabs.domain.service.dto.RecipientData;
import com.redwerk.likelabs.web.rest.dto.exposable.PhotoDataExposable;
import com.redwerk.likelabs.web.rest.dto.exposable.RecipientDataExposable;

public class DataConverter {

    public static List<PhotoData> convertPhotos(Collection<PhotoDataExposable> photos) {
        List<PhotoData> result = Lists.newArrayList();
        if(photos != null) {
            for(PhotoDataExposable photo : photos) {
                result.add(new PhotoData(photo.getStatus(), photo.getImage()));
            }
        }
        return result;
    }
    
    public static List<RecipientData> convertRecipients(Collection<RecipientDataExposable> recipients) {
        List<RecipientData> result = Lists.newArrayList();
        if(recipients != null) {
            for(RecipientDataExposable recipient : recipients) {
                result.add(new RecipientData(recipient.getType(), recipient.getAddress()));
            }           
        }
        return result;
    }
}
