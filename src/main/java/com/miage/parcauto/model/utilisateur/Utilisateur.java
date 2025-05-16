package main.java.com.miage.parcauto.model.utilisateur;

// import main.java.com.miage.parcauto.model.utilisateur.Role;

public class Utilisateur {

    private Integer id; // SQL: id
    private String login;
    private String hashMotDePasse; // SQL: hash
    private Role role;
    private Integer idPersonnel;
    private String mfaSecret;

    public Utilisateur() {
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

    public String getHashMotDePasse() {
        return hashMotDePasse;
    }

    public void setHashMotDePasse(String hashMotDePasse) {
        this.hashMotDePasse = hashMotDePasse;
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

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }
}