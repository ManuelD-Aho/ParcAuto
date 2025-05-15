package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

public class BilanMensuelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private YearMonth moisAnnee;
    private BigDecimal totalRevenusMensualites;
    private BigDecimal totalCoutsEntretiens;
    private BigDecimal totalCoutsMissions;
    private BigDecimal totalAutresDepenses;
    private BigDecimal soldeMensuel;
    private int nombreNouvellesAffectations;
    private int nombreFinAffectations;
    private int nombreNouveauxVehicules;
    private int nombreVehiculesSortis; // Vendus, mis au rebut
    private Map<String, BigDecimal> revenusParSource; // e.g., "Mensualités Crédit", "Location Mission"
    private Map<String, BigDecimal> depensesParCategorie; // e.g., "Carburant", "Entretien Préventif"

    public BilanMensuelDTO() {
        this.totalRevenusMensualites = BigDecimal.ZERO;
        this.totalCoutsEntretiens = BigDecimal.ZERO;
        this.totalCoutsMissions = BigDecimal.ZERO;
        this.totalAutresDepenses = BigDecimal.ZERO;
        this.soldeMensuel = BigDecimal.ZERO;
    }

    public YearMonth getMoisAnnee() {
        return moisAnnee;
    }

    public void setMoisAnnee(YearMonth moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public BigDecimal getTotalRevenusMensualites() {
        return totalRevenusMensualites;
    }

    public void setTotalRevenusMensualites(BigDecimal totalRevenusMensualites) {
        this.totalRevenusMensualites = totalRevenusMensualites;
    }

    public BigDecimal getTotalCoutsEntretiens() {
        return totalCoutsEntretiens;
    }

    public void setTotalCoutsEntretiens(BigDecimal totalCoutsEntretiens) {
        this.totalCoutsEntretiens = totalCoutsEntretiens;
    }

    public BigDecimal getTotalCoutsMissions() {
        return totalCoutsMissions;
    }

    public void setTotalCoutsMissions(BigDecimal totalCoutsMissions) {
        this.totalCoutsMissions = totalCoutsMissions;
    }

    public BigDecimal getTotalAutresDepenses() {
        return totalAutresDepenses;
    }

    public void setTotalAutresDepenses(BigDecimal totalAutresDepenses) {
        this.totalAutresDepenses = totalAutresDepenses;
    }

    public BigDecimal getSoldeMensuel() {
        return soldeMensuel;
    }

    public void setSoldeMensuel(BigDecimal soldeMensuel) {
        this.soldeMensuel = soldeMensuel;
    }

    public int getNombreNouvellesAffectations() {
        return nombreNouvellesAffectations;
    }

    public void setNombreNouvellesAffectations(int nombreNouvellesAffectations) {
        this.nombreNouvellesAffectations = nombreNouvellesAffectations;
    }

    public int getNombreFinAffectations() {
        return nombreFinAffectations;
    }

    public void setNombreFinAffectations(int nombreFinAffectations) {
        this.nombreFinAffectations = nombreFinAffectations;
    }

    public int getNombreNouveauxVehicules() {
        return nombreNouveauxVehicules;
    }

    public void setNombreNouveauxVehicules(int nombreNouveauxVehicules) {
        this.nombreNouveauxVehicules = nombreNouveauxVehicules;
    }

    public int getNombreVehiculesSortis() {
        return nombreVehiculesSortis;
    }

    public void setNombreVehiculesSortis(int nombreVehiculesSortis) {
        this.nombreVehiculesSortis = nombreVehiculesSortis;
    }

    public Map<String, BigDecimal> getRevenusParSource() {
        return revenusParSource;
    }

    public void setRevenusParSource(Map<String, BigDecimal> revenusParSource) {
        this.revenusParSource = revenusParSource;
    }

    public Map<String, BigDecimal> getDepensesParCategorie() {
        return depensesParCategorie;
    }

    public void setDepensesParCategorie(Map<String, BigDecimal> depensesParCategorie) {
        this.depensesParCategorie = depensesParCategorie;
    }

    @Override
    public String toString() {
        return "BilanMensuelDTO{" +
                "moisAnnee=" + moisAnnee +
                ", soldeMensuel=" + soldeMensuel +
                '}';
    }
}