package main.java.com.miage.parcauto.dao;

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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'accès aux données pour les documents des sociétaires.
 * Gère les opérations CRUD et les fonctionnalités spécifiques aux documents.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DocumentDao {

    private static final Logger LOGGER = Logger.getLogger(DocumentDao.class.getName());

    // Configuration du stockage des fichiers
    private static final String BASE_UPLOAD_DIR = "./uploads/documents";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10Mo
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "pdf", "jpg", "jpeg", "png", "doc", "docx"));

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    /**
     * Constructeur par défaut. Initialise l'instance de DbUtil.
     */
    public DocumentDao() {
        this.dbUtil = DbUtil.getInstance();
        initializeDirectories();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     */
    public DocumentDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
        initializeDirectories();
    }

    /**
     * Initialise les répertoires de stockage des documents.
     */
    private void initializeDirectories() {
        try {
            Path basePath = Paths.get(BASE_UPLOAD_DIR);
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
                LOGGER.info("Répertoire de base pour les documents créé: " + basePath);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du répertoire de documents", ex);
            throw new RuntimeException("Impossible de créer le répertoire de documents", ex);
        }
    }

    /**
     * Énumération des types de documents acceptés.
     */
    public enum TypeDoc {
        CarteGrise,
        Assurance,
        ID,
        Permis;

        /**
         * Convertit une chaîne en TypeDoc.
         *
         * @param value Valeur à convertir
         * @return Le TypeDoc correspondant ou null si non valide
         */
        public static TypeDoc fromString(String value) {
            try {
                return valueOf(value);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Classe représentant un document sociétaire.
     */
    public static class Document {
        private Integer idDoc;
        private Integer idSocietaire;
        private String nomSocietaire;
        private TypeDoc typeDoc;
        private String cheminFichier;
        private LocalDateTime dateUpload;
        private String nomOriginal;

        // Constructeur vide
        public Document() {
        }

        // Constructeur avec paramètres essentiels
        public Document(Integer idSocietaire, TypeDoc typeDoc, String cheminFichier) {
            this.idSocietaire = idSocietaire;
            this.typeDoc = typeDoc;
            this.cheminFichier = cheminFichier;
            this.dateUpload = LocalDateTime.now();
        }

        // Constructeur complet
        public Document(Integer idDoc, Integer idSocietaire, String nomSocietaire, TypeDoc typeDoc,
                String cheminFichier, LocalDateTime dateUpload, String nomOriginal) {
            this.idDoc = idDoc;
            this.idSocietaire = idSocietaire;
            this.nomSocietaire = nomSocietaire;
            this.typeDoc = typeDoc;
            this.cheminFichier = cheminFichier;
            this.dateUpload = dateUpload;
            this.nomOriginal = nomOriginal;
        }

        // Getters et setters
        public Integer getIdDoc() {
            return idDoc;
        }

        public void setIdDoc(Integer idDoc) {
            this.idDoc = idDoc;
        }

        public Integer getIdSocietaire() {
            return idSocietaire;
        }

        public void setIdSocietaire(Integer idSocietaire) {
            this.idSocietaire = idSocietaire;
        }

        public String getNomSocietaire() {
            return nomSocietaire;
        }

        public void setNomSocietaire(String nomSocietaire) {
            this.nomSocietaire = nomSocietaire;
        }

        public TypeDoc getTypeDoc() {
            return typeDoc;
        }

        public void setTypeDoc(TypeDoc typeDoc) {
            this.typeDoc = typeDoc;
        }

        public String getCheminFichier() {
            return cheminFichier;
        }

        public void setCheminFichier(String cheminFichier) {
            this.cheminFichier = cheminFichier;
        }

        public LocalDateTime getDateUpload() {
            return dateUpload;
        }

        public void setDateUpload(LocalDateTime dateUpload) {
            this.dateUpload = dateUpload;
        }

        public String getNomOriginal() {
            return nomOriginal;
        }

        public void setNomOriginal(String nomOriginal) {
            this.nomOriginal = nomOriginal;
        }

        /**
         * Retourne le nom du fichier à partir du chemin.
         *
         * @return Le nom du fichier
         */
        public String getNomFichier() {
            if (cheminFichier == null) {
                return null;
            }
            return new File(cheminFichier).getName();
        }

        /**
         * Retourne l'extension du fichier.
         *
         * @return L'extension du fichier
         */
        public String getExtension() {
            String nomFichier = getNomFichier();
            if (nomFichier == null) {
                return "";
            }
            int dot = nomFichier.lastIndexOf('.');
            if (dot < 0) {
                return "";
            }
            return nomFichier.substring(dot + 1).toLowerCase();
        }
    }

    /**
     * Récupère tous les documents de la base de données.
     *
     * @return Liste des documents
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Document> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT d.*, sc.nom " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE sc ON d.id_societaire = sc.id_societaire " +
                    "ORDER BY d.date_upload DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les documents", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un document par son ID.
     *
     * @param id ID du document à récupérer
     * @return Optional contenant le document s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Document> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT d.*, sc.nom " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE sc ON d.id_societaire = sc.id_societaire " +
                    "WHERE d.id_doc = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractDocumentFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du document par ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les documents d'un sociétaire.
     *
     * @param idSocietaire ID du sociétaire
     * @return Liste des documents du sociétaire
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Document> findBySocietaire(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT d.*, sc.nom " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE sc ON d.id_societaire = sc.id_societaire " +
                    "WHERE d.id_societaire = ? " +
                    "ORDER BY d.date_upload DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des documents pour le sociétaire ID: " + idSocietaire,
                    ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les documents d'un sociétaire par type.
     *
     * @param idSocietaire ID du sociétaire
     * @param typeDoc      Type de document recherché
     * @return Liste des documents du sociétaire du type spécifié
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Document> findBySocietaireAndType(int idSocietaire, TypeDoc typeDoc) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT d.*, sc.nom " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE sc ON d.id_societaire = sc.id_societaire " +
                    "WHERE d.id_societaire = ? AND d.type_doc = ? " +
                    "ORDER BY d.date_upload DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);
            pstmt.setString(2, typeDoc.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des documents pour le sociétaire ID: " +
                    idSocietaire + " et type: " + typeDoc, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère le document spécifique d'un sociétaire.
     * Utile pour vérifier si un type de document existe déjà pour un sociétaire.
     *
     * @param idSocietaire ID du sociétaire
     * @param typeDoc      Type de document recherché
     * @return Optional contenant le document s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Document> findDocumentSpecifique(int idSocietaire, TypeDoc typeDoc) throws SQLException {
        List<Document> documents = findBySocietaireAndType(idSocietaire, typeDoc);
        if (documents.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(documents.get(0));
    }

    /**
     * Récupère les documents par type.
     *
     * @param typeDoc Type de document recherché
     * @return Liste des documents du type spécifié
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Document> findByType(TypeDoc typeDoc) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT d.*, sc.nom " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE sc ON d.id_societaire = sc.id_societaire " +
                    "WHERE d.type_doc = ? " +
                    "ORDER BY d.date_upload DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, typeDoc.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des documents par type: " + typeDoc, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Recherche de documents par critères (nom, chemin, etc.).
     *
     * @param searchTerm Terme de recherche
     * @return Liste des documents correspondant aux critères
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Document> search(String searchTerm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Document> documents = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            // Préparer le terme de recherche pour LIKE
            String searchPattern = "%" + searchTerm + "%";

            String sql = "SELECT d.*, sc.nom " +
                    "FROM DOCUMENT_SOCIETAIRE d " +
                    "JOIN SOCIETAIRE_COMPTE sc ON d.id_societaire = sc.id_societaire " +
                    "WHERE d.chemin_fichier LIKE ? " +
                    "OR sc.nom LIKE ? " +
                    "ORDER BY d.date_upload DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }

            return documents;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des documents avec le terme: " + searchTerm, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Enregistre un nouveau document dans la base de données et sur le système de
     * fichiers.
     *
     * @param sourcePath   Chemin source du fichier à enregistrer
     * @param idSocietaire ID du sociétaire associé au document
     * @param typeDoc      Type du document
     * @param nomOriginal  Nom original du fichier
     * @return Le document créé
     * @throws SQLException             En cas d'erreur d'accès à la base de données
     * @throws IOException              En cas d'erreur lors de la copie du fichier
     * @throws IllegalArgumentException Si le fichier est invalide
     */
    public Document save(Path sourcePath, int idSocietaire, TypeDoc typeDoc, String nomOriginal)
            throws SQLException, IOException, IllegalArgumentException {

        validateFile(sourcePath);

        // Générer un nom de fichier unique
        String extension = getFileExtension(nomOriginal);
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

        // Créer le répertoire spécifique si nécessaire
        Path targetDir = Paths.get(BASE_UPLOAD_DIR, String.valueOf(idSocietaire));
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        // Chemin cible pour le fichier
        Path targetPath = targetDir.resolve(uniqueFileName);

        // Copier le fichier vers sa destination
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Créer l'entrée en base de données
        Document document = new Document();
        document.setIdSocietaire(idSocietaire);
        document.setTypeDoc(typeDoc);
        document.setCheminFichier(targetPath.toString());
        document.setDateUpload(LocalDateTime.now());
        document.setNomOriginal(nomOriginal);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "INSERT INTO DOCUMENT_SOCIETAIRE (id_societaire, type_doc, chemin_fichier, date_upload) " +
                    "VALUES (?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, document.getIdSocietaire());
            pstmt.setString(2, document.getTypeDoc().name());
            pstmt.setString(3, document.getCheminFichier());
            pstmt.setTimestamp(4, Timestamp.valueOf(document.getDateUpload()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du document a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                document.setIdDoc(rs.getInt(1));
            } else {
                throw new SQLException("La création du document a échoué, aucun ID généré.");
            }

            return document;

        } catch (SQLException ex) {
            // Si erreur SQL, supprimer le fichier copié
            try {
                Files.deleteIfExists(targetPath);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la suppression du fichier après échec SQL", e);
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du document", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour un document dans la base de données.
     * Note: Cette méthode ne modifie pas le fichier physique.
     *
     * @param document Le document à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(Document document) throws SQLException {
        if (document.getIdDoc() == null) {
            throw new IllegalArgumentException("L'ID du document ne peut pas être null pour une mise à jour");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE DOCUMENT_SOCIETAIRE SET id_societaire = ?, type_doc = ?, chemin_fichier = ? " +
                    "WHERE id_doc = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, document.getIdSocietaire());
            pstmt.setString(2, document.getTypeDoc().name());
            pstmt.setString(3, document.getCheminFichier());

            // ID du document à mettre à jour
            pstmt.setInt(4, document.getIdDoc());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du document ID: " + document.getIdDoc(), ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Remplace un document existant.
     *
     * @param idDoc       ID du document à remplacer
     * @param sourcePath  Chemin source du nouveau fichier
     * @param nomOriginal Nom original du nouveau fichier
     * @return Le document mis à jour
     * @throws SQLException             En cas d'erreur d'accès à la base de données
     * @throws IOException              En cas d'erreur lors de la copie du fichier
     * @throws IllegalArgumentException Si le fichier est invalide
     */
    public Document replace(int idDoc, Path sourcePath, String nomOriginal)
            throws SQLException, IOException, IllegalArgumentException {

        // Récupérer le document existant
        Optional<Document> existingDocOpt = findById(idDoc);
        if (!existingDocOpt.isPresent()) {
            throw new IllegalArgumentException("Document non trouvé avec l'ID: " + idDoc);
        }

        Document existingDoc = existingDocOpt.get();
        String oldFilePath = existingDoc.getCheminFichier();

        validateFile(sourcePath);

        // Générer un nom de fichier unique
        String extension = getFileExtension(nomOriginal);
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

        // Déterminer le répertoire cible
        Path targetDir = Paths.get(BASE_UPLOAD_DIR, String.valueOf(existingDoc.getIdSocietaire()));
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        // Chemin cible pour le nouveau fichier
        Path targetPath = targetDir.resolve(uniqueFileName);

        // Copier le nouveau fichier
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Mettre à jour l'entrée en base de données
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE DOCUMENT_SOCIETAIRE SET chemin_fichier = ?, date_upload = ? " +
                    "WHERE id_doc = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, targetPath.toString());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, idDoc);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                // Si échec, supprimer le nouveau fichier
                Files.deleteIfExists(targetPath);
                throw new SQLException("La mise à jour du document a échoué.");
            }

            // Supprimer l'ancien fichier
            try {
                Files.deleteIfExists(Paths.get(oldFilePath));
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Impossible de supprimer l'ancien fichier: " + oldFilePath, e);
            }

            // Retourner le document mis à jour
            existingDoc.setCheminFichier(targetPath.toString());
            existingDoc.setDateUpload(LocalDateTime.now());
            existingDoc.setNomOriginal(nomOriginal);

            return existingDoc;

        } catch (SQLException ex) {
            // Si erreur SQL, supprimer le nouveau fichier
            try {
                Files.deleteIfExists(targetPath);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la suppression du fichier après échec SQL", e);
            }
            LOGGER.log(Level.SEVERE, "Erreur lors du remplacement du document ID: " + idDoc, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Supprime un document de la base de données et du système de fichiers.
     *
     * @param id ID du document à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean delete(int id) throws SQLException {
        // Récupérer d'abord le document pour connaître le chemin du fichier
        Optional<Document> documentOpt = findById(id);
        if (!documentOpt.isPresent()) {
            return false; // Document non trouvé
        }

        String cheminFichier = documentOpt.get().getCheminFichier();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "DELETE FROM DOCUMENT_SOCIETAIRE WHERE id_doc = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Supprimer le fichier physique
                try {
                    Files.deleteIfExists(Paths.get(cheminFichier));
                    return true;
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Impossible de supprimer le fichier: " + cheminFichier, e);
                    return true; // On considère que la suppression est réussie même si le fichier n'est pas
                                 // supprimé
                }
            }

            return false;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du document ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Valide un fichier avant enregistrement.
     * Vérifie l'extension et la taille.
     *
     * @param filePath Chemin du fichier à valider
     * @throws IOException              En cas d'erreur lors de la lecture du
     *                                  fichier
     * @throws IllegalArgumentException Si le fichier est invalide
     */
    private void validateFile(Path filePath) throws IOException, IllegalArgumentException {
        // Vérifier que le fichier existe
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Le fichier n'existe pas");
        }

        // Vérifier la taille du fichier
        long fileSize = Files.size(filePath);
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Le fichier est trop volumineux (max " +
                    (MAX_FILE_SIZE / (1024 * 1024)) + "Mo)");
        }

        // Vérifier l'extension
        String fileName = filePath.getFileName().toString();
        String extension = getFileExtension(fileName);

        if (extension.isEmpty() || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Type de fichier non autorisé. Extensions permises: " +
                    String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    /**
     * Extrait l'extension d'un nom de fichier.
     *
     * @param fileName Nom du fichier
     * @return L'extension du fichier
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int dot = fileName.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return fileName.substring(dot + 1).toLowerCase();
    }

    /**
     * Extrait un objet Document à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du document
     * @return Objet Document créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private Document extractDocumentFromResultSet(ResultSet rs) throws SQLException {
        Document document = new Document();

        document.setIdDoc(rs.getInt("id_doc"));
        document.setIdSocietaire(rs.getInt("id_societaire"));

        // Nom du sociétaire si disponible
        try {
            String nomSocietaire = rs.getString("nom");
            document.setNomSocietaire(nomSocietaire);
        } catch (SQLException ex) {
            // Ignorer si la colonne n'est pas disponible
        }

        String typeDocStr = rs.getString("type_doc");
        document.setTypeDoc(TypeDoc.valueOf(typeDocStr));

        document.setCheminFichier(rs.getString("chemin_fichier"));

        Timestamp dateUpload = rs.getTimestamp("date_upload");
        if (dateUpload != null) {
            document.setDateUpload(dateUpload.toLocalDateTime());
        }

        // Extraire le nom original du fichier à partir du chemin si non disponible
        String nomFichier = document.getNomFichier();
        document.setNomOriginal(nomFichier);

        return document;
    }

    /**
     * Vérifie si un document existe déjà pour un sociétaire et un type donné.
     *
     * @param idSocietaire ID du sociétaire
     * @param typeDoc      Type de document
     * @return true si un document existe déjà, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean documentExists(int idSocietaire, TypeDoc typeDoc) throws SQLException {
        return findDocumentSpecifique(idSocietaire, typeDoc).isPresent();
    }

    /**
     * Récupère le chemin physique d'un document.
     *
     * @param idDoc ID du document
     * @return Optional contenant le chemin du document s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Path> getDocumentPath(int idDoc) throws SQLException {
        Optional<Document> documentOpt = findById(idDoc);
        if (!documentOpt.isPresent()) {
            return Optional.empty();
        }

        String cheminFichier = documentOpt.get().getCheminFichier();
        Path path = Paths.get(cheminFichier);

        if (Files.exists(path)) {
            return Optional.of(path);
        } else {
            LOGGER.warning("Le fichier n'existe pas sur le disque: " + cheminFichier);
            return Optional.empty();
        }
    }

    /**
     * Vérifie si les documents obligatoires sont présents pour un sociétaire.
     *
     * @param idSocietaire ID du sociétaire
     * @return true si tous les documents obligatoires sont présents, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean hasRequiredDocuments(int idSocietaire) throws SQLException {
        List<Document> documents = findBySocietaire(idSocietaire);
        Set<TypeDoc> documentTypes = new HashSet<>();

        for (Document document : documents) {
            documentTypes.add(document.getTypeDoc());
        }

        // Documents obligatoires : ID et Permis
        return documentTypes.contains(TypeDoc.ID) && documentTypes.contains(TypeDoc.Permis);
    }

    /**
     * Compte les documents par type.
     *
     * @return Map contenant le nombre de documents par type
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Map<TypeDoc, Integer> countDocumentsByType() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<TypeDoc, Integer> counts = new java.util.HashMap<>();

        // Initialiser tous les types à 0
        for (TypeDoc type : TypeDoc.values()) {
            counts.put(type, 0);
        }

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT type_doc, COUNT(*) as count FROM DOCUMENT_SOCIETAIRE GROUP BY type_doc";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String typeStr = rs.getString("type_doc");
                int count = rs.getInt("count");

                TypeDoc type = TypeDoc.valueOf(typeStr);
                counts.put(type, count);
            }

            return counts;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des documents par type", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Compte les documents par sociétaire.
     *
     * @return Map contenant le nombre de documents par sociétaire
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Map<Integer, Integer> countDocumentsBySocietaire() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, Integer> counts = new java.util.HashMap<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT id_societaire, COUNT(*) as count FROM DOCUMENT_SOCIETAIRE GROUP BY id_societaire";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idSocietaire = rs.getInt("id_societaire");
                int count = rs.getInt("count");

                counts.put(idSocietaire, count);
            }

            return counts;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des documents par sociétaire", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }
}