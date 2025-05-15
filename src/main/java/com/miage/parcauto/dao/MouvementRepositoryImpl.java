package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.finance.SocietaireCompte;
import main.java.com.miage.parcauto.model.finance.TypeMouvement; // Assurez-vous que cette énumération existe
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.entretien.Entretien;

import java.math.BigDecimal;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Mouvement}.
 * Gère les opérations CRUD pour les mouvements financiers.
 */
public class MouvementRepositoryImpl implements Repository<Mouvement, Integer> {

    private static final Logger LOGGER = Logger.getLogger(MouvementRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT m.id_mouvement, m.id_compte_societaire, m.date_mouvement, m.montant_mouvement, " +
            "m.type_mouvement, m.description_mouvement, m.id_mission, m.id_entretien " +
            "FROM MOUVEMENT m ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE m.id_mouvement = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY m.date_mouvement DESC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY m.date_mouvement DESC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO MOUVEMENT (id_compte_societaire, date_mouvement, montant_mouvement, type_mouvement, description_mouvement, id_mission, id_entretien) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE MOUVEMENT SET id_compte_societaire = ?, date_mouvement = ?, montant_mouvement = ?, type_mouvement = ?, " +
            "description_mouvement = ?, id_mission = ?, id_entretien = ? WHERE id_mouvement = ?";
    private static final String SQL_DELETE = "DELETE FROM MOUVEMENT WHERE id_mouvement = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM MOUVEMENT";
    private static final String SQL_FIND_BY_COMPTE_ID = SQL_SELECT_BASE + "WHERE m.id_compte_societaire = ? ORDER BY m.date_mouvement DESC";
    private static final String SQL_FIND_BY_MISSION_ID = SQL_SELECT_BASE + "WHERE m.id_mission = ? ORDER BY m.date_mouvement ASC";
    private static final String SQL_FIND_BY_ENTRETIEN_ID = SQL_SELECT_BASE + "WHERE m.id_entretien = ? ORDER BY m.date_mouvement ASC";


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Mouvement> findById(Integer id) {
        if (id == null) return Optional.empty();
        Mouvement mouvement = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    mouvement = mapResultSetToMouvement(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Mouvement par ID: " + id, e);
        }
        return Optional.ofNullable(mouvement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAll() {
        List<Mouvement> mouvements = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                mouvements.add(mapResultSetToMouvement(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les Mouvements", e);
        }
        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Mouvement> mouvements = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Mouvements", e);
        }
        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mouvement save(Mouvement entity) {
        if (entity == null || entity.getCompteSocietaire() == null || entity.getCompteSocietaire().getIdCompteSocietaire() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'un Mouvement nul ou sans compte sociétaire associé.");
            return null; // id_compte_societaire est NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapMouvementToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Mouvement a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdMouvement(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Mouvement a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Mouvement pour compte ID: " + entity.getCompteSocietaire().getIdCompteSocietaire(), e);
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
    public Mouvement update(Mouvement entity) {
        if (entity == null || entity.getIdMouvement() == null || entity.getCompteSocietaire() == null || entity.getCompteSocietaire().getIdCompteSocietaire() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'un Mouvement nul, sans ID, ou sans compte sociétaire associé.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapMouvementToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Mouvement ID: {0}", entity.getIdMouvement());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Mouvement ID: " + entity.getIdMouvement(), e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Mouvement ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Mouvements", e);
        }
        return count;
    }

    /**
     * Recherche tous les mouvements pour un compte sociétaire spécifique.
     * @param idCompteSocietaire L'ID du compte sociétaire.
     * @return Une liste de mouvements.
     */
    public List<Mouvement> findByCompteId(int idCompteSocietaire) {
        List<Mouvement> mouvements = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_COMPTE_ID)) {
            pstmt.setInt(1, idCompteSocietaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Mouvements pour le compte ID: " + idCompteSocietaire, e);
        }
        return mouvements;
    }

    /**
     * Recherche tous les mouvements associés à une mission spécifique.
     * @param idMission L'ID de la mission.
     * @return Une liste de mouvements.
     */
    public List<Mouvement> findByMissionId(int idMission) {
        List<Mouvement> mouvements = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_MISSION_ID)) {
            pstmt.setInt(1, idMission);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Mouvements pour la mission ID: " + idMission, e);
        }
        return mouvements;
    }

    /**
     * Recherche tous les mouvements associés à un entretien spécifique.
     * @param idEntretien L'ID de l'entretien.
     * @return Une liste de mouvements.
     */
    public List<Mouvement> findByEntretienId(int idEntretien) {
        List<Mouvement> mouvements = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ENTRETIEN_ID)) {
            pstmt.setInt(1, idEntretien);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Mouvements pour l'entretien ID: " + idEntretien, e);
        }
        return mouvements;
    }


    private Mouvement mapResultSetToMouvement(ResultSet rs) throws SQLException {
        Mouvement mouvement = new Mouvement();
        mouvement.setIdMouvement(rs.getInt("id_mouvement"));

        int idCompte = rs.getInt("id_compte_societaire");
        if (!rs.wasNull()) {
            SocietaireCompte compte = new SocietaireCompte(); // Partiellement chargé
            compte.setIdCompteSocietaire(idCompte);
            mouvement.setCompteSocietaire(compte);
        }

        Timestamp dateMouvementTs = rs.getTimestamp("date_mouvement");
        if (dateMouvementTs != null) {
            mouvement.setDateMouvement(dateMouvementTs.toLocalDateTime());
        }
        mouvement.setMontantMouvement(rs.getBigDecimal("montant_mouvement"));

        String typeMouvementDb = rs.getString("type_mouvement");
        if (typeMouvementDb != null) {
            try {
                mouvement.setTypeMouvement(TypeMouvement.fromString(typeMouvementDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "TypeMouvement inconnu '" + typeMouvementDb + "' pour Mouvement ID: " + mouvement.getIdMouvement() + ". Valeurs attendues: " + TypeMouvement.getValidValues(), e);
            }
        }
        mouvement.setDescriptionMouvement(rs.getString("description_mouvement"));

        int idMission = rs.getInt("id_mission");
        if (!rs.wasNull()) {
            Mission mission = new Mission(); // Partiellement chargé
            mission.setIdMission(idMission);
            mouvement.setMission(mission);
        }

        int idEntretien = rs.getInt("id_entretien");
        if (!rs.wasNull()) {
            Entretien entretien = new Entretien(); // Partiellement chargé
            entretien.setIdOt(idEntretien);
            mouvement.setEntretien(entretien);
        }
        return mouvement;
    }

    private void mapMouvementToPreparedStatement(Mouvement entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        pstmt.setInt(paramIndex++, entity.getCompteSocietaire().getIdCompteSocietaire()); // id_compte_societaire NOT NULL

        if (entity.getDateMouvement() != null) { // date_mouvement NOT NULL
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateMouvement()));
        } else {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(LocalDateTime.now())); // Ou gérer l'erreur
        }
        if (entity.getMontantMouvement() != null) { // montant_mouvement NOT NULL
            pstmt.setBigDecimal(paramIndex++, entity.getMontantMouvement());
        } else {
            pstmt.setNull(paramIndex++, Types.DECIMAL); // Devrait être géré
        }
        if (entity.getTypeMouvement() != null) { // type_mouvement NOT NULL
            pstmt.setString(paramIndex++, entity.getTypeMouvement().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // Devrait être géré
        }
        pstmt.setString(paramIndex++, entity.getDescriptionMouvement());

        if (entity.getMission() != null && entity.getMission().getIdMission() != null) {
            pstmt.setInt(paramIndex++, entity.getMission().getIdMission());
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }
        if (entity.getEntretien() != null && entity.getEntretien().getIdOt() != null) {
            pstmt.setInt(paramIndex++, entity.getEntretien().getIdOt());
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdMouvement());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Mouvement.", ex);
            }
        }
    }
}