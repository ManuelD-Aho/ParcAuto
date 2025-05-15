package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AlerteEntretienDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private String vehiculeInfo; // Marque Modèle (Immatriculation)
    private String typeEntretienPrevu; // "Préventif KM", "Préventif Date"
    private Integer kmProchainEntretien;
    private LocalDateTime dateProchainEntretien;
    private Integer kmDepuisDernierEntretien; // Optionnel, pour information
    private String messageAlerte;

    public AlerteEntretienDTO() {
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getVehiculeInfo() {
        return vehiculeInfo;
    }

    public void setVehiculeInfo(String vehiculeInfo) {
        this.vehiculeInfo = vehiculeInfo;
    }

    public String getTypeEntretienPrevu() {
        return typeEntretienPrevu;
    }

    public void setTypeEntretienPrevu(String typeEntretienPrevu) {
        this.typeEntretienPrevu = typeEntretienPrevu;
    }

    public Integer getKmProchainEntretien() {
        return kmProchainEntretien;
    }

    public void setKmProchainEntretien(Integer kmProchainEntretien) {
        this.kmProchainEntretien = kmProchainEntretien;
    }

    public LocalDateTime getDateProchainEntretien() {
        return dateProchainEntretien;
    }

    public void setDateProchainEntretien(LocalDateTime dateProchainEntretien) {
        this.dateProchainEntretien = dateProchainEntretien;
    }

    public Integer getKmDepuisDernierEntretien() {
        return kmDepuisDernierEntretien;
    }

    public void setKmDepuisDernierEntretien(Integer kmDepuisDernierEntretien) {
        this.kmDepuisDernierEntretien = kmDepuisDernierEntretien;
    }

    public String getMessageAlerte() {
        return messageAlerte;
    }

    public void setMessageAlerte(String messageAlerte) {
        this.messageAlerte = messageAlerte;
    }

    @Override
    public String toString() {
        return "AlerteEntretienDTO{" +
                "vehiculeInfo='" + vehiculeInfo + '\'' +
                ", typeEntretienPrevu='" + typeEntretienPrevu + '\'' +
                ", messageAlerte='" + messageAlerte + '\'' +
                '}';
    }
}