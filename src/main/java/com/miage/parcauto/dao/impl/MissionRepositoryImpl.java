package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.MissionRepository;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.StatutMission;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionRepositoryImpl implements MissionRepository {

    private Mission mapResultSetToMission(ResultSet rs) throws SQLException {
        Mission mission = new Mission();
        mission.setIdMission(rs.getInt("id_mission"));
        mission.setIdVehicule(rs.getInt("id_vehicule"));
        mission.setIdPersonnel(rs.getInt("id_personnel"));
        mission.setLibelle(rs.getString("libelle"));
        mission.setSiteDestination(rs.getString("site_destination"));
        mission.setDateDebut(
                rs.getTimestamp("date_debut") != null ? rs.getTimestamp("date_debut").toLocalDateTime() : null);
        mission.setDateFinPrevue(
                rs.getTimestamp("date_fin_prevue") != null ? rs.getTimestamp("date_fin_prevue").toLocalDateTime()
                        : null);
        mission.setDateFinEffective(
                rs.getTimestamp("date_fin_effective") != null ? rs.getTimestamp("date_fin_effective").toLocalDateTime()
                        : null);
        String statutStr = rs.getString("statut");
        mission.setStatut(statutStr != null ? StatutMission.valueOf(statutStr) : null);
        mission.setCoutEstime(rs.getBigDecimal("cout_estime"));
        mission.setCoutTotalReel(rs.getBigDecimal("cout_total_reel"));
        mission.setCircuit(rs.getString("circuit"));
        mission.setObservations(rs.getString("observations"));
        return mission;
    }

    @Override
    public Optional<Mission> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM MISSION WHERE id_mission = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de la mission par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Mission> findAll(Connection conn) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT * FROM MISSION";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                missions.add(mapResultSetToMission(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de toutes les missions", e);
        }
        return missions;
    }

    @Override
    public List<Mission> findAll(Connection conn, int page, int size) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT * FROM MISSION LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des missions", e);
        }
        return missions;
    }

    @Override
    public Mission save(Connection conn, Mission mission) throws SQLException {
        String sql = "INSERT INTO MISSION (id_vehicule, id_personnel, id_compte_societaire, status, date_debut, date_fin, motif, destination, km_depart, km_retour, observation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, mission.getIdVehicule());
            pstmt.setInt(2, mission.getIdPersonnel());
            if (mission.getIdCompteSocietaire() != null)
                pstmt.setInt(3, mission.getIdCompteSocietaire());
            else
                pstmt.setNull(3, Types.INTEGER);
            pstmt.setString(4, mission.getStatus() != null ? mission.getStatus().getValeur()
                    : StatutMission.PLANIFIEE.getValeur()); // Défaut si non fourni
            pstmt.setTimestamp(5, mission.getDateDebut() != null ? Timestamp.valueOf(mission.getDateDebut()) : null);
            pstmt.setTimestamp(6, mission.getDateFin() != null ? Timestamp.valueOf(mission.getDateFin()) : null);
            pstmt.setString(7, mission.getMotif());
            pstmt.setString(8, mission.getDestination());
            if (mission.getKmDepart() != null)
                pstmt.setInt(9, mission.getKmDepart());
            else
                pstmt.setNull(9, Types.INTEGER);
            if (mission.getKmRetour() != null)
                pstmt.setInt(10, mission.getKmRetour());
            else
                pstmt.setNull(10, Types.INTEGER);
            pstmt.setString(11, mission.getObservation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de la mission a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mission.setIdMission(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de la mission a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de la mission: " + mission.getMotif(), e);
        }
        return mission;
    }

    @Override
    public Mission update(Connection conn, Mission mission) throws SQLException {
        String sql = "UPDATE MISSION SET id_vehicule = ?, id_personnel = ?, id_compte_societaire = ?, status = ?, date_debut = ?, date_fin = ?, motif = ?, destination = ?, km_depart = ?, km_retour = ?, observation = ? WHERE id_mission = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mission.getIdVehicule());
            pstmt.setInt(2, mission.getIdPersonnel());
            if (mission.getIdCompteSocietaire() != null)
                pstmt.setInt(3, mission.getIdCompteSocietaire());
            else
                pstmt.setNull(3, Types.INTEGER);
            pstmt.setString(4, mission.getStatus() != null ? mission.getStatus().getValeur() : null);
            pstmt.setTimestamp(5, mission.getDateDebut() != null ? Timestamp.valueOf(mission.getDateDebut()) : null);
            pstmt.setTimestamp(6, mission.getDateFin() != null ? Timestamp.valueOf(mission.getDateFin()) : null);
            pstmt.setString(7, mission.getMotif());
            pstmt.setString(8, mission.getDestination());
            if (mission.getKmDepart() != null)
                pstmt.setInt(9, mission.getKmDepart());
            else
                pstmt.setNull(9, Types.INTEGER);
            if (mission.getKmRetour() != null)
                pstmt.setInt(10, mission.getKmRetour());
            else
                pstmt.setNull(10, Types.INTEGER);
            pstmt.setString(11, mission.getObservation());
            pstmt.setInt(12, mission.getIdMission());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de la mission avec ID " + mission.getIdMission()
                        + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de la mission: " + mission.getIdMission(), e);
        }
        return mission;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM MISSION WHERE id_mission = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            // Avant de supprimer une mission, il faudrait peut-être gérer les
            // DepenseMission associées
            // (suppression en cascade configurée dans la DB ou suppression explicite
            // ici/service)
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de la mission: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MISSION";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des missions", e);
        }
        return 0;
    }

    @Override
    public List<Mission> findActiveForVehicule(Connection conn, Integer idVehicule) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT * FROM MISSION WHERE id_vehicule = ? AND status != ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setString(2, StatutMission.CLOTUREE.getValeur());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des missions actives pour le véhicule " + idVehicule, e);
        }
        return missions;
    }

    @Override
    public List<Mission> findByPeriod(Connection conn, LocalDateTime debut, LocalDateTime fin) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        // Missions qui commencent ou finissent dans la période, ou qui englobent la
        // période.
        String sql = "SELECT * FROM MISSION WHERE (date_debut <= ? AND date_fin >= ?) OR (date_debut BETWEEN ? AND ?) OR (date_fin BETWEEN ? AND ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Timestamp tsDebut = Timestamp.valueOf(debut);
            Timestamp tsFin = Timestamp.valueOf(fin);
            pstmt.setTimestamp(1, tsFin); // date_debut <= finPeriode
            pstmt.setTimestamp(2, tsDebut); // date_fin >= debutPeriode
            pstmt.setTimestamp(3, tsDebut);
            pstmt.setTimestamp(4, tsFin);
            pstmt.setTimestamp(5, tsDebut);
            pstmt.setTimestamp(6, tsFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des missions pour la période du " + debut + " au " + fin, e);
        }
        return missions;
    }

    @Override
    public List<Mission> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT * FROM MISSION WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des missions par ID véhicule: " + idVehicule, e);
        }
        return missions;
    }

    @Override
    public List<Mission> findByVehiculeIdAndStatus(Connection conn, Integer idVehicule, StatutMission statut)
            throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT * FROM MISSION WHERE id_vehicule = ? AND status = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setString(2, statut.getValeur());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des missions pour le véhicule " + idVehicule
                    + " avec statut " + statut, e);
        }
        return missions;
    }
}