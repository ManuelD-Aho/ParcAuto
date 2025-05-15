package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Fonction;
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.rh.Service;
import main.java.com.miage.parcauto.model.rh.Sexe;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Personnel}.
 * Gère les opérations CRUD pour les membres du personnel.
 */
public class PersonnelRepositoryImpl implements Repository<Personnel, Integer> {

    private static final Logger LOGGER = Logger.getLogger(PersonnelRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT p.id_personnel, p.nom, p.prenom, p.date_naissance, p.sexe, p.adresse, " +
            "p.telephone, p.email, p.date_embauche, " +
            "p.id_fonction as f_id, f.lib_fonction as f_lib, " +
            "p.id_service as s_id, s.lib_service as s_lib " +
            "FROM PERSONNEL p " +
            "LEFT JOIN FONCTION f ON p.id_fonction = f.id_fonction " +
            "LEFT JOIN SERVICE s ON p.id_service = s.id_service ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE p.id_personnel = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY p.nom ASC, p.prenom ASC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY p.nom ASC, p.prenom ASC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO PERSONNEL (nom, prenom, date_naissance, sexe, adresse, telephone, email, date_embauche, id_fonction, id_service) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE PERSONNEL SET nom = ?, prenom = ?, date_naissance = ?, sexe = ?, adresse = ?, " +
            "telephone = ?, email = ?, date_embauche = ?, id_fonction = ?, id_service = ? " +
            "WHERE id_personnel = ?";
    private static final String SQL_DELETE = "DELETE FROM PERSONNEL WHERE id_personnel = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM PERSONNEL";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Personnel> findById(Integer id) {
        if (id == null) return Optional.empty();
        Personnel personnel = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    personnel = mapResultSetToPersonnel(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Personnel par ID: " + id, e);
        }
        return Optional.ofNullable(personnel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAll() {
        List<Personnel> personnels = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                personnels.add(mapResultSetToPersonnel(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les Personnels", e);
        }
        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Personnel> personnels = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Personnels", e);
        }
        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Personnel save(Personnel entity) {
        if (entity == null) return null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapPersonnelToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Personnel a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdPersonnel(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Personnel a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Personnel: " + entity.getNomComplet(), e);
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
    public Personnel update(Personnel entity) {
        if (entity == null || entity.getIdPersonnel() == null) return null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapPersonnelToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Personnel ID: {0}", entity.getIdPersonnel());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Personnel ID: " + entity.getIdPersonnel(), e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Personnel ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Personnels", e);
        }
        return count;
    }

    private Personnel mapResultSetToPersonnel(ResultSet rs) throws SQLException {
        Personnel personnel = new Personnel();
        personnel.setIdPersonnel(rs.getInt("id_personnel"));
        personnel.setNom(rs.getString("nom"));
        personnel.setPrenom(rs.getString("prenom"));
        Date dateNaissanceDb = rs.getDate("date_naissance");
        if (dateNaissanceDb != null) {
            personnel.setDateNaissance(dateNaissanceDb.toLocalDate());
        }
        String sexeDb = rs.getString("sexe");
        if (sexeDb != null) {
            try {
                personnel.setSexe(Sexe.fromString(sexeDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Sexe inconnu '" + sexeDb + "' trouvé pour Personnel ID: " + personnel.getIdPersonnel(), e);
            }
        }
        personnel.setAdresse(rs.getString("adresse"));
        personnel.setTelephone(rs.getString("telephone"));
        personnel.setEmail(rs.getString("email"));
        Date dateEmbaucheDb = rs.getDate("date_embauche");
        if (dateEmbaucheDb != null) {
            personnel.setDateEmbauche(dateEmbaucheDb.toLocalDate());
        }

        int idFonction = rs.getInt("f_id");
        if (!rs.wasNull()) {
            Fonction fonction = new Fonction();
            fonction.setIdFonction(idFonction);
            fonction.setLibFonction(rs.getString("f_lib"));
            personnel.setFonction(fonction);
        }

        int idService = rs.getInt("s_id");
        if (!rs.wasNull()) {
            Service service = new Service();
            service.setIdService(idService);
            service.setLibService(rs.getString("s_lib"));
            personnel.setService(service);
        }
        return personnel;
    }

    private void mapPersonnelToPreparedStatement(Personnel entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;
        pstmt.setString(paramIndex++, entity.getNom());
        pstmt.setString(paramIndex++, entity.getPrenom());
        if (entity.getDateNaissance() != null) {
            pstmt.setDate(paramIndex++, Date.valueOf(entity.getDateNaissance()));
        } else {
            pstmt.setNull(paramIndex++, Types.DATE);
        }
        if (entity.getSexe() != null) {
            pstmt.setString(paramIndex++, entity.getSexe().getValeurDb()); // Assumes Sexe has getValeurDb() for DB ENUM/VARCHAR
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR);
        }
        pstmt.setString(paramIndex++, entity.getAdresse());
        pstmt.setString(paramIndex++, entity.getTelephone());
        pstmt.setString(paramIndex++, entity.getEmail());
        if (entity.getDateEmbauche() != null) {
            pstmt.setDate(paramIndex++, Date.valueOf(entity.getDateEmbauche()));
        } else {
            pstmt.setNull(paramIndex++, Types.DATE);
        }

        if (entity.getFonction() != null && entity.getFonction().getIdFonction() != null) {
            pstmt.setInt(paramIndex++, entity.getFonction().getIdFonction());
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }
        if (entity.getService() != null && entity.getService().getIdService() != null) {
            pstmt.setInt(paramIndex++, entity.getService().getIdService());
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdPersonnel());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Personnel.", ex);
            }
        }
    }
}