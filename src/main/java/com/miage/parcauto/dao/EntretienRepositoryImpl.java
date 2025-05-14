package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation concrète du repository pour la gestion des entretiens.
 * Cette classe encapsule la logique JDBC pour l'accès aux données des
 * entretiens.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienRepositoryImpl implements EntretienRepository {
    private static final Logger LOGGER = Logger.getLogger(EntretienRepositoryImpl.class.getName());
    private final DbUtil dbUtil;
    private final VehiculeRepository vehiculeRepository;

    /**
     * Constructeur par défaut.
     */
    public EntretienRepositoryImpl() {
        this.dbUtil = DbUtil.getInstance();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
    }

    /**
     * Constructeur avec injection de dépendance (pour tests).
     */
    public EntretienRepositoryImpl(DbUtil dbUtil, VehiculeRepository vehiculeRepository) {
        this.dbUtil = dbUtil;
        this.vehiculeRepository = vehiculeRepository;
    }

    @Override
    public Optional<Entretien> findById(Integer id) {
        try (var conn = dbUtil.getConnection();
                var pstmt = conn.prepareStatement("SELECT * FROM ENTRETIEN WHERE id_entretien = ?")) {
            pstmt.setInt(1, id);
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntretien(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de l'entretien par ID", ex);
        }
        return Optional.empty();
    }

    @Override
    public List<Entretien> findAll() {
        List<Entretien> result = new ArrayList<>();
        try (var conn = dbUtil.getConnection();
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT * FROM ENTRETIEN ORDER BY date_entree_entr DESC")) {
            while (rs.next()) {
                result.add(mapResultSetToEntretien(rs));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les entretiens", ex);
        }
        return result;
    }

    @Override
    public List<Entretien> findAll(int page, int size) {
        List<Entretien> result = new ArrayList<>();
        int offset = page * size;
        try (var conn = dbUtil.getConnection();
                var pstmt = conn
                        .prepareStatement("SELECT * FROM ENTRETIEN ORDER BY date_entree_entr DESC LIMIT ? OFFSET ?")) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des entretiens", ex);
        }
        return result;
    }

    @Override
    public Entretien save(Entretien entity) {
        String sql = "INSERT INTO ENTRETIEN (id_vehicule, date_entree_entr, date_sortie_entr, motif_entr, observation, cout_entr, lieu_entr, type, statut_ot) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var conn = dbUtil.getConnection();
                var pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entity.getIdVehicule());
            pstmt.setTimestamp(2,
                    entity.getDateEntreeEntr() != null ? java.sql.Timestamp.valueOf(entity.getDateEntreeEntr()) : null);
            pstmt.setTimestamp(3,
                    entity.getDateSortieEntr() != null ? java.sql.Timestamp.valueOf(entity.getDateSortieEntr()) : null);
            pstmt.setString(4, entity.getMotifEntr());
            pstmt.setString(5, entity.getObservation());
            pstmt.setBigDecimal(6, entity.getCoutEntr());
            pstmt.setString(7, entity.getLieuEntr());
            pstmt.setString(8, entity.getType() != null ? entity.getType().name() : null);
            pstmt.setString(9, entity.getStatutOt() != null ? entity.getStatutOt().name() : null);
            int affected = pstmt.executeUpdate();
            if (affected == 0)
                throw new SQLException("Aucune ligne insérée");
            try (var rs = pstmt.getGeneratedKeys()) {
                if (rs.next())
                    entity.setIdEntretien(rs.getInt(1));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'entretien", ex);
        }
        return entity;
    }

    @Override
    public Entretien update(Entretien entity) {
        String sql = "UPDATE ENTRETIEN SET id_vehicule=?, date_entree_entr=?, date_sortie_entr=?, motif_entr=?, observation=?, cout_entr=?, lieu_entr=?, type=?, statut_ot=? WHERE id_entretien=?";
        try (var conn = dbUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entity.getIdVehicule());
            pstmt.setTimestamp(2,
                    entity.getDateEntreeEntr() != null ? java.sql.Timestamp.valueOf(entity.getDateEntreeEntr()) : null);
            pstmt.setTimestamp(3,
                    entity.getDateSortieEntr() != null ? java.sql.Timestamp.valueOf(entity.getDateSortieEntr()) : null);
            pstmt.setString(4, entity.getMotifEntr());
            pstmt.setString(5, entity.getObservation());
            pstmt.setBigDecimal(6, entity.getCoutEntr());
            pstmt.setString(7, entity.getLieuEntr());
            pstmt.setString(8, entity.getType() != null ? entity.getType().name() : null);
            pstmt.setString(9, entity.getStatutOt() != null ? entity.getStatutOt().name() : null);
            pstmt.setInt(10, entity.getIdEntretien());
            pstmt.executeUpdate();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'entretien", ex);
        }
        return entity;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM ENTRETIEN WHERE id_entretien = ?";
        try (var conn = dbUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'entretien", ex);
            return false;
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ENTRETIEN";
        try (var conn = dbUtil.getConnection();
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getLong(1);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des entretiens", ex);
        }
        return 0;
    }

    @Override
    public List<Entretien> findByVehicule(int idVehicule) {
        List<Entretien> result = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN WHERE id_vehicule = ? ORDER BY date_entree_entr DESC";
        try (var conn = dbUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens par véhicule", ex);
        }
        return result;
    }

    @Override
    public List<Entretien> findScheduledBetween(LocalDate debut, LocalDate fin) {
        List<Entretien> result = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN WHERE date_entree_entr BETWEEN ? AND ? AND statut_ot = 'Ouvert' ORDER BY date_entree_entr ASC";
        try (var conn = dbUtil.getConnection();
                var pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, java.sql.Timestamp.valueOf(debut.atStartOfDay()));
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(fin.atTime(23, 59, 59)));
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens planifiés entre deux dates", ex);
        }
        return result;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers un objet Entretien.
     * 
     * @param rs ResultSet positionné sur une ligne
     * @return Entretien mappé
     * @throws SQLException en cas d'erreur d'accès
     */
    private Entretien mapResultSetToEntretien(java.sql.ResultSet rs) throws java.sql.SQLException {
        Entretien e = new Entretien();
        e.setIdEntretien(rs.getInt("id_entretien"));
        e.setIdVehicule(rs.getInt("id_vehicule"));
        var entree = rs.getTimestamp("date_entree_entr");
        if (entree != null)
            e.setDateEntreeEntr(entree.toLocalDateTime());
        var sortie = rs.getTimestamp("date_sortie_entr");
        if (sortie != null)
            e.setDateSortieEntr(sortie.toLocalDateTime());
        e.setMotifEntr(rs.getString("motif_entr"));
        e.setObservation(rs.getString("observation"));
        e.setCoutEntr(rs.getBigDecimal("cout_entr"));
        e.setLieuEntr(rs.getString("lieu_entr"));
        String type = rs.getString("type");
        if (type != null)
            e.setType(Entretien.TypeEntretien.fromString(type));
        String statut = rs.getString("statut_ot");
        if (statut != null)
            e.setStatutOt(Entretien.StatutOT.fromString(statut));
        return e;
    }
}
