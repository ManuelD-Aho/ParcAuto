package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.NotificationRepository;
import main.java.com.miage.parcauto.model.notification.Notification;
import main.java.com.miage.parcauto.exception.DataAccessException;

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
import java.util.StringJoiner;

public class NotificationRepositoryImpl implements NotificationRepository {

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setIdNotification(rs.getInt("id_notification"));
        notification.setIdUtilisateur(rs.getInt("id_utilisateur"));
        notification.setMessage(rs.getString("message"));
        notification.setTitre(rs.getString("titre"));
        notification.setDateCreation(
                rs.getTimestamp("date_creation") != null ? rs.getTimestamp("date_creation").toLocalDateTime() : null);
        notification.setEstLue(rs.getBoolean("est_lue"));
        notification.setTypeNotification(rs.getString("type_notification"));
        notification.setIdEntiteLiee(rs.getInt("id_entite_liee"));
        notification.setTypeEntiteLiee(rs.getString("type_entite_liee"));
        return notification;
    }

    @Override
    public Optional<Notification> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM NOTIFICATION WHERE id_notification = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de la notification par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Notification> findAll(Connection conn) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATION ORDER BY date_creation DESC";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de toutes les notifications", e);
        }
        return notifications;
    }

    @Override
    public List<Notification> findAll(Connection conn, int page, int size) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATION ORDER BY date_creation DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des notifications", e);
        }
        return notifications;
    }

    @Override
    public Notification save(Connection conn, Notification notification) throws SQLException {
        String sql = "INSERT INTO NOTIFICATION (id_utilisateur, message, date_creation, est_lu) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, notification.getIdUtilisateur());
            pstmt.setString(2, notification.getMessage());
            pstmt.setTimestamp(3,
                    notification.getDateCreation() != null ? Timestamp.valueOf(notification.getDateCreation())
                            : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setBoolean(4, notification.isEstLu());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de la notification a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    notification.setIdNotification(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de la notification a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la sauvegarde de la notification: " + notification.getMessage(), e);
        }
        return notification;
    }

    @Override
    public Notification update(Connection conn, Notification notification) throws SQLException {
        String sql = "UPDATE NOTIFICATION SET id_utilisateur = ?, message = ?, date_creation = ?, est_lu = ? WHERE id_notification = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notification.getIdUtilisateur());
            pstmt.setString(2, notification.getMessage());
            pstmt.setTimestamp(3,
                    notification.getDateCreation() != null ? Timestamp.valueOf(notification.getDateCreation()) : null);
            pstmt.setBoolean(4, notification.isEstLu());
            pstmt.setInt(5, notification.getIdNotification());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de la notification avec ID "
                        + notification.getIdNotification() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la mise à jour de la notification: " + notification.getIdNotification(), e);
        }
        return notification;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM NOTIFICATION WHERE id_notification = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de la notification: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM NOTIFICATION";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des notifications", e);
        }
        return 0;
    }

    @Override
    public List<Notification> findByUtilisateurId(Connection conn, Integer idUtilisateur) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATION WHERE id_utilisateur = ? ORDER BY date_creation DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUtilisateur);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des notifications pour l'utilisateur ID: " + idUtilisateur, e);
        }
        return notifications;
    }

    @Override
    public List<Notification> findByUtilisateurIdAndEstLu(Connection conn, Integer idUtilisateur, boolean estLu)
            throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM NOTIFICATION WHERE id_utilisateur = ? AND est_lu = ? ORDER BY date_creation DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUtilisateur);
            pstmt.setBoolean(2, estLu);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des notifications pour l'utilisateur ID: "
                    + idUtilisateur + " et état lu: " + estLu, e);
        }
        return notifications;
    }

    @Override
    public int markAsRead(Connection conn, List<Integer> idsNotification) throws SQLException {
        if (idsNotification == null || idsNotification.isEmpty()) {
            return 0;
        }
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (Integer id : idsNotification) {
            sj.add("?");
        }
        String sql = "UPDATE NOTIFICATION SET est_lu = TRUE WHERE id_notification IN " + sj.toString();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : idsNotification) {
                pstmt.setInt(index++, id);
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du marquage des notifications comme lues.", e);
        }
    }
}