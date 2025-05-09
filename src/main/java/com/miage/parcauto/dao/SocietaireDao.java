package main.java.com.miage.parcauto.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.model.Societaire;

/**
 * Classe d'accès aux données pour les sociétaires.
 * Gère les opérations CRUD et les requêtes spécifiques liées aux sociétaires.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocietaireDao {
    
    private static final Logger LOGGER = Logger.getLogger(SocietaireDao.class.getName());
    
    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    /**
     * Constructeur par défaut. Initialise l'instance de DbUtil.
     */
    public SocietaireDao() {
        this.dbUtil = DbUtil.getInstance();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     */
    public SocietaireDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Récupère tous les sociétaires de la base de données.
     *
     * @return Liste des sociétaires
     */
    public List<Societaire> findAll() {
        List<Societaire> societaires = new ArrayList<>();
        String sql = "SELECT * FROM SOCIETAIRE ORDER BY nom, prenom";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                societaires.add(extractSocietaireFromResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des sociétaires", ex);
        }
        return societaires;
    }

    /**
     * Récupère tous les sociétaires actifs de la base de données.
     *
     * @return Liste des sociétaires actifs
     */
    public List<Societaire> findAllActifs() {
        List<Societaire> societaires = new ArrayList<>();
        String sql = "SELECT * FROM SOCIETAIRE WHERE actif = TRUE ORDER BY nom, prenom";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                societaires.add(extractSocietaireFromResultSet(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des sociétaires actifs", ex);
        }
        return societaires;
    }

    /**
     * Recherche un sociétaire par son identifiant.
     *
     * @param id Identifiant du sociétaire
     * @return Le sociétaire trouvé, ou vide si non trouvé
     */
    public Optional<Societaire> findById(int id) {
        String sql = "SELECT * FROM SOCIETAIRE WHERE id_societaire = ?";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractSocietaireFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du sociétaire avec id=" + id, ex);
        }
        return Optional.empty();
    }

    /**
     * Recherche un sociétaire par son numéro de CIN.
     *
     * @param numeroCin Numéro de CIN à rechercher
     * @return Le sociétaire trouvé, ou vide si non trouvé
     */
    public Optional<Societaire> findByCIN(String numeroCin) {
        String sql = "SELECT * FROM SOCIETAIRE WHERE numero_cin = ?";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroCin);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractSocietaireFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du sociétaire avec CIN=" + numeroCin, ex);
        }
        return Optional.empty();
    }

    /**
     * Recherche un sociétaire par son numéro de permis.
     *
     * @param numeroPermis Numéro de permis à rechercher
     * @return Le sociétaire trouvé, ou vide si non trouvé
     */
    public Optional<Societaire> findByPermis(String numeroPermis) {
        String sql = "SELECT * FROM SOCIETAIRE WHERE numero_permis = ?";
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroPermis);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extractSocietaireFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du sociétaire avec permis=" + numeroPermis, ex);
        }
        return Optional.empty();
    }

    /**
     * Recherche des sociétaires par nom et/ou prénom.
     *
     * @param recherche Chaîne de recherche (nom ou prénom partiel)
     * @return Liste des sociétaires correspondant à la recherche
     */
    public List<Societaire> searchByNomPrenom(String recherche) {
        List<Societaire> societaires = new ArrayList<>();
        String sql = "SELECT * FROM SOCIETAIRE WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom, prenom";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + recherche + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    societaires.add(extractSocietaireFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de sociétaires avec " + recherche, ex);
        }
        return societaires;
    }

    /**
     * Insère un nouveau sociétaire dans la base de données.
     *
     * @param societaire Le sociétaire à insérer
     * @return L'identifiant généré, ou -1 en cas d'erreur
     */
    public int insert(Societaire societaire) {
        String sql = "INSERT INTO SOCIETAIRE (nom, prenom, numero_cin, numero_permis, email, telephone, " +
                    "date_naissance, date_permis, adresse, actif) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            prepareStatementFromSocietaire(societaire, stmt);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création du sociétaire a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    societaire.setIdSocietaire(id);
                    return id;
                } else {
                    throw new SQLException("La création du sociétaire a échoué, aucun ID généré.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'insertion du sociétaire", ex);
            return -1;
        }
    }

    /**
     * Met à jour un sociétaire existant.
     *
     * @param societaire Le sociétaire à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Societaire societaire) {
        if (societaire.getIdSocietaire() == null) {
            LOGGER.severe("Tentative de mise à jour d'un sociétaire sans ID");
            return false;
        }

        String sql = "UPDATE SOCIETAIRE SET nom = ?, prenom = ?, numero_cin = ?, numero_permis = ?, " +
                    "email = ?, telephone = ?, date_naissance = ?, date_permis = ?, adresse = ?, actif = ? " +
                    "WHERE id_societaire = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            prepareStatementFromSocietaire(societaire, stmt);
            stmt.setInt(11, societaire.getIdSocietaire());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du sociétaire id=" + societaire.getIdSocietaire(), ex);
            return false;
        }
    }

    /**
     * Supprime un sociétaire de la base de données.
     *
     * @param id Identifiant du sociétaire à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(int id) {
        // Plutôt que de supprimer, on désactive le sociétaire (actif = false)
        String sql = "UPDATE SOCIETAIRE SET actif = false WHERE id_societaire = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la désactivation du sociétaire id=" + id, ex);
            return false;
        }
    }

    /**
     * Vérifie si un sociétaire possède tous les documents requis.
     *
     * @param idSocietaire ID du sociétaire à vérifier
     * @return true si tous les documents sont présents, false sinon
     */
    public boolean aDocumentsComplets(int idSocietaire) {
        String sql = "SELECT COUNT(DISTINCT type_doc) as nb_types FROM DOCUMENT WHERE id_societaire = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idSocietaire);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int nbTypes = rs.getInt("nb_types");
                    // On vérifie si tous les types de documents sont présents (4 types)
                    return nbTypes >= 4;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des documents pour le sociétaire id=" + idSocietaire, ex);
        }
        
        return false;
    }

    /**
     * Remplit un PreparedStatement avec les données d'un sociétaire.
     *
     * @param societaire Le sociétaire source
     * @param stmt Le PreparedStatement à remplir
     * @throws SQLException Si une erreur se produit
     */
    private void prepareStatementFromSocietaire(Societaire societaire, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, societaire.getNom());
        stmt.setString(2, societaire.getPrenom());
        stmt.setString(3, societaire.getNumeroCin());
        stmt.setString(4, societaire.getNumeroPermis());
        stmt.setString(5, societaire.getEmail());
        stmt.setString(6, societaire.getTelephone());

        if (societaire.getDateNaissance() != null) {
            stmt.setDate(7, Date.valueOf(societaire.getDateNaissance()));
        } else {
            stmt.setNull(7, Types.DATE);
        }

        if (societaire.getDatePermis() != null) {
            stmt.setDate(8, Date.valueOf(societaire.getDatePermis()));
        } else {
            stmt.setNull(8, Types.DATE);
        }

        stmt.setString(9, societaire.getAdresse());
        stmt.setBoolean(10, societaire.isActif());
    }

    /**
     * Extrait un objet Societaire à partir d'un ResultSet.
     *
     * @param rs Le ResultSet contenant les données
     * @return L'objet Societaire créé
     * @throws SQLException Si une erreur se produit
     */
    private Societaire extractSocietaireFromResultSet(ResultSet rs) throws SQLException {
        Integer idSocietaire = rs.getInt("id_societaire");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String numeroCin = rs.getString("numero_cin");
        String numeroPermis = rs.getString("numero_permis");
        String email = rs.getString("email");
        String telephone = rs.getString("telephone");
        
        LocalDate dateNaissance = null;
        Date sqlDateNaissance = rs.getDate("date_naissance");
        if (sqlDateNaissance != null) {
            dateNaissance = sqlDateNaissance.toLocalDate();
        }
        
        LocalDate datePermis = null;
        Date sqlDatePermis = rs.getDate("date_permis");
        if (sqlDatePermis != null) {
            datePermis = sqlDatePermis.toLocalDate();
        }
        
        String adresse = rs.getString("adresse");
        boolean actif = rs.getBoolean("actif");
        
        return new Societaire(idSocietaire, nom, prenom, numeroCin, numeroPermis, 
                             email, telephone, dateNaissance, datePermis, adresse, actif);
    }
}