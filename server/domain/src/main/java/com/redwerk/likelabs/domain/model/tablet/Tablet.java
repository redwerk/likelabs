package com.redwerk.likelabs.domain.model.tablet;

import com.redwerk.likelabs.domain.model.point.Point;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tablet")
public class Tablet {

    private static final int API_KEY_LENGTH = 32;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    @Column(name = "login_password")
    private String loginPassword;

    @Column(name = "logout_password")
    private String logoutPassword;

    @Column(name = "api_key")
    private String apiKey;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;


    // constructors

    public Tablet(Point point, String login, String loginPassword, String logoutPassword) {
        this.point = point;
        this.login = login;
        this.loginPassword = loginPassword;
        this.logoutPassword = logoutPassword;
        generateApiKey();
    }
    
    private void generateApiKey() {
        apiKey = RandomStringUtils.randomAlphanumeric(API_KEY_LENGTH);
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

    public String getApiKey() {
        return apiKey;
    }
    
    public Point getPoint() {
        return point;
    }

    // modifiers

    public void setLogin(String login) {
        generateApiKey();
        this.login = login;
    }

    public void setLoginPassword(String loginPassword) {
        generateApiKey();
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
                .append("apiKey", apiKey)
                .append("point", point)
                .toString();
    }

    // interface for JPA

    protected Tablet() {
    }

}
