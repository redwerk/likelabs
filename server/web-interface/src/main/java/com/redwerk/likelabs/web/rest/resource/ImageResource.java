package com.redwerk.likelabs.web.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

public class ImageResource {
    
    private final byte[] image;
    
    
    public ImageResource(byte[] image) {
        this.image = image;
    }
    
    @GET 
    @Produces({"image/jpeg", "image/png", "image/gif", "image/tiff"})
    public byte[] getImage() {
        return image;
    }
}
