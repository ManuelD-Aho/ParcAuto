package main.java.com.miage.parcauto.model.affectation;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.finance.SocietaireCompte;

import java.io.Serializable;
import java.time.LocalDateTime; // La DB utilise DATETIME
import java.util.Objects;

/**
 * Entité représentant l'affectation d'un véhicule à un membre du personnel ou à un sociétaire.
 * Correspond à un enregistrement de la table AFFECTATION.
 * Un véhicule ne peut être affecté qu'à un personnel OU à un sociétaire à la fois pour une période donnée.
 */
public class Affectation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; // Nommé 'id' dans la table
    private Vehicule vehicule; // Relation avec Vehicule (via id_vehicule), obligatoire
    private Personnel personnel; // Personnel à qui le véhicule est affecté (optionnel, via id_personnel)
    private SocietaireCompte societaireCompte; // Sociétaire à qui le véhicule est affecté (optionnel, via id_societaire)
    private TypeAffectation type;
    private LocalDateTime dateDebut; // Nommé date_debut dans la DB
    private LocalDateTime dateFin;   // Nommé date_fin dans la DB (peut être null pour affectations indéfinies)

    /**
     * Constructeur par défaut.
     */
    public Affectation() {
    }

    /**
     * Constructeur pour une affectation à un Personnel.
     *
     * @param id                L'identifiant unique de l'affectation.
     * @param vehicule          Le véhicule affecté.
     * @param personnel         Le membre du personnel.
     * @param type              Le type d'affectation.
     * @param dateDebut         La date de début de l'affectation.
     * @param dateFin           La date de fin de l'affectation (peut être null).
     */
    public Affectation(Integer id, Vehicule vehicule, Personnel personnel, TypeAffectation type,
                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id = id;
        this.vehicule = vehicule;
        this.personnel = personnel;
        this.societaireCompte = null; // Assurer l'exclusivité
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        validerExclusiviteBeneficiaire();
    }

    /**
     * Constructeur pour une affectation à un SocietaireCompte.
     *
     * @param id                L'identifiant unique de l'affectation.
     * @param vehicule          Le véhicule affecté.
     * @param societaireCompte  Le compte sociétaire.
     * @param type              Le type d'affectation.
     * @param dateDebut         La date de début de l'affectation.
     * @param dateFin           La date de fin de l'affectation (peut être null).
     */
    public Affectation(Integer id, Vehicule vehicule, SocietaireCompte societaireCompte, TypeAffectation type,
                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.id = id;
        this.vehicule = vehicule;
        this.personnel = null; // Assurer l'exclusivité
        this.societaireCompte = societaireCompte;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        validerExclusiviteBeneficiaire();
    }

    private void validerExclusiviteBeneficiaire() {
        if (this.personnel != null && this.societaireCompte != null) {
            throw new IllegalStateException("Une affectation ne peut concerner qu'un personnel OU un sociétaire, pas les deux.");
        }
        if (this.personnel == null && this.societaireCompte == null) {
            // Selon les règles métier, cela peut être autorisé ou non.
            // Pour l'instant, on suppose qu'un bénéficiaire est requis.
            // throw new IllegalStateException("Une affectation doit avoir un bénéficiaire (Personnel ou Sociétaire).");
        }
    }


    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
        if (personnel != null) {
            this.societaireCompte = null; // Assurer l'exclusivité
        }
        validerExclusiviteBeneficiaire();
    }

    public SocietaireCompte getSocietaireCompte() {
        return societaireCompte;
    }

    public void setSocietaireCompte(SocietaireCompte societaireCompte) {
        this.societaireCompte = societaireCompte;
        if (societaireCompte != null) {
            this.personnel = null; // Assurer l'exclusivité
        }
        validerExclusiviteBeneficiaire();
    }

    public TypeAffectation getType() {
        return type;
    }

    public void setType(TypeAffectation type) {
        this.type = type;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Vérifie si l'affectation est active à la date actuelle.
     * @return true si l'affectation est active, false sinon.
     */
    public boolean isAffectationActive() {
        LocalDateTime maintenant = LocalDateTime.now();
        boolean estCommencee = (dateDebut == null || !dateDebut.isAfter(maintenant));
        boolean pasTerminee = (dateFin == null || !dateFin.isBefore(maintenant));
        return estCommencee && pasTerminee;
    }

    /**
     * Retourne le bénéficiaire de l'affectation, qu'il s'agisse d'un Personnel ou d'un SocietaireCompte.
     * @return Un objet représentant le bénéficiaire, ou null si aucun n'est défini.
     */
    public Object getBeneficiaire() {
        if (personnel != null) {
            return personnel;
        }
        return societaireCompte;
    }

    /**
     * Retourne une chaîne de caractères décrivant le bénéficiaire.
     * @return Description du bénéficiaire ou "Aucun" si non défini.
     */
    public String getBeneficiaireDescription() {
        if (personnel != null) {
            return "Personnel: " + personnel.getNomComplet();
        }
        if (societaireCompte != null) {
            return "Sociétaire: " + societaireCompte.getNom() + " (Cpt: " + societaireCompte.getNumero() + ")";
        }
        return "Aucun bénéficiaire défini";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Affectation that = (Affectation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Affectation{" +
                "id=" + id +
                ", vehicule=" + (vehicule != null ? vehicule.getImmatriculation() : "N/A") +
                ", type=" + type +
                ", beneficiaire=" + getBeneficiaireDescription() +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}