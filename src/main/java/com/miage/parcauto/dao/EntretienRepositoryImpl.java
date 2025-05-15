package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.StatutOT; // Assurez-vous que ce fichier existe et correspond à l'ENUM de la DB
import main.java.com.miage.parcauto.model.entretien.TypeEntretien; // Assurez-vous que ce fichier existe et correspond à l'ENUM de la DB
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Entretien}.
 * Gère les opérations CRUD et les requêtes spécifiques pour les entretiens de véhicules.
 */
public class EntretienRepositoryImpl implements Repository<Entretien, Integer> {

    private static final Logger LOGGER = Logger.getLogger(EntretienRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT e.id_ot, e.date_debut_ot, e.date_fin_ot, e.cout_reel, e.observations, " +
            "e.type_entretien, e.statut_ot, e.id_vehicule " +
            "FROM ENTRETIEN e ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE e.id_ot = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY e.date_debut_ot DESC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY e.date_debut_ot DESC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO ENTRETIEN (id_vehicule, date_debut_ot, date_fin_ot, cout_reel, observations, type_entretien, statut_ot) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE ENTRETIEN SET id_vehicule = ?, date_debut_ot = ?, date_fin_ot = ?, cout_reel = ?, " +
            "observations = ?, type_entretien = ?, statut_ot = ? WHERE id_ot = ?";
    private static final String SQL_DELETE = "DELETE FROM ENTRETIEN WHERE id_ot = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM ENTRETIEN";
    private static final String SQL_FIND_BY_VEHICULE_ID = SQL_SELECT_BASE + "WHERE e.id_vehicule = ? ORDER BY e.date_debut_ot DESC";
    private static final String SQL_FIND_SCHEDULED_BETWEEN = SQL_SELECT_BASE + "WHERE e.date_debut_ot >= ? AND e.date_debut_ot < ? ORDER BY e.date_debut_ot ASC";


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Entretien> findById(Integer id) {
        if (id == null) return Optional.empty();
        Entretien entretien = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entretien = mapResultSetToEntretien(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Entretien par ID: " + id, e);
        }
        return Optional.ofNullable(entretien);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entretien> findAll() {
        List<Entretien> entretiens = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entretiens.add(mapResultSetToEntretien(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les Entretiens", e);
        }
        return entretiens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entretien> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Entretien> entretiens = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Entretiens", e);
        }
        return entretiens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entretien save(Entretien entity) {
        if (entity == null || entity.getVehicule() == null || entity.getVehicule().getIdVehicule() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'un Entretien nul ou sans véhicule associé.");
            return null; // id_vehicule est NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapEntretienToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Entretien a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdOt(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Entretien a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Entretien pour véhicule ID: " + entity.getVehicule().getIdVehicule(), e);
            dbUtilRollback(conn);
            return null;
        } finally {
            DbUtil.closeQuietly(null, pstmt, generatedKeys);
            DbUtil.closeQuietly(conn, null, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entretien update(Entretien entity) {
        if (entity == null || entity.getIdOt() == null || entity.getVehicule() == null || entity.getVehicule().getIdVehicule() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'un Entretien nul, sans ID OT, ou sans véhicule associé.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapEntretienToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Entretien ID: {0}", entity.getIdOt());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Entretien ID: " + entity.getIdOt(), e);
            dbUtilRollback(conn);
            return null;
        } finally {
            DbUtil.closeQuietly(conn, pstmt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Entretien ID: " + id, e);
            dbUtilRollback(conn);
            return false;
        } finally {
            DbUtil.closeQuietly(conn, pstmt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        long count = 0;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Entretiens", e);
        }
        return count;
    }

    /**
     * Recherche les entretiens pour un véhicule spécifique.
     * @param idVehicule L'ID du véhicule.
     * @return Une liste d'entretiens pour le véhicule.
     */
    public List<Entretien> findByVehiculeId(int idVehicule) {
        List<Entretien> entretiens = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_VEHICULE_ID)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche d'Entretiens pour le véhicule ID: " + idVehicule, e);
        }
        return entretiens;
    }

    /**
     * Recherche les entretiens planifiés (date de début) entre deux dates.
     * @param debut La date de début de la période.
     * @param fin La date de fin de la période.
     * @return Une liste d'entretiens planifiés dans l'intervalle.
     */
    public List<Entretien> findScheduledBetween(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) return new ArrayList<>();
        List<Entretien> entretiens = new ArrayList<>();
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.plusDays(1).atStartOfDay();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_SCHEDULED_BETWEEN)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(debutDateTime));
            pstmt.setTimestamp(2, Timestamp.valueOf(finDateTime));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche d'Entretiens planifiés entre " + debut + " et " + fin, e);
        }
        return entretiens;
    }


    private Entretien mapResultSetToEntretien(ResultSet rs) throws SQLException {
        Entretien entretien = new Entretien();
        entretien.setIdOt(rs.getInt("id_ot"));

        int idVehicule = rs.getInt("id_vehicule");
        if (!rs.wasNull()) {
            Vehicule vehicule = new Vehicule();
            vehicule.setIdVehicule(idVehicule);
            entretien.setVehicule(vehicule);
        }

        Timestamp dateDebutOtTs = rs.getTimestamp("date_debut_ot");
        if (dateDebutOtTs != null) {
            entretien.setDateDebutOt(dateDebutOtTs.toLocalDateTime());
        }
        Timestamp dateFinOtTs = rs.getTimestamp("date_fin_ot");
        if (dateFinOtTs != null) {
            entretien.setDateFinOt(dateFinOtTs.toLocalDateTime());
        }
        entretien.setCoutReel(rs.getBigDecimal("cout_reel"));
        entretien.setObservations(rs.getString("observations"));

        String typeEntretienDb = rs.getString("type_entretien");
        if (typeEntretienDb != null) {
            try {
                entretien.setTypeEntretien(TypeEntretien.fromString(typeEntretienDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Type d'entretien inconnu '" + typeEntretienDb + "' pour Entretien ID: " + entretien.getIdOt() + ". Valeurs attendues: " + TypeEntretien.getValidValues(), e);
            }
        }

        String statutOtDb = rs.getString("statut_ot");
        if (statutOtDb != null) {
            try {
                entretien.setStatutOt(StatutOT.fromString(statutOtDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Statut OT inconnu '" + statutOtDb + "' pour Entretien ID: " + entretien.getIdOt() + ". Valeurs attendues: " + StatutOT.getValidValues(), e);
            }
        }
        return entretien;
    }

    private void mapEntretienToPreparedStatement(Entretien entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        // id_vehicule est NOT NULL dans la DB
        pstmt.setInt(paramIndex++, entity.getVehicule().getIdVehicule());

        if (entity.getDateDebutOt() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateDebutOt()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP); // date_debut_ot est NOT NULL
        }
        if (entity.getDateFinOt() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateFinOt()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP);
        }
        pstmt.setBigDecimal(paramIndex++, entity.getCoutReel());
        pstmt.setString(paramIndex++, entity.getObservations());

        if (entity.getTypeEntretien() != null) {
            pstmt.setString(paramIndex++, entity.getTypeEntretien().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // type_entretien est NOT NULL
        }
        if (entity.getStatutOt() != null) {
            pstmt.setString(paramIndex++, entity.getStatutOt().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // statut_ot est NOT NULL
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdOt());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Entretien.", ex);
            }
        }
    }
}