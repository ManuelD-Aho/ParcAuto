package main.java.com.miage.parcauto.dto;

import java.io.Serializable;

/**
 * DTO pour l'entité Utilisateur.
 */
public class UtilisateurDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String login;
    private Integer idPersonnel;
    private String personnelInfo;
    private String role;
    private boolean hasMfa;

    public UtilisateurDTO() {
    }

    // Getters et Setters
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isHasMfa() {
        return hasMfa;
    }

    public void setHasMfa(boolean hasMfa) {
        this.hasMfa = hasMfa;
    }

    @Override
    public String toString() {
        return login + " (" + role + ")";
    }
}