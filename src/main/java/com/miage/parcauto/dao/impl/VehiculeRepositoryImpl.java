package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.VehiculeRepository;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.Energie;
// L'entité EtatVoiture n'est pas directement utilisée pour le mapping ici, on utilise son ID.
// import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculeRepositoryImpl implements VehiculeRepository {

    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("id_vehicule"));
        vehicule.setImmatriculation(rs.getString("immatriculation"));
        vehicule.setNumeroChassis(rs.getString("numero_chassis"));
        vehicule.setIdEtatVoiture(rs.getInt("id_etat_voiture"));
        String energieStr = rs.getString("energie");
        vehicule.setEnergie(energieStr != null ? Energie.valueOf(energieStr) : null);
        vehicule.setMarque(rs.getString("marque"));
        vehicule.setModele(rs.getString("modele"));
        vehicule.setDateAchat(
                rs.getTimestamp("date_achat") != null ? rs.getTimestamp("date_achat").toLocalDateTime() : null);
        vehicule.setCoutAchat(rs.getBigDecimal("cout_achat"));
        vehicule.setKilometrage(rs.getInt("kilometrage"));
        vehicule.setDateAmortissement(
                rs.getTimestamp("date_amortissement") != null ? rs.getTimestamp("date_amortissement").toLocalDateTime()
                        : null);
        return vehicule;
    }

    @Override
    public Optional<Vehicule> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM VEHICULES WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du véhicule par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicule> findAll(Connection conn) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULES";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les véhicules", e);
        }
        return vehicules;
    }

    @Override
    public List<Vehicule> findAll(Connection conn, int page, int size) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULES LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des véhicules", e);
        }
        return vehicules;
    }

    @Override
    public Vehicule save(Connection conn, Vehicule vehicule) throws SQLException {
        String sql = "INSERT INTO VEHICULES (id_etat_voiture, energie, numero_chassi, immatriculation, marque, modele, nb_places, date_acquisition, date_ammortissement, date_mise_en_service, puissance, couleur, prix_vehicule, km_actuels, date_etat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vehicule.getIdEtatVoiture());
            pstmt.setString(2, vehicule.getEnergie() != null ? vehicule.getEnergie().getValeur() : null);
            pstmt.setString(3, vehicule.getNumeroChassi());
            pstmt.setString(4, vehicule.getImmatriculation());
            pstmt.setString(5, vehicule.getMarque());
            pstmt.setString(6, vehicule.getModele());
            if (vehicule.getNbPlaces() != null)
                pstmt.setInt(7, vehicule.getNbPlaces());
            else
                pstmt.setNull(7, Types.INTEGER);
            pstmt.setTimestamp(8,
                    vehicule.getDateAcquisition() != null ? Timestamp.valueOf(vehicule.getDateAcquisition()) : null);
            pstmt.setTimestamp(9,
                    vehicule.getDateAmmortissement() != null ? Timestamp.valueOf(vehicule.getDateAmmortissement())
                            : null);
            pstmt.setTimestamp(10,
                    vehicule.getDateMiseEnService() != null ? Timestamp.valueOf(vehicule.getDateMiseEnService())
                            : null);
            if (vehicule.getPuissance() != null)
                pstmt.setInt(11, vehicule.getPuissance());
            else
                pstmt.setNull(11, Types.INTEGER);
            pstmt.setString(12, vehicule.getCouleur());
            pstmt.setBigDecimal(13, vehicule.getPrixVehicule());
            if (vehicule.getKmActuels() != null)
                pstmt.setInt(14, vehicule.getKmActuels());
            else
                pstmt.setNull(14, Types.INTEGER);
            pstmt.setTimestamp(15, vehicule.getDateEtat() != null ? Timestamp.valueOf(vehicule.getDateEtat()) : null);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création du véhicule a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicule.setIdVehicule(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création du véhicule a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde du véhicule: " + vehicule.getImmatriculation(),
                    e);
        }
        return vehicule;
    }

    @Override
    public Vehicule update(Connection conn, Vehicule vehicule) throws SQLException {
        String sql = "UPDATE VEHICULES SET id_etat_voiture = ?, energie = ?, numero_chassi = ?, immatriculation = ?, marque = ?, modele = ?, nb_places = ?, date_acquisition = ?, date_ammortissement = ?, date_mise_en_service = ?, puissance = ?, couleur = ?, prix_vehicule = ?, km_actuels = ?, date_etat = ? WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicule.getIdEtatVoiture());
            pstmt.setString(2, vehicule.getEnergie() != null ? vehicule.getEnergie().getValeur() : null);
            pstmt.setString(3, vehicule.getNumeroChassi());
            pstmt.setString(4, vehicule.getImmatriculation());
            pstmt.setString(5, vehicule.getMarque());
            pstmt.setString(6, vehicule.getModele());
            if (vehicule.getNbPlaces() != null)
                pstmt.setInt(7, vehicule.getNbPlaces());
            else
                pstmt.setNull(7, Types.INTEGER);
            pstmt.setTimestamp(8,
                    vehicule.getDateAcquisition() != null ? Timestamp.valueOf(vehicule.getDateAcquisition()) : null);
            pstmt.setTimestamp(9,
                    vehicule.getDateAmmortissement() != null ? Timestamp.valueOf(vehicule.getDateAmmortissement())
                            : null);
            pstmt.setTimestamp(10,
                    vehicule.getDateMiseEnService() != null ? Timestamp.valueOf(vehicule.getDateMiseEnService())
                            : null);
            if (vehicule.getPuissance() != null)
                pstmt.setInt(11, vehicule.getPuissance());
            else
                pstmt.setNull(11, Types.INTEGER);
            pstmt.setString(12, vehicule.getCouleur());
            pstmt.setBigDecimal(13, vehicule.getPrixVehicule());
            if (vehicule.getKmActuels() != null)
                pstmt.setInt(14, vehicule.getKmActuels());
            else
                pstmt.setNull(14, Types.INTEGER);
            pstmt.setTimestamp(15, vehicule.getDateEtat() != null ? Timestamp.valueOf(vehicule.getDateEtat()) : null);
            pstmt.setInt(16, vehicule.getIdVehicule());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour du véhicule avec ID " + vehicule.getIdVehicule()
                        + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour du véhicule: " + vehicule.getIdVehicule(), e);
        }
        return vehicule;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM VEHICULES WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression du véhicule: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM VEHICULES";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des véhicules", e);
        }
        return 0;
    }

    @Override
    public List<Vehicule> findByEtatVoitureId(Connection conn, Integer idEtatVoiture) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULES WHERE id_etat_voiture = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEtatVoiture);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des véhicules par ID état: " + idEtatVoiture, e);
        }
        return vehicules;
    }

    @Override
    public List<Vehicule> findRequiringMaintenance(Connection conn, int kmSeuilProchainEntretien) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        // Cette requête est un exemple et doit être adaptée à votre logique métier
        // exacte
        // pour déterminer la nécessité d'une maintenance.
        // Par exemple, si vous avez une table d'entretiens planifiés ou un champ
        // 'km_prochain_entretien'.
        // Ici, une logique simplifiée : véhicules ayant dépassé un certain kilométrage.
        String sql = "SELECT * FROM VEHICULES v " +
                "LEFT JOIN ENTRETIEN e ON v.id_vehicule = e.id_vehicule " +
                "AND e.date_realisation = (SELECT MAX(e2.date_realisation) FROM ENTRETIEN e2 WHERE e2.id_vehicule = v.id_vehicule) "
                + // Dernier entretien réalisé
                "WHERE v.km_actuels >= COALESCE(e.km_prochain_entretien, 0) - ? " + // km_prochain_entretien du dernier
                                                                                    // entretien
                "OR (e.id_entretien IS NULL AND v.km_actuels >= ?)"; // Cas où aucun entretien n'a encore été fait,
                                                                     // seuil initial

        // Si la logique est plus simple, par exemple juste un seuil sur km_actuels par
        // rapport à une valeur de référence
        // String sql_simple = "SELECT * FROM VEHICULES WHERE km_actuels > ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, kmSeuilProchainEntretien); // Seuil pour la différence
            pstmt.setInt(2, kmSeuilProchainEntretien); // Seuil initial si pas d'entretien
            // Pour la version simple: pstmt.setInt(1, kmSeuilProchainEntretien);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des véhicules nécessitant une maintenance avec seuil "
                            + kmSeuilProchainEntretien,
                    e);
        }
        return vehicules;
    }

    @Override
    public Optional<Vehicule> findByImmatriculation(Connection conn, String immatriculation) throws SQLException {
        String sql = "SELECT * FROM VEHICULES WHERE immatriculation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, immatriculation);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche du véhicule par immatriculation: " + immatriculation, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vehicule> findByNumeroChassi(Connection conn, String numeroChassi) throws SQLException {
        String sql = "SELECT * FROM VEHICULES WHERE numero_chassi = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numeroChassi);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche du véhicule par numéro de châssis: " + numeroChassi, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicule> findByEnergie(Connection conn, Energie energie) throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM VEHICULES WHERE energie = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, energie.getValeur());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des véhicules par énergie: " + energie.getValeur(), e);
        }
        return vehicules;
    }
}