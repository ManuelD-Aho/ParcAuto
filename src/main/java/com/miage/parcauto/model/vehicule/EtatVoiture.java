package main.java.com.miage.parcauto.model.vehicule;

public class EtatVoiture {

    private Integer idEtatVoiture;
    private String libEtatVoiture;

    public EtatVoiture() {
    }

    public EtatVoiture(Integer idEtatVoiture, String libEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
        this.libEtatVoiture = libEtatVoiture;
    }

    public Integer getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(Integer idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getLibEtatVoiture() {
        return libEtatVoiture;
    }

    public void setLibEtatVoiture(String libEtatVoiture) {
        this.libEtatVoiture = libEtatVoiture;
    }
}