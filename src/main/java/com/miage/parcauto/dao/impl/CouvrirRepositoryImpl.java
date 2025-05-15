package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.CouvrirRepository;
import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.Energie; // Nécessaire pour mapResultSetToVehicule
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Nécessaire pour mapResultSetToVehicule et Assurance
import java.util.ArrayList;
import java.util.List;

public class CouvrirRepositoryImpl implements CouvrirRepository {

    // Méthodes de mapping réutilisées (simplifiées ici, devraient être complètes)
    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("v_id_vehicule")); // Alias pour éviter conflits
        vehicule.setIdEtatVoiture(rs.getInt("v_id_etat_voiture"));
        String energieStr = rs.getString("v_energie");
        if (energieStr != null) {
            vehicule.setEnergie(Energie.fromString(energieStr));
        }
        vehicule.setNumeroChassi(rs.getString("v_numero_chassi"));
        vehicule.setImmatriculation(rs.getString("v_immatriculation"));
        vehicule.setMarque(rs.getString("v_marque"));
        vehicule.setModele(rs.getString("v_modele"));
        int nbPlaces = rs.getInt("v_nb_places");
        vehicule.setNbPlaces(rs.wasNull() ? null : nbPlaces);
        Timestamp dateAcquisitionTs = rs.getTimestamp("v_date_acquisition");
        vehicule.setDateAcquisition(dateAcquisitionTs != null ? dateAcquisitionTs.toLocalDateTime() : null);
        Timestamp dateMiseEnServiceTs = rs.getTimestamp("v_date_mise_en_service");
        vehicule.setDateMiseEnService(dateMiseEnServiceTs != null ? dateMiseEnServiceTs.toLocalDateTime() : null);
        int kmActuels = rs.getInt("v_km_actuels");
        vehicule.setKmActuels(rs.wasNull() ? null : kmActuels);
        // Ajouter les autres champs de Vehicule si nécessaire pour findVehiculesByAssuranceId
        return vehicule;
    }

    private Assurance mapResultSetToAssurance(ResultSet rs) throws SQLException {
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(rs.getInt("a_num_carte_assurance")); // Alias
        assurance.setCompagnie(rs.getString("a_compagnie"));
        assurance.setAdresse(rs.getString("a_adresse"));
        assurance.setTelephone(rs.getString("a_telephone"));
        Timestamp dateDebutTs = rs.getTimestamp("a_date_debut");
        assurance.setDateDebut(dateDebutTs != null ? dateDebutTs.toLocalDateTime() : null);
        Timestamp dateFinTs = rs.getTimestamp("a_date_fin");
        assurance.setDateFin(dateFinTs != null ? dateFinTs.toLocalDateTime() : null);
        assurance.setPrix(rs.getBigDecimal("a_prix"));
        // Ajouter les autres champs d'Assurance si nécessaire pour findAssurancesByVehiculeId
        return assurance;
    }


    @Override
    public void linkVehiculeToAssurance(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException {
        String sql = "INSERT INTO COUVRIR (id_vehicule, num_carte_assurance) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setInt(2, numCarteAssurance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Gérer les violations de contrainte (ex: clé dupliquée) spécifiquement si besoin
            throw new DataAccessException("Erreur lors de la liaison du véhicule " + idVehicule + " à l'assurance " + numCarteAssurance, e);
        }
    }

    @Override
    public void unlinkVehiculeFromAssurance(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException {
        String sql = "DELETE FROM COUVRIR WHERE id_vehicule = ? AND num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setInt(2, numCarteAssurance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la dissociation du véhicule " + idVehicule + " de l'assurance " + numCarteAssurance, e);
        }
    }

    @Override
    public void unlinkAllVehiculesFromAssurance(Connection conn, Integer numCarteAssurance) throws SQLException {
        String sql = "DELETE FROM COUVRIR WHERE num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numCarteAssurance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la dissociation de tous les véhicules de l'assurance " + numCarteAssurance, e);
        }
    }

    @Override
    public void unlinkAllAssurancesFromVehicule(Connection conn, Integer idVehicule) throws SQLException {
        String sql = "DELETE FROM COUVRIR WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la dissociation de toutes les assurances du véhicule " + idVehicule, e);
        }
    }

    @Override
    public List<Vehicule> findVehiculesByAssuranceId(Connection conn, Integer numCarteAssurance) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        // Jointure pour récupérer les détails complets du véhicule
        String sql = "SELECT v.id_vehicule as v_id_vehicule, v.id_etat_voiture as v_id_etat_voiture, v.energie as v_energie, v.numero_chassi as v_numero_chassi, v.immatriculation as v_immatriculation, v.marque as v_marque, v.modele as v_modele, v.nb_places as v_nb_places, v.date_acquisition as v_date_acquisition, v.date_mise_en_service as v_date_mise_en_service, v.km_actuels as v_km_actuels " +
                "FROM VEHICULES v JOIN COUVRIR c ON v.id_vehicule = c.id_vehicule " +
                "WHERE c.num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numCarteAssurance);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des véhicules pour l'assurance " + numCarteAssurance, e);
        }
        return vehicules;
    }

    @Override
    public List<Assurance> findAssurancesByVehiculeId(Connection conn, Integer idVehicule) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        // Jointure pour récupérer les détails complets de l'assurance
        String sql = "SELECT a.num_carte_assurance as a_num_carte_assurance, a.compagnie as a_compagnie, a.adresse as a_adresse, a.telephone as a_telephone, a.date_debut as a_date_debut, a.date_fin as a_date_fin, a.prix as a_prix " +
                "FROM ASSURANCE a JOIN COUVRIR c ON a.num_carte_assurance = c.num_carte_assurance " +
                "WHERE c.id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des assurances pour le véhicule " + idVehicule, e);
        }
        return assurances;
    }

    @Override
    public boolean isLinked(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException {
        String sql = "SELECT COUNT(*) FROM COUVRIR WHERE id_vehicule = ? AND num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setInt(2, numCarteAssurance);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la vérification du lien entre véhicule " + idVehicule + " et assurance " + numCarteAssurance, e);
        }
        return false;
    }
}