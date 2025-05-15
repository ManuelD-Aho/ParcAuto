package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.utilisateur.Role;

import java.io.Serializable;

public class UtilisateurDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String login;
    private Role role;
    private Integer idPersonnel;
    private String personnelInfo;
    private boolean mfaEnabled;

    public UtilisateurDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getPersonnelInfo() {
        return personnelInfo;
    }

    public void setPersonnelInfo(String personnelInfo) {
        this.personnelInfo = personnelInfo;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    @Override
    public String toString() {
        return "UtilisateurDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + (role != null ? role.getValeur() : "N/A") +
                ", personnelInfo='" + personnelInfo + '\'' +
                '}';
    }
}