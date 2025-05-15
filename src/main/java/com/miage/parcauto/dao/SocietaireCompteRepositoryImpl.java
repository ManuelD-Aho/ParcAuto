package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.SocietaireCompte;
import main.java.com.miage.parcauto.model.finance.TypeCompteSocietaire; // Assurez-vous que cette énumération existe
import main.java.com.miage.parcauto.model.rh.Societaire; // Supposant une entité Societaire, sinon ajuster

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
 * Implémentation du Repository pour l'entité {@link SocietaireCompte}.
 * Gère les opérations CRUD pour les comptes des sociétaires.
 */
public class SocietaireCompteRepositoryImpl implements Repository<SocietaireCompte, Integer> {

    private static final Logger LOGGER = Logger.getLogger(SocietaireCompteRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT sc.id_compte_societaire, sc.id_societaire, sc.num_compte, " +
            "sc.solde_actuel, sc.date_creation_compte, sc.type_compte " +
            "FROM SOCIETAIRE_COMPTE sc ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE sc.id_compte_societaire = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY sc.num_compte ASC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY sc.num_compte ASC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO SOCIETAIRE_COMPTE (id_societaire, num_compte, solde_actuel, date_creation_compte, type_compte) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE SOCIETAIRE_COMPTE SET id_societaire = ?, num_compte = ?, solde_actuel = ?, " +
            "date_creation_compte = ?, type_compte = ? WHERE id_compte_societaire = ?";
    private static final String SQL_DELETE = "DELETE FROM SOCIETAIRE_COMPTE WHERE id_compte_societaire = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM SOCIETAIRE_COMPTE";
    private static final String SQL_FIND_BY_NUM_COMPTE = SQL_SELECT_BASE + "WHERE sc.num_compte = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SocietaireCompte> findById(Integer id) {
        if (id == null) return Optional.empty();
        SocietaireCompte compte = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    compte = mapResultSetToSocietaireCompte(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de SocietaireCompte par ID: " + id, e);
        }
        return Optional.ofNullable(compte);
    }

    /**
     * Recherche un compte sociétaire par son numéro de compte.
     * @param numCompte Le numéro de compte unique.
     * @return Un Optional contenant le compte s'il est trouvé.
     */
    public Optional<SocietaireCompte> findByNumCompte(String numCompte) {
        if (numCompte == null || numCompte.trim().isEmpty()) return Optional.empty();
        SocietaireCompte compte = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_NUM_COMPTE)) {
            pstmt.setString(1, numCompte);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    compte = mapResultSetToSocietaireCompte(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de SocietaireCompte par num_compte: " + numCompte, e);
        }
        return Optional.ofNullable(compte);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocietaireCompte> findAll() {
        List<SocietaireCompte> comptes = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                comptes.add(mapResultSetToSocietaireCompte(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les SocietaireComptes", e);
        }
        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocietaireCompte> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<SocietaireCompte> comptes = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToSocietaireCompte(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des SocietaireComptes", e);
        }
        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SocietaireCompte save(SocietaireCompte entity) {
        if (entity == null || entity.getSocietaire() == null || entity.getSocietaire().getIdSocietaire() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'un SocietaireCompte nul ou sans societaire associé.");
            return null; // id_societaire est NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapSocietaireCompteToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de SocietaireCompte a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdCompteSocietaire(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de SocietaireCompte a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de SocietaireCompte: " + entity.getNumCompte(), e);
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
    public SocietaireCompte update(SocietaireCompte entity) {
        if (entity == null || entity.getIdCompteSocietaire() == null || entity.getSocietaire() == null || entity.getSocietaire().getIdSocietaire() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'un SocietaireCompte nul, sans ID, ou sans societaire associé.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapSocietaireCompteToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour SocietaireCompte ID: {0}", entity.getIdCompteSocietaire());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de SocietaireCompte ID: " + entity.getIdCompteSocietaire(), e);
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
            // Attention: la suppression d'un compte peut avoir des implications sur les mouvements associés.
            // Une suppression en cascade ou une vérification préalable dans un service serait nécessaire.
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de SocietaireCompte ID: " + id, e);
            if (e.getSQLState().startsWith("23")) {
                LOGGER.log(Level.WARNING, "Impossible de supprimer SocietaireCompte ID: {0}. Il est probablement référencé par des Mouvements.", id);
            }
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des SocietaireComptes", e);
        }
        return count;
    }

    private SocietaireCompte mapResultSetToSocietaireCompte(ResultSet rs) throws SQLException {
        SocietaireCompte compte = new SocietaireCompte();
        compte.setIdCompteSocietaire(rs.getInt("id_compte_societaire"));

        int idSocietaire = rs.getInt("id_societaire");
        if (!rs.wasNull()) {
            Societaire societaire = new Societaire(); // Partiellement chargé
            societaire.setIdSocietaire(idSocietaire); // Assurez-vous que Societaire a setIdSocietaire
            compte.setSocietaire(societaire);
        }

        compte.setNumCompte(rs.getString("num_compte"));
        compte.setSoldeActuel(rs.getBigDecimal("solde_actuel"));
        Timestamp dateCreationTs = rs.getTimestamp("date_creation_compte");
        if (dateCreationTs != null) {
            compte.setDateCreationCompte(dateCreationTs.toLocalDateTime());
        }

        String typeCompteDb = rs.getString("type_compte");
        if (typeCompteDb != null) {
            try {
                compte.setTypeCompte(TypeCompteSocietaire.fromString(typeCompteDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "TypeCompteSocietaire inconnu '" + typeCompteDb + "' pour Compte ID: " + compte.getIdCompteSocietaire() + ". Valeurs attendues: " + TypeCompteSocietaire.getValidValues(), e);
            }
        }
        return compte;
    }

    private void mapSocietaireCompteToPreparedStatement(SocietaireCompte entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        pstmt.setInt(paramIndex++, entity.getSocietaire().getIdSocietaire()); // id_societaire NOT NULL
        pstmt.setString(paramIndex++, entity.getNumCompte()); // num_compte NOT NULL

        if (entity.getSoldeActuel() != null) { // solde_actuel NOT NULL
            pstmt.setBigDecimal(paramIndex++, entity.getSoldeActuel());
        } else {
            pstmt.setBigDecimal(paramIndex++, BigDecimal.ZERO); // Ou gérer l'erreur si solde ne peut être null
        }
        if (entity.getDateCreationCompte() != null) { // date_creation_compte NOT NULL
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateCreationCompte()));
        } else {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(LocalDateTime.now())); // Ou gérer l'erreur
        }
        if (entity.getTypeCompte() != null) { // type_compte NOT NULL
            pstmt.setString(paramIndex++, entity.getTypeCompte().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // Devrait être géré par validation
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdCompteSocietaire());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction SocietaireCompte.", ex);
            }
        }
    }
}