package com.redwerk.likelabs.domain.model.tablet;

import com.redwerk.likelabs.domain.model.point.Point;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
public class Tablet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String loginPassword;

    private String logoutPassword;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;


    // constructors

    public Tablet(Point point, String login, String loginPassword, String logoutPassword) {
        this.point = point;
        this.login = login;
        this.loginPassword = loginPassword;
        this.logoutPassword = logoutPassword;
    }

    // accessors

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public String getLogoutPassword() {
        return logoutPassword;
    }

    // modifiers

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public void setLogoutPassword(String logoutPassword) {
        this.logoutPassword = logoutPassword;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tablet other = (Tablet) obj;
        return new EqualsBuilder()
                .append(login, other.login)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(login)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("login", login)
                .append("loginPassword", loginPassword)
                .append("logoutPassword", logoutPassword)
                .append("point", point)
                .toString();
    }

    // interface for JPA

    protected Tablet() {
    }

}
