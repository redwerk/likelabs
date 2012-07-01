package com.redwerk.likelabs.application.dto.statistics;

import java.util.Date;

public class SocialNetworkPost {
    
    private Date createDate;
    
    private Integer shares;
    
    private Integer comments;
    
    private Integer likes;
    
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Integer getShares() {
        return shares;
    }
    public void setShares(Integer shares) {
        this.shares = shares;
    }
    public Integer getComments() {
        return comments;
    }
    public void setComments(Integer comments) {
        this.comments = comments;
    }
    public Integer getLikes() {
        return likes;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    public SocialNetworkPost(){
        
    }
    
    public SocialNetworkPost(Date date, Integer shares, Integer comments, Integer likes){
        this.createDate = date;
        this.comments = comments;
        this.shares = shares;
        this.likes = likes;
    }
}
