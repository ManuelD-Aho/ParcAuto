package main.java.com.miage.parcauto.model.finance;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Représentation de la vue v_TCO qui calcule le coût total de possession d'un véhicule.
 */
public class TCO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal coutsTotaux;
    private Integer idVehicule;
    private Integer kmActuels;

    public TCO() {
    }

    public TCO(BigDecimal coutsTotaux, Integer idVehicule, Integer kmActuels) {
        this.coutsTotaux = coutsTotaux;
        this.idVehicule = idVehicule;
        this.kmActuels = kmActuels;
    }

    // Getters et Setters
    public BigDecimal getCoutsTotaux() {
        return coutsTotaux;
    }

    public void setCoutsTotaux(BigDecimal coutsTotaux) {
        this.coutsTotaux = coutsTotaux;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public Integer getKmActuels() {
        return kmActuels;
    }

    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    /**
     * Calcule le coût par kilomètre.
     * @return Le coût par kilomètre ou null si les kilomètres actuels sont nuls ou zéro
     */
    public BigDecimal getCoutParKm() {
        if (kmActuels == null || kmActuels == 0 || coutsTotaux == null) {
            return null;
        }
        return coutsTotaux.divide(new BigDecimal(kmActuels), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCO tco = (TCO) o;
        return Objects.equals(idVehicule, tco.idVehicule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicule);
    }

    @Override
    public String toString() {
        return "TCO pour véhicule #" + idVehicule + ": " + coutsTotaux + "€";
    }
}