package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RapportVehiculeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private VehiculeDTO vehicule; // Informations de base du véhicule
    private TCODTO tcoActuel;
    private List<EntretienDTO> historiqueEntretiens;
    private List<MissionDTO> historiqueMissions;
    private List<AffectationDTO> historiqueAffectations;
    private AssuranceDTO assuranceActuelle; // Ou List<AssuranceDTO> si plusieurs peuvent être actives/historiques
    private BigDecimal totalCoutsEntretiens;
    private BigDecimal totalCoutsMissions; // Carburant, etc.
    private Integer totalKmParcourusEnMission;
    private Double tauxUtilisation; // Pourcentage de temps en mission ou affecté

    public RapportVehiculeDTO() {
    }

    public VehiculeDTO getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeDTO vehicule) {
        this.vehicule = vehicule;
    }

    public TCODTO getTcoActuel() {
        return tcoActuel;
    }

    public void setTcoActuel(TCODTO tcoActuel) {
        this.tcoActuel = tcoActuel;
    }

    public List<EntretienDTO> getHistoriqueEntretiens() {
        return historiqueEntretiens;
    }

    public void setHistoriqueEntretiens(List<EntretienDTO> historiqueEntretiens) {
        this.historiqueEntretiens = historiqueEntretiens;
    }

    public List<MissionDTO> getHistoriqueMissions() {
        return historiqueMissions;
    }

    public void setHistoriqueMissions(List<MissionDTO> historiqueMissions) {
        this.historiqueMissions = historiqueMissions;
    }

    public List<AffectationDTO> getHistoriqueAffectations() {
        return historiqueAffectations;
    }

    public void setHistoriqueAffectations(List<AffectationDTO> historiqueAffectations) {
        this.historiqueAffectations = historiqueAffectations;
    }

    public AssuranceDTO getAssuranceActuelle() {
        return assuranceActuelle;
    }

    public void setAssuranceActuelle(AssuranceDTO assuranceActuelle) {
        this.assuranceActuelle = assuranceActuelle;
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

    public Integer getTotalKmParcourusEnMission() {
        return totalKmParcourusEnMission;
    }

    public void setTotalKmParcourusEnMission(Integer totalKmParcourusEnMission) {
        this.totalKmParcourusEnMission = totalKmParcourusEnMission;
    }

    public Double getTauxUtilisation() {
        return tauxUtilisation;
    }

    public void setTauxUtilisation(Double tauxUtilisation) {
        this.tauxUtilisation = tauxUtilisation;
    }

    @Override
    public String toString() {
        return "RapportVehiculeDTO{" +
                "vehicule=" + (vehicule != null ? vehicule.getImmatriculation() : "N/A") +
                '}';
    }
}