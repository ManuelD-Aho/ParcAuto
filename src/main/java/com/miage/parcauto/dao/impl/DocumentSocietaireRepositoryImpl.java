package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.DocumentSocietaireRepository;
import main.java.com.miage.parcauto.model.document.DocumentSocietaire;
import main.java.com.miage.parcauto.model.document.TypeDocument;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentSocietaireRepositoryImpl implements DocumentSocietaireRepository {

    private DocumentSocietaire mapResultSetToDocumentSocietaire(ResultSet rs) throws SQLException {
        DocumentSocietaire document = new DocumentSocietaire();
        document.setIdDocument(rs.getInt("id_document"));
        document.setIdCompteSocietaire(rs.getInt("id_compte_societaire"));

        String typeDocStr = rs.getString("type_doc");
        if (typeDocStr != null) {
            document.setTypeDoc(TypeDocument.fromString(typeDocStr));
        }
        document.setNomFichier(rs.getString("nom_fichier"));
        document.setCheminFichier(rs.getString("chemin_fichier"));

        Timestamp dateUploadTs = rs.getTimestamp("date_upload");
        document.setDateUpload(dateUploadTs != null ? dateUploadTs.toLocalDateTime() : null);

        Timestamp dateExpirationTs = rs.getTimestamp("date_expiration");
        document.setDateExpiration(dateExpirationTs != null ? dateExpirationTs.toLocalDateTime() : null);

        document.setObservation(rs.getString("observation"));
        return document;
    }

    @Override
    public Optional<DocumentSocietaire> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM DOCUMENT_SOCIETAIRE WHERE id_document = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du document sociétaire par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<DocumentSocietaire> findAll(Connection conn) throws SQLException {
        List<DocumentSocietaire> documents = new ArrayList<>();
        String sql = "SELECT * FROM DOCUMENT_SOCIETAIRE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                documents.add(mapResultSetToDocumentSocietaire(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les documents sociétaires", e);
        }
        return documents;
    }

    @Override
    public List<DocumentSocietaire> findAll(Connection conn, int page, int size) throws SQLException {
        List<DocumentSocietaire> documents = new ArrayList<>();
        String sql = "SELECT * FROM DOCUMENT_SOCIETAIRE LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des documents sociétaires", e);
        }
        return documents;
    }

    @Override
    public DocumentSocietaire save(Connection conn, DocumentSocietaire document) throws SQLException {
        String sql = "INSERT INTO DOCUMENT_SOCIETAIRE (id_compte_societaire, type_doc, nom_fichier, chemin_fichier, date_upload, date_expiration, observation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, document.getIdCompteSocietaire());
            pstmt.setString(2, document.getTypeDoc() != null ? document.getTypeDoc().getValeur() : null);
            pstmt.setString(3, document.getNomFichier());
            pstmt.setString(4, document.getCheminFichier());
            pstmt.setTimestamp(5, document.getDateUpload() != null ? Timestamp.valueOf(document.getDateUpload()) : null);
            pstmt.setTimestamp(6, document.getDateExpiration() != null ? Timestamp.valueOf(document.getDateExpiration()) : null);
            pstmt.setString(7, document.getObservation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création du document sociétaire a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    document.setIdDocument(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création du document sociétaire a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde du document sociétaire: " + document.getNomFichier(), e);
        }
        return document;
    }

    @Override
    public DocumentSocietaire update(Connection conn, DocumentSocietaire document) throws SQLException {
        String sql = "UPDATE DOCUMENT_SOCIETAIRE SET id_compte_societaire = ?, type_doc = ?, nom_fichier = ?, chemin_fichier = ?, date_upload = ?, date_expiration = ?, observation = ? WHERE id_document = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, document.getIdCompteSocietaire());
            pstmt.setString(2, document.getTypeDoc() != null ? document.getTypeDoc().getValeur() : null);
            pstmt.setString(3, document.getNomFichier());
            pstmt.setString(4, document.getCheminFichier());
            pstmt.setTimestamp(5, document.getDateUpload() != null ? Timestamp.valueOf(document.getDateUpload()) : null);
            pstmt.setTimestamp(6, document.getDateExpiration() != null ? Timestamp.valueOf(document.getDateExpiration()) : null);
            pstmt.setString(7, document.getObservation());
            pstmt.setInt(8, document.getIdDocument());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour du document sociétaire avec ID " + document.getIdDocument() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour du document sociétaire: " + document.getIdDocument(), e);
        }
        return document;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM DOCUMENT_SOCIETAIRE WHERE id_document = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression du document sociétaire: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DOCUMENT_SOCIETAIRE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des documents sociétaires", e);
        }
        return 0;
    }

    @Override
    public List<DocumentSocietaire> findBySocietaireCompteId(Connection conn, Integer idCompteSocietaire) throws SQLException {
        List<DocumentSocietaire> documents = new ArrayList<>();
        String sql = "SELECT * FROM DOCUMENT_SOCIETAIRE WHERE id_compte_societaire = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCompteSocietaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des documents pour le compte sociétaire ID: " + idCompteSocietaire, e);
        }
        return documents;
    }

    @Override
    public List<DocumentSocietaire> findBySocietaireCompteIdAndType(Connection conn, Integer idCompteSocietaire, TypeDocument typeDocument) throws SQLException {
        List<DocumentSocietaire> documents = new ArrayList<>();
        String sql = "SELECT * FROM DOCUMENT_SOCIETAIRE WHERE id_compte_societaire = ? AND type_doc = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCompteSocietaire);
            pstmt.setString(2, typeDocument.getValeur());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(mapResultSetToDocumentSocietaire(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des documents pour le compte sociétaire ID: " + idCompteSocietaire + " et type: " + typeDocument, e);
        }
        return documents;
    }
}