package com.miage.parcauto.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.miage.parcauto.model.document.Document;
import com.miage.parcauto.model.document.TypeDocument;
import com.miage.parcauto.model.finance.SocieteCompte;

/**
 * Classe DAO pour la gestion des documents sociétaires dans la base de données.
 * Gère les opérations CRUD pour les documents et leur stockage physique.
 *
 * @author MIAGE Holding - ParcAuto
 * @version 1.0
 */
public class DocumentDao {

    private static final Logger LOGGER = Logger.getLogger(DocumentDao.class.getName());

    // Chemin racine pour le stockage des documents (configurable)
    private static final String DOCUMENT_ROOT_PATH = System.getProperty("user.dir") + File.separator + "documents";

    /**
     * Constructeur par défaut qui initialise le répertoire racine des documents
     */
    public DocumentDao() {
        initDocumentDirectory();
    }

    /**
     * Initialise le répertoire de stockage des documents s'il n'existe pas
     */
    private void initDocumentDirectory() {
        try {
            Path rootPath = Paths.get(DOCUMENT_ROOT_PATH);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
                LOGGER.info("Répertoire de stockage des documents créé: " + rootPath);
            }

            // Créer des sous-répertoires pour chaque type de document
            for (TypeDocument type : TypeDocument.values()) {
                Path typePath = Paths.get(DOCUMENT_ROOT_PATH, type.name().toLowerCase());
                if (!Files.exists(typePath)) {
                    Files.createDirectories(typePath);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation du répertoire de documents", e);
        }
    }

    /**
     * Récupère tous les documents de la base de données.
     *
     * @return liste de tous les documents
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Document> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT d.*, s.nom, s.numero " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE s ON d.id_sociétaire = s.id_sociétaire " +
                    "ORDER BY d.date_upload DESC";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(buildDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les documents", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère un document par son identifiant.
     *
     * @param idDoc identifiant du document
     * @return un Optional contenant le document ou un Optional vide si non trouvé
     * @throws SQLException si une erreur de base de données survient
     */
    public Optional<Document> findById(int idDoc) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT d.*, s.nom, s.numero " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE s ON d.id_sociétaire = s.id_sociétaire " +
                    "WHERE d.id_doc = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idDoc);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(buildDocumentFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du document ID: " + idDoc, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les documents d'un sociétaire.
     *
     * @param idSocietaire identifiant du sociétaire
     * @return liste des documents du sociétaire
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Document> findBySocietaire(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT d.*, s.nom, s.numero " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE s ON d.id_sociétaire = s.id_sociétaire " +
                    "WHERE d.id_sociétaire = ? " +
                    "ORDER BY d.date_upload DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(buildDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents pour le sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les documents d'un certain type.
     *
     * @param typeDoc type de document
     * @return liste des documents du type spécifié
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Document> findByType(TypeDocument typeDoc) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT d.*, s.nom, s.numero " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE s ON d.id_sociétaire = s.id_sociétaire " +
                    "WHERE d.type_doc = ? " +
                    "ORDER BY d.date_upload DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, typeDoc.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(buildDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents de type: " + typeDoc, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Vérifie si un sociétaire possède un document d'un type spécifique.
     *
     * @param idSocietaire identifiant du sociétaire
     * @param typeDoc type de document
     * @return true si le sociétaire possède déjà ce type de document
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean hasDocumentType(int idSocietaire, TypeDocument typeDoc) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT COUNT(*) FROM DOCUMENT_SOCIETAIRE " +
                    "WHERE id_sociétaire = ? AND type_doc = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);
            pstmt.setString(2, typeDoc.name());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification du document de type " + typeDoc +
                    " pour le sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Enregistre un nouveau document dans la base de données et le fichier physique.
     *
     * @param document objet Document avec les métadonnées
     * @param fichierSource chemin vers le fichier source à copier
     * @return identifiant du document créé
     * @throws SQLException si une erreur de base de données survient
     * @throws IOException si une erreur de manipulation de fichier survient
     */
    public int save(Document document, Path fichierSource) throws SQLException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Vérifier que le fichier existe
            if (!Files.exists(fichierSource)) {
                throw new IOException("Le fichier source n'existe pas: " + fichierSource);
            }

            // Vérifier que le sociétaire existe
            if (document.getSocietaire() == null || document.getSocietaire().getIdSocietaire() == null) {
                throw new SQLException("Le sociétaire n'est pas défini pour ce document");
            }

            // Générer un nom de fichier unique
            String extension = getFileExtension(fichierSource.toString());
            String nomFichier = generateUniqueFileName(document.getSocietaire().getIdSocietaire(),
                    document.getTypeDoc(), extension);

            // Déterminer le chemin de destination
            String cheminRelatif = document.getTypeDoc().name().toLowerCase() + File.separator + nomFichier;
            Path cheminDestination = Paths.get(DOCUMENT_ROOT_PATH, cheminRelatif);

            // Stocker le chemin relatif dans l'objet document
            document.setCheminFichier(cheminRelatif);

            // Enregistrer les métadonnées du document en base
            conn = DbUtil.getConnection();

            String sql = "INSERT INTO DOCUMENT_SOCIETAIRE (id_sociétaire, type_doc, chemin_fichier, date_upload) " +
                    "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, document.getSocietaire().getIdSocietaire());
            pstmt.setString(2, document.getTypeDoc().name());
            pstmt.setString(3, cheminRelatif);
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du document a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);

                // Copier le fichier vers sa destination
                Files.createDirectories(cheminDestination.getParent());
                Files.copy(fichierSource, cheminDestination, StandardCopyOption.REPLACE_EXISTING);

                DbUtil.commitTransaction(conn);
                return generatedId;
            } else {
                throw new SQLException("La création du document a échoué, aucun ID obtenu.");
            }

        } catch (SQLException | IOException e) {
            if (conn != null) {
                DbUtil.rollbackTransaction(conn);
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du document", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Met à jour les métadonnées d'un document existant.
     * Note: Cette méthode ne modifie pas le fichier physique, seulement les métadonnées.
     *
     * @param document document à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean update(Document document) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "UPDATE DOCUMENT_SOCIETAIRE SET id_sociétaire = ?, type_doc = ? WHERE id_doc = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, document.getSocietaire().getIdSocietaire());
            pstmt.setString(2, document.getTypeDoc().name());
            pstmt.setInt(3, document.getIdDoc());

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du document ID: " + document.getIdDoc(), e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Remplace le fichier physique d'un document existant.
     *
     * @param idDoc identifiant du document
     * @param nouveauFichier chemin vers le nouveau fichier
     * @return true si le remplacement a réussi
     * @throws SQLException si une erreur de base de données survient
     * @throws IOException si une erreur de manipulation de fichier survient
     */
    public boolean remplacerFichier(int idDoc, Path nouveauFichier) throws SQLException, IOException {
        Connection conn = null;

        try {
            // Vérifier que le nouveau fichier existe
            if (!Files.exists(nouveauFichier)) {
                throw new IOException("Le fichier source n'existe pas: " + nouveauFichier);
            }

            conn = DbUtil.getConnection();

            // Récupérer le document existant
            Optional<Document> documentOpt = findById(idDoc);
            if (!documentOpt.isPresent()) {
                throw new SQLException("Le document à mettre à jour n'existe pas");
            }

            Document document = documentOpt.get();
            Path ancienFichier = Paths.get(DOCUMENT_ROOT_PATH, document.getCheminFichier());

            // Copier le nouveau fichier par-dessus l'ancien
            Files.copy(nouveauFichier, ancienFichier, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour la date de téléchargement
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE DOCUMENT_SOCIETAIRE SET date_upload = ? WHERE id_doc = ?");
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, idDoc);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.closePreparedStatement(pstmt);
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException | IOException e) {
            if (conn != null) {
                DbUtil.rollbackTransaction(conn);
            }
            LOGGER.log(Level.SEVERE, "Erreur lors du remplacement du fichier pour le document ID: " + idDoc, e);
            throw e;
        }
    }

    /**
     * Supprime un document de la base de données et le fichier physique associé.
     *
     * @param idDoc identifiant du document à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean delete(int idDoc) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            // Récupérer le chemin du fichier avant suppression
            Optional<Document> documentOpt = findById(idDoc);
            if (!documentOpt.isPresent()) {
                return false; // Document n'existe pas
            }

            Document document = documentOpt.get();
            String cheminFichier = document.getCheminFichier();

            // Supprimer l'enregistrement en base de données
            String sql = "DELETE FROM DOCUMENT_SOCIETAIRE WHERE id_doc = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idDoc);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Supprimer le fichier physique
                try {
                    Path fichierASupprimer = Paths.get(DOCUMENT_ROOT_PATH, cheminFichier);
                    Files.deleteIfExists(fichierASupprimer);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Impossible de supprimer le fichier physique: " + cheminFichier, e);
                    // On continue malgré l'échec de la suppression du fichier physique
                }

                DbUtil.commitTransaction(conn);
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du document ID: " + idDoc, e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère le chemin absolu d'un fichier document.
     *
     * @param document document dont on veut le chemin
     * @return chemin absolu du fichier
     */
    public Path getAbsolutePath(Document document) {
        if (document == null || document.getCheminFichier() == null) {
            return null;
        }

        return Paths.get(DOCUMENT_ROOT_PATH, document.getCheminFichier());
    }

    /**
     * Vérifie si un document existe physiquement sur le système de fichiers.
     *
     * @param document document à vérifier
     * @return true si le fichier existe
     */
    public boolean existsPhysically(Document document) {
        Path path = getAbsolutePath(document);
        return path != null && Files.exists(path);
    }

    /**
     * Exporte une copie d'un document vers un emplacement spécifié.
     *
     * @param idDoc identifiant du document
     * @param destination chemin de destination pour la copie
     * @return true si l'export a réussi
     * @throws SQLException si une erreur de base de données survient
     * @throws IOException si une erreur de manipulation de fichier survient
     */
    public boolean exportDocument(int idDoc, Path destination) throws SQLException, IOException {
        // Récupérer le document
        Optional<Document> documentOpt = findById(idDoc);
        if (!documentOpt.isPresent()) {
            throw new SQLException("Le document n'existe pas");
        }

        Document document = documentOpt.get();
        Path sourcePath = getAbsolutePath(document);

        if (!Files.exists(sourcePath)) {
            throw new IOException("Le fichier source n'existe pas: " + sourcePath);
        }

        // Créer le répertoire de destination si nécessaire
        if (!Files.exists(destination.getParent())) {
            Files.createDirectories(destination.getParent());
        }

        // Copier le fichier
        Files.copy(sourcePath, destination, StandardCopyOption.REPLACE_EXISTING);
        return true;
    }

    /**
     * Recherche des documents selon plusieurs critères.
     *
     * @param idSocietaire identifiant du sociétaire (peut être null)
     * @param typeDoc type de document (peut être null)
     * @param dateDebut date de début de la période (peut être null)
     * @param dateFin date de fin de la période (peut être null)
     * @return liste des documents correspondant aux critères
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Document> searchDocuments(Integer idSocietaire, TypeDocument typeDoc,
                                          LocalDateTime dateDebut, LocalDateTime dateFin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT d.*, s.nom, s.numero ");
            sqlBuilder.append("FROM DOCUMENT_SOCIETAIRE d ");
            sqlBuilder.append("JOIN SOCIETAIRE_COMPTE s ON d.id_sociétaire = s.id_sociétaire ");
            sqlBuilder.append("WHERE 1=1 ");

            List<Object> params = new ArrayList<>();

            if (idSocietaire != null) {
                sqlBuilder.append("AND d.id_sociétaire = ? ");
                params.add(idSocietaire);
            }

            if (typeDoc != null) {
                sqlBuilder.append("AND d.type_doc = ? ");
                params.add(typeDoc.name());
            }

            if (dateDebut != null) {
                sqlBuilder.append("AND d.date_upload >= ? ");
                params.add(Timestamp.valueOf(dateDebut));
            }

            if (dateFin != null) {
                sqlBuilder.append("AND d.date_upload <= ? ");
                params.add(Timestamp.valueOf(dateFin));
            }

            sqlBuilder.append("ORDER BY d.date_upload DESC");

            pstmt = conn.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(buildDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de documents", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Construit un objet Document à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du document
     * @return objet Document construit
     * @throws SQLException si une erreur de base de données survient
     */
    private Document buildDocumentFromResultSet(ResultSet rs) throws SQLException {
        Document document = new Document();
        document.setIdDoc(rs.getInt("id_doc"));

        // Type de document (enum)
        String typeDocStr = rs.getString("type_doc");
        document.setTypeDoc(TypeDocument.valueOf(typeDocStr));

        document.setCheminFichier(rs.getString("chemin_fichier"));

        // Date d'upload
        Timestamp dateUpload = rs.getTimestamp("date_upload");
        if (dateUpload != null) {
            document.setDateUpload(dateUpload.toLocalDateTime());
        }

        // Sociétaire associé
        SocieteCompte societaire = new SocieteCompte();
        societaire.setIdSocietaire(rs.getInt("id_sociétaire"));

        // Récupérer le nom et numéro du sociétaire si disponibles
        try {
            societaire.setNom(rs.getString("nom"));
            societaire.setNumero(rs.getString("numero"));
        } catch (SQLException e) {
            // Ces colonnes peuvent ne pas être présentes dans certaines requêtes
            LOGGER.log(Level.FINE, "Certaines colonnes de société ne sont pas présentes dans le ResultSet");
        }

        document.setSocietaire(societaire);

        return document;
    }

    /**
     * Génère un nom de fichier unique pour un document.
     *
     * @param idSocietaire identifiant du sociétaire
     * @param typeDoc type de document
     * @param extension extension du fichier (avec le point)
     * @return nom de fichier unique
     */
    private String generateUniqueFileName(int idSocietaire, TypeDocument typeDoc, String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return "sociétaire_" + idSocietaire + "_" + typeDoc.name().toLowerCase() +
                "_" + timestamp + "_" + uuid + extension;
    }

    /**
     * Extrait l'extension d'un nom de fichier.
     *
     * @param fileName nom du fichier
     * @return extension du fichier avec le point (ex: ".pdf")
     */
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot).toLowerCase();
        }
        return "";
    }
}