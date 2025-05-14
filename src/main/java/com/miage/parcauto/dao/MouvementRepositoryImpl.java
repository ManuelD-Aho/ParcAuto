package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du repository pour la gestion des mouvements financiers.
 * Remplace MouvementDao.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MouvementRepositoryImpl implements MouvementRepository {

    private static final Logger LOGGER = Logger.getLogger(MouvementRepositoryImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Mouvement> findById(Integer id) {
        String query = "SELECT * FROM MOUVEMENT WHERE id_mouvement = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du mouvement par ID: " + id, ex);
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAll() {
        String query = "SELECT * FROM MOUVEMENT";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                mouvements.add(mapResultSetToMouvement(rs));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les mouvements", ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAll(int page, int size) {
        String query = "SELECT * FROM MOUVEMENT LIMIT ? OFFSET ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements avec pagination", ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mouvement save(Mouvement mouvement) {
        String query = "INSERT INTO MOUVEMENT (id_compte, date_mouvement, montant, libelle, type_mouvement, " +
                "id_mission, id_entretien, categorie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, mouvement.getIdCompte());
            pstmt.setDate(2, java.sql.Date.valueOf(mouvement.getDateMouvement()));
            pstmt.setBigDecimal(3, mouvement.getMontant());
            pstmt.setString(4, mouvement.getLibelle());
            pstmt.setString(5, mouvement.getTypeMouvement());

            if (mouvement.getIdMission() != null) {
                pstmt.setInt(6, mouvement.getIdMission());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            if (mouvement.getIdEntretien() != null) {
                pstmt.setInt(7, mouvement.getIdEntretien());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }

            pstmt.setString(8, mouvement.getCategorie());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du mouvement a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mouvement.setIdMouvement(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du mouvement a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création d'un mouvement", ex);
        }

        return mouvement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mouvement update(Mouvement mouvement) {
        String query = "UPDATE MOUVEMENT SET id_compte = ?, date_mouvement = ?, montant = ?, libelle = ?, " +
                "type_mouvement = ?, id_mission = ?, id_entretien = ?, categorie = ? WHERE id_mouvement = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, mouvement.getIdCompte());
            pstmt.setDate(2, java.sql.Date.valueOf(mouvement.getDateMouvement()));
            pstmt.setBigDecimal(3, mouvement.getMontant());
            pstmt.setString(4, mouvement.getLibelle());
            pstmt.setString(5, mouvement.getTypeMouvement());

            if (mouvement.getIdMission() != null) {
                pstmt.setInt(6, mouvement.getIdMission());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            if (mouvement.getIdEntretien() != null) {
                pstmt.setInt(7, mouvement.getIdEntretien());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }

            pstmt.setString(8, mouvement.getCategorie());
            pstmt.setInt(9, mouvement.getIdMouvement());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La mise à jour du mouvement a échoué, aucune ligne affectée.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du mouvement: " + mouvement.getIdMouvement(), ex);
        }

        return mouvement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM MOUVEMENT WHERE id_mouvement = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du mouvement: " + id, ex);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM MOUVEMENT";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des mouvements", ex);
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAllByCompte(Integer idCompte) {
        String query = "SELECT * FROM MOUVEMENT WHERE id_compte = ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idCompte);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par compte: " + idCompte, ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAllByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        String query = "SELECT * FROM MOUVEMENT WHERE date_mouvement BETWEEN ? AND ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dateDebut));
            pstmt.setDate(2, java.sql.Date.valueOf(dateFin));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par période", ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAllByType(String typeMouvement) {
        String query = "SELECT * FROM MOUVEMENT WHERE type_mouvement = ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, typeMouvement);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par type: " + typeMouvement, ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAllByMontantGreaterThan(BigDecimal montant) {
        String query = "SELECT * FROM MOUVEMENT WHERE montant > ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBigDecimal(1, montant);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par montant supérieur à: " + montant,
                    ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAllByMission(Integer idMission) {
        String query = "SELECT * FROM MOUVEMENT WHERE id_mission = ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idMission);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par mission: " + idMission, ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mouvement> findAllByEntretien(Integer idEntretien) {
        String query = "SELECT * FROM MOUVEMENT WHERE id_entretien = ?";
        List<Mouvement> mouvements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idEntretien);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par entretien: " + idEntretien, ex);
        }

        return mouvements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calculerSoldePeriode(LocalDate dateDebut, LocalDate dateFin) {
        String query = "SELECT SUM(CASE WHEN type_mouvement = 'CREDIT' THEN montant ELSE -montant END) " +
                "FROM MOUVEMENT WHERE date_mouvement BETWEEN ? AND ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dateDebut));
            pstmt.setDate(2, java.sql.Date.valueOf(dateFin));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal resultat = rs.getBigDecimal(1);
                    return resultat != null ? resultat : BigDecimal.ZERO;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du solde pour la période", ex);
        }

        return BigDecimal.ZERO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object[]> calculerDepensesParCategorie(LocalDate dateDebut, LocalDate dateFin) {
        String query = "SELECT categorie, SUM(montant) FROM MOUVEMENT " +
                "WHERE type_mouvement = 'DEBIT' AND date_mouvement BETWEEN ? AND ? " +
                "GROUP BY categorie ORDER BY SUM(montant) DESC";
        List<Object[]> resultats = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dateDebut));
            pstmt.setDate(2, java.sql.Date.valueOf(dateFin));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] ligne = new Object[2];
                    ligne[0] = rs.getString(1); // catégorie
                    ligne[1] = rs.getBigDecimal(2); // somme
                    resultats.add(ligne);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des dépenses par catégorie", ex);
        }

        return resultats;
    }

    /**
     * Convertit un ResultSet en objet Mouvement.
     *
     * @param rs Le ResultSet à convertir
     * @return L'objet Mouvement créé
     * @throws SQLException Si une erreur survient lors de la récupération des
     *                      données
     */
    private Mouvement mapResultSetToMouvement(ResultSet rs) throws SQLException {
        Mouvement mouvement = new Mouvement();

        mouvement.setIdMouvement(rs.getInt("id_mouvement"));
        mouvement.setIdCompte(rs.getInt("id_compte"));
        mouvement.setDateMouvement(rs.getDate("date_mouvement").toLocalDate());
        mouvement.setMontant(rs.getBigDecimal("montant"));
        mouvement.setLibelle(rs.getString("libelle"));
        mouvement.setTypeMouvement(rs.getString("type_mouvement"));

        int idMission = rs.getInt("id_mission");
        if (!rs.wasNull()) {
            mouvement.setIdMission(idMission);
        }

        int idEntretien = rs.getInt("id_entretien");
        if (!rs.wasNull()) {
            mouvement.setIdEntretien(idEntretien);
        }

        mouvement.setCategorie(rs.getString("categorie"));

        return mouvement;
    }
}
