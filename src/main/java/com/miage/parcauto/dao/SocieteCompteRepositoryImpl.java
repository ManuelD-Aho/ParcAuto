package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.SocieteCompte;
import main.java.com.miage.parcauto.util.ConnectionFactory;

import java.math.BigDecimal;
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
 * Implémentation du repository pour la gestion des comptes sociétaires.
 * Remplace SocieteCompteDao.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocieteCompteRepositoryImpl implements SocieteCompteRepository {

    private static final Logger LOGGER = Logger.getLogger(SocieteCompteRepositoryImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SocieteCompte> findById(Integer id) {
        String query = "SELECT * FROM SOCIETE_COMPTE WHERE id_compte = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocieteCompte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte par ID: " + id, ex);
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocieteCompte> findAll() {
        String query = "SELECT * FROM SOCIETE_COMPTE";
        List<SocieteCompte> comptes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                comptes.add(mapResultSetToSocieteCompte(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les comptes", ex);
        }

        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocieteCompte> findAll(int page, int size) {
        String query = "SELECT * FROM SOCIETE_COMPTE LIMIT ? OFFSET ?";
        List<SocieteCompte> comptes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToSocieteCompte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des comptes avec pagination", ex);
        }

        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SocieteCompte save(SocieteCompte compte) {
        String query = "INSERT INTO SOCIETE_COMPTE (numero_compte, libelle_compte, solde_compte, type_compte, " +
                "date_creation, id_personnel, actif) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, compte.getNumeroCompte());
            pstmt.setString(2, compte.getLibelleCompte());
            pstmt.setBigDecimal(3, compte.getSoldeCompte());
            pstmt.setString(4, compte.getTypeCompte());
            pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(compte.getDateCreation()));
            pstmt.setInt(6, compte.getIdPersonnel());
            pstmt.setBoolean(7, compte.isActif());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du compte a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    compte.setIdCompte(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du compte a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création d'un compte", ex);
        }

        return compte;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SocieteCompte update(SocieteCompte compte) {
        String query = "UPDATE SOCIETE_COMPTE SET numero_compte = ?, libelle_compte = ?, solde_compte = ?, " +
                "type_compte = ?, date_creation = ?, id_personnel = ?, actif = ? WHERE id_comp

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, compte.getNumeroCompte());
            pstmt.setString(2, compte.getLibelleCompte());
            pstmt.setBigDecimal(3, compte.getSoldeCompte());
            pstmt.setString(4, compte.getTypeCompte());
            pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(compte.getDateCreation()));
            pstmt.setInt(6, compte.getIdPersonnel());
            pstmt.setBoolean(7, compte.isActif());
            pstmt.setInt(8, compte.getIdCompte());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La mise à jour du compte a échoué, aucune ligne affectée.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du compte: " + compte.getIdCompte(), ex);
        }

        return compte;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM SOCIETE_COMPTE WHERE id_compte = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du compte: " + id, ex);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM SOCIETE_COMPTE";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des comptes", ex);
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SocieteCompte> findByNumeroCompte(String numeroCompte) {
        String query = "SELECT * FROM SOCIETE_COMPTE WHERE numero_compte = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, numeroCompte);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocieteCompte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte par numéro: " + numeroCompte, ex);
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocieteCompte> findAllBySoldeGreaterThan(BigDecimal montant) {
        String query = "SELECT * FROM SOCIETE_COMPTE WHERE solde_compte > ?";
        List<SocieteCompte> comptes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBigDecimal(1, montant);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToSocieteCompte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des comptes avec solde supérieur à: " + montant, ex);
        }

        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocieteCompte> findAllBySoldeLessThan(BigDecimal montant) {
        String query = "SELECT * FROM SOCIETE_COMPTE WHERE solde_compte < ?";
        List<SocieteCompte> comptes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBigDecimal(1, montant);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToSocieteCompte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des comptes avec solde inférieur à: " + montant, ex);
        }

        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SocieteCompte> findAllByPersonnel(Integer idPersonnel) {
        String query = "SELECT * FROM SOCIETE_COMPTE WHERE id_personnel = ?";
        List<SocieteCompte> comptes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idPersonnel);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToSocieteCompte(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des comptes par personnel: " + idPersonnel, ex);
        }

        return comptes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateSolde(Integer idCompte, BigDecimal nouveauSolde) {
        String query = "UPDATE SOCIETE_COMPTE SET solde_compte = ? WHERE id_compte = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setInt(2, idCompte);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du solde du compte: " + idCompte, ex);
            return false;
        }
    }

    /**
     * Convertit un ResultSet en objet SocieteCompte.
     *
     * @param rs Le ResultSet à convertir
     * @return L'objet SocieteCompte créé
     * @throws SQLException Si une erreur survient lors de la récupération des
     *                      données
     */
    private SocieteCompte mapResultSetToSocieteCompte(ResultSet rs) throws SQLException {
        SocieteCompte compte = new SocieteCompte();

        compte.setIdCompte(rs.getInt("id_compte"));
        compte.setNumeroCompte(rs.getString("numero_compte"));
        compte.setLibelleCompte(rs.getString("libelle_compte"));
        compte.setSoldeCompte(rs.getBigDecimal("solde_compte"));
        compte.setTypeCompte(rs.getString("type_compte"));
        compte.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        compte.setIdPersonnel(rs.getInt("id_personnel"));
        compte.setActif(rs.getBoolean("actif"));

        return compte;
    }
}
