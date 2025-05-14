package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Personnel;
import main.java.com.miage.parcauto.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation de l'interface PersonnelRepository pour la gestion du
 * personnel.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class PersonnelRepositoryImpl implements PersonnelRepository {

    private static final Logger LOGGER = Logger.getLogger(PersonnelRepositoryImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Personnel> findById(Integer id) {
        String query = "SELECT * FROM PERSONNEL WHERE id_personnel = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du personnel par ID: " + id, ex);
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAll() {
        String query = "SELECT * FROM PERSONNEL";
        List<Personnel> personnels = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                personnels.add(mapResultSetToPersonnel(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tout le personnel", ex);
        }

        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAll(int page, int size) {
        String query = "SELECT * FROM PERSONNEL LIMIT ? OFFSET ?";
        List<Personnel> personnels = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du personnel avec pagination", ex);
        }

        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Personnel save(Personnel personnel) {
        String query = "INSERT INTO PERSONNEL (nom, prenom, email, telephone, nom_utilisateur, " +
                "mot_de_passe, sel, role, actif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, personnel.getNom());
            pstmt.setString(2, personnel.getPrenom());
            pstmt.setString(3, personnel.getEmail());
            pstmt.setString(4, personnel.getTelephone());
            pstmt.setString(5, personnel.getNomUtilisateur());
            pstmt.setString(6, personnel.getMotDePasse());
            pstmt.setString(7, personnel.getSel());
            pstmt.setString(8, personnel.getRole());
            pstmt.setBoolean(9, personnel.isActif());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du personnel a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personnel.setIdPersonnel(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du personnel a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création d'un personnel", ex);
        }

        return personnel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Personnel update(Personnel personnel) {
        String query = "UPDATE PERSONNEL SET nom = ?, prenom = ?, email = ?, telephone = ?, " +
                "nom_utilisateur = ?, mot_de_passe = ?, sel = ?, role = ?, actif = ? " +
                "WHERE id_personnel = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, personnel.getNom());
            pstmt.setString(2, personnel.getPrenom());
            pstmt.setString(3, personnel.getEmail());
            pstmt.setString(4, personnel.getTelephone());
            pstmt.setString(5, personnel.getNomUtilisateur());
            pstmt.setString(6, personnel.getMotDePasse());
            pstmt.setString(7, personnel.getSel());
            pstmt.setString(8, personnel.getRole());
            pstmt.setBoolean(9, personnel.isActif());
            pstmt.setInt(10, personnel.getIdPersonnel());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La mise à jour du personnel a échoué, aucune ligne affectée.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du personnel: " + personnel.getIdPersonnel(), ex);
        }

        return personnel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM PERSONNEL WHERE id_personnel = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du personnel: " + id, ex);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM PERSONNEL";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage du personnel", ex);
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Personnel> findByNomUtilisateur(String nomUtilisateur) {
        String query = "SELECT * FROM PERSONNEL WHERE nom_utilisateur = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nomUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche du personnel par nom d'utilisateur: " + nomUtilisateur, ex);
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Personnel> findByEmail(String email) {
        String query = "SELECT * FROM PERSONNEL WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du personnel par email: " + email, ex);
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAllByRole(String role) {
        String query = "SELECT * FROM PERSONNEL WHERE role = ?";
        List<Personnel> personnels = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, role);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du personnel par rôle: " + role, ex);
        }

        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAllActifs() {
        String query = "SELECT * FROM PERSONNEL WHERE actif = TRUE";
        List<Personnel> personnels = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                personnels.add(mapResultSetToPersonnel(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du personnel actif", ex);
        }

        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Personnel> findAllInactifs() {
        String query = "SELECT * FROM PERSONNEL WHERE actif = FALSE";
        List<Personnel> personnels = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                personnels.add(mapResultSetToPersonnel(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du personnel inactif", ex);
        }

        return personnels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateMotDePasse(Integer idPersonnel, String nouveauMotDePasse, String nouveauSel) {
        String query = "UPDATE PERSONNEL SET mot_de_passe = ?, sel = ? WHERE id_personnel = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nouveauMotDePasse);
            pstmt.setString(2, nouveauSel);
            pstmt.setInt(3, idPersonnel);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du mot de passe du personnel: " + idPersonnel, ex);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean desactiver(Integer idPersonnel) {
        String query = "UPDATE PERSONNEL SET actif = FALSE WHERE id_personnel = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idPersonnel);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la désactivation du personnel: " + idPersonnel, ex);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reactiver(Integer idPersonnel) {
        String query = "UPDATE PERSONNEL SET actif = TRUE WHERE id_personnel = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idPersonnel);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la réactivation du personnel: " + idPersonnel, ex);
            return false;
        }
    }

    /**
     * Convertit un ResultSet en objet Personnel.
     *
     * @param rs Le ResultSet à convertir
     * @return L'objet Personnel créé
     * @throws SQLException Si une erreur survient lors de la récupération des
     *                      données
     */
    private Personnel mapResultSetToPersonnel(ResultSet rs) throws SQLException {
        Personnel personnel = new Personnel();

        personnel.setIdPersonnel(rs.getInt("id_personnel"));
        personnel.setNom(rs.getString("nom"));
        personnel.setPrenom(rs.getString("prenom"));
        personnel.setEmail(rs.getString("email"));
        personnel.setTelephone(rs.getString("telephone"));
        personnel.setNomUtilisateur(rs.getString("nom_utilisateur"));
        personnel.setMotDePasse(rs.getString("mot_de_passe"));
        personnel.setSel(rs.getString("sel"));
        personnel.setRole(rs.getString("role"));
        personnel.setActif(rs.getBoolean("actif"));

        return personnel;
    }
}
