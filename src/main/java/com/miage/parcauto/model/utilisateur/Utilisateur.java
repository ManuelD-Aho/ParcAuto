package main.java.com.miage.parcauto.model.utilisateur;

import main.java.com.miage.parcauto.model.rh.Personnel;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant un utilisateur du système.
 */
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String login;
    private String hash;
    private Role role;
    private Personnel personnel;  // Personnel associé à cet utilisateur (facultatif)
    private String mfaSecret;     // Secret pour l'authentification à deux facteurs

    public Utilisateur() {
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return login + " (" + role + ")";
    }
}