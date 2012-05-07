package com.redwerk.likelabs.domain.model.photo;

import com.redwerk.likelabs.domain.model.user.User;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    
    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDT;
    
    private PhotoStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // constructors

    public Photo(User user, byte[] image) {
        this.user = user;
        this.image = image;
        this.createdDT = new Date();
    }

    // accessors

    public Long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public Date getCreatedDT() {
        return createdDT;
    }

    public PhotoStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    // modifiers

    public void setStatus(PhotoStatus status) {
        this.status = status;
    }

    // overrides

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("status", status)
                .append("hasImage", image != null)
                .append("createdDT", createdDT)
                .append("user", user)
                .toString();
    }

    // interface for JPA

    protected Photo() {
    }

}
