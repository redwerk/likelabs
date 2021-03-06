package com.redwerk.likelabs.web.ui.dto;



public class TabletDto {

    private Long id;
    private String login;
    private String loginPassword;
    private String logoutPassword;

    public TabletDto(Long id, String login, String loginPassword, String logoutPassword) {
        this.id = id;
        this.login = login;
        this.loginPassword = loginPassword;
        this.logoutPassword = logoutPassword;
    }

    public TabletDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLogoutPassword() {
        return logoutPassword;
    }

    public void setLogoutPassword(String logoutPassword) {
        this.logoutPassword = logoutPassword;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TabletDto other = (TabletDto) obj;
        if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.login != null ? this.login.hashCode() : 0);
        return hash;
    }
}
