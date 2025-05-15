package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.document.DocumentSocietaire;

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
 * Implémentation du Repository pour l'entité {@link DocumentSocietaire}.
 * Gère les opérations CRUD pour les documents sociétaires.
 */
public class DocumentSocietaireRepositoryImpl implements DocumentRepository {

    private static final Logger LOGGER = Logger.getLogger(DocumentSocietaireRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT ds.id_document_societaire, ds.id_societaire, ds.nom_document, ds.type_document, " +
            "ds.chemin_acces_document, ds.date_upload_document, ds.taille_document_ko " +
            "FROM DOCUMENT_SOCIETAIRE ds ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE ds.id_document_societaire = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY ds.date_upload_document DESC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY ds.date_upload_document DESC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO DOCUMENT_SOCIETAIRE (id_societaire, nom_document, type_document, chemin_acces_document, date_upload_document, taille_document_ko) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE DOCUMENT_SOCIETAIRE SET id_societaire = ?, nom_document = ?, type_document = ?, chemin_acces_document = ?, " +
            "date_upload_document = ?, taille_document_ko = ? WHERE id_document_societaire = ?";
    private static final String SQL_DELETE = "DELETE FROM DOCUMENT_SOCIETAIRE WHERE id_document_societaire = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM DOCUMENT_SOCIETAIRE";
    private static final String SQL_FIND_BY_SOCIETAIRE_ID = SQL_SELECT_BASE + "WHERE ds.id_societaire = ? ORDER BY ds.date_upload_document DESC";
    private static final String SQL_FIND_BY_TYPE = SQL_SELECT_BASE + "WHERE ds.type_document = ? ORDER BY ds.date_upload_document DESC";
    private static final String SQL_FIND_BY_NOM_CONTAINING = SQL_SELECT_BASE + "WHERE ds.nom_document LIKE ? ORDER BY ds.date_upload_document DESC";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DocumentSocietaire> findById(Integer id) {
        if (id == null) return Optional.empty();
        DocumentSocietaire document = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    document = mapResultSetToDocumentSocietaire(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de DocumentSocietaire par ID: " + id, e);
        }
        return Optional.ofNullable(document);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> findAll() {
        List<DocumentSocietaire> documents = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                documents.add(mapResultSetToDocumentSocietaire(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les DocumentSocietaires", e);
        }
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<DocumentSocietaire> documents = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des DocumentSocietaires", e);
        }
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentSocietaire save(DocumentSocietaire entity) {
        if (entity == null || entity.getSocietaire() == null || entity.getSocietaire().getIdSocietaire() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'un DocumentSocietaire nul ou sans societaire associé.");
            return null; // id_societaire est NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapDocumentSocietaireToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de DocumentSocietaire a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdDocumentSocietaire(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de DocumentSocietaire a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de DocumentSocietaire: " + entity.getNomDocument(), e);
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
    public DocumentSocietaire update(DocumentSocietaire entity) {
        if (entity == null || entity.getIdDocumentSocietaire() == null || entity.getSocietaire() == null || entity.getSocietaire().getIdSocietaire() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'un DocumentSocietaire nul, sans ID, ou sans societaire associé.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapDocumentSocietaireToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour DocumentSocietaire ID: {0}", entity.getIdDocumentSocietaire());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de DocumentSocietaire ID: " + entity.getIdDocumentSocietaire(), e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de DocumentSocietaire ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des DocumentSocietaires", e);
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> findBySocietaire(Societaire societaire) {
        if (societaire == null || societaire.getIdSocietaire() == null) {
            return new ArrayList<>();
        }
        return findBySocietaireId(societaire.getIdSocietaire());
    }

    /**
     * Recherche tous les documents pour un sociétaire spécifique par son ID.
     * @param idSocietaire L'ID du sociétaire.
     * @return Une liste de documents.
     */
    public List<DocumentSocietaire> findBySocietaireId(int idSocietaire) {
        List<DocumentSocietaire> documents = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_SOCIETAIRE_ID)) {
            pstmt.setInt(1, idSocietaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de DocumentSocietaire pour le societaire ID: " + idSocietaire, e);
        }
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> findByType(TypeDocumentSocietaire typeDocument) {
        if (typeDocument == null) {
            return new ArrayList<>();
        }
        List<DocumentSocietaire> documents = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_TYPE)) {
            pstmt.setString(1, typeDocument.getValeurDb());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de DocumentSocietaire par type: " + typeDocument, e);
        }
        return documents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> findByNomContaining(String nomPartiel) {
        if (nomPartiel == null || nomPartiel.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<DocumentSocietaire> documents = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_NOM_CONTAINING)) {
            pstmt.setString(1, "%" + nomPartiel + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de DocumentSocietaire contenant '" + nomPartiel + "' dans le nom", e);
        }
        return documents;
    }

    private DocumentSocietaire mapResultSetToDocumentSocietaire(ResultSet rs) throws SQLException {
        DocumentSocietaire doc = new DocumentSocietaire();
        doc.setIdDocumentSocietaire(rs.getInt("id_document_societaire"));

        int idSocietaire = rs.getInt("id_societaire");
        if (!rs.wasNull()) {
            Societaire societaire = new Societaire(); // Partiellement chargé
            societaire.setIdSocietaire(idSocietaire);
            doc.setSocietaire(societaire);
        }

        doc.setNomDocument(rs.getString("nom_document"));
        String typeDocDb = rs.getString("type_document");
        if (typeDocDb != null) {
            try {
                doc.setTypeDocument(TypeDocumentSocietaire.fromString(typeDocDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "TypeDocumentSocietaire inconnu '" + typeDocDb + "' pour Document ID: " + doc.getIdDocumentSocietaire() + ". Valeurs attendues: " + TypeDocumentSocietaire.getValidValues(), e);
            }
        }
        doc.setCheminAccesDocument(rs.getString("chemin_acces_document"));
        Timestamp dateUploadTs = rs.getTimestamp("date_upload_document");
        if (dateUploadTs != null) {
            doc.setDateUploadDocument(dateUploadTs.toLocalDateTime());
        }
        doc.setTailleDocumentKo(rs.getObject("taille_document_ko", Integer.class));
        return doc;
    }

    private void mapDocumentSocietaireToPreparedStatement(DocumentSocietaire entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        pstmt.setInt(paramIndex++, entity.getSocietaire().getIdSocietaire());
        pstmt.setString(paramIndex++, entity.getNomDocument());

        if (entity.getTypeDocument() != null) {
            pstmt.setString(paramIndex++, entity.getTypeDocument().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR);
        }
        pstmt.setString(paramIndex++, entity.getCheminAccesDocument());

        if (entity.getDateUploadDocument() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateUploadDocument()));
        } else {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(LocalDateTime.now()));
        }
        pstmt.setObject(paramIndex++, entity.getTailleDocumentKo(), Types.INTEGER);

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdDocumentSocietaire());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction DocumentSocietaire.", ex);
            }
        }
    }
}