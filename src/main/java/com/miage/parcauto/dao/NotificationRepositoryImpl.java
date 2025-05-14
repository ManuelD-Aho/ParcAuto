package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.notification.Notification;
import main.java.com.miage.parcauto.exception.DatabaseException;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du repository pour les notifications.
 * Gère les opérations CRUD et recherches spécifiques pour les notifications.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class NotificationRepositoryImpl implements NotificationRepository {

    private static final Logger LOGGER = Logger.getLogger(NotificationRepositoryImpl.class.getName());
    private final DbUtil dbUtil;

    /**
     * Constructeur par défaut.
     */
    public NotificationRepositoryImpl() {
        this.dbUtil = DbUtil.getInstance();
    }

    /**
     * Convertit un ResultSet en objet Notification.
     * 
     * @param rs ResultSet à convertir
     * @return Notification créée à partir du ResultSet
     * @throws SQLException en cas d'erreur SQL
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setIdNotification(rs.getInt("id_notification"));
        notification.setIdUtilisateur(rs.getInt("id_utilisateur"));
        notification.setTitre(rs.getString("titre"));
        notification.setMessage(rs.getString("message"));

        String typeStr = rs.getString("type");
        Notification.TypeNotification type = Notification.TypeNotification.valueOf(typeStr);
        notification.setType(type);

        Timestamp timestamp = rs.getTimestamp("date_creation");
        notification.setDateCreation(timestamp != null ? timestamp.toLocalDateTime() : null);

        notification.setVue(rs.getBoolean("vue"));
        notification.setActionUrl(rs.getString("action_url"));

        return notification;
    }

    @Override
    public Optional<Notification> findById(Integer id) {
        String query = "SELECT * FROM NOTIFICATION WHERE id_notification = ?";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    return Optional.of(notification);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de la notification par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de la notification: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM NOTIFICATION ORDER BY date_creation DESC";

        try (Connection connection = dbUtil.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des notifications", e);
            throw new DatabaseException("Erreur lors de la récupération des notifications: " + e.getMessage(), e);
        }

        return notifications;
    }

    @Override
    public List<Notification> findAll(int page, int size) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM NOTIFICATION ORDER BY date_creation DESC LIMIT ? OFFSET ?";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des notifications", e);
            throw new DatabaseException("Erreur lors de la récupération paginée des notifications: " + e.getMessage(),
                    e);
        }

        return notifications;
    }

    @Override
    public Notification save(Notification notification) {
        String query = "INSERT INTO NOTIFICATION (id_utilisateur, titre, message, type, date_creation, vue, action_url) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, notification.getIdUtilisateur());
            ps.setString(2, notification.getTitre());
            ps.setString(3, notification.getMessage());
            ps.setString(4, notification.getType().name());
            ps.setTimestamp(5, Timestamp.valueOf(notification.getDateCreation()));
            ps.setBoolean(6, notification.isVue());
            ps.setString(7, notification.getActionUrl());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("La création de la notification a échoué, aucune ligne affectée");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    notification.setIdNotification(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("La création de la notification a échoué, aucun ID généré");
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la notification", e);
            throw new DatabaseException("Erreur lors de la création de la notification: " + e.getMessage(), e);
        }

        return notification;
    }

    @Override
    public Notification update(Notification notification) {
        String query = "UPDATE NOTIFICATION SET id_utilisateur = ?, titre = ?, message = ?, " +
                "type = ?, date_creation = ?, vue = ?, action_url = ? " +
                "WHERE id_notification = ?";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, notification.getIdUtilisateur());
            ps.setString(2, notification.getTitre());
            ps.setString(3, notification.getMessage());
            ps.setString(4, notification.getType().name());
            ps.setTimestamp(5, Timestamp.valueOf(notification.getDateCreation()));
            ps.setBoolean(6, notification.isVue());
            ps.setString(7, notification.getActionUrl());
            ps.setInt(8, notification.getIdNotification());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("La mise à jour de la notification a échoué, aucune ligne affectée");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la notification", e);
            throw new DatabaseException("Erreur lors de la mise à jour de la notification: " + e.getMessage(), e);
        }

        return notification;
    }

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM NOTIFICATION WHERE id_notification = ?";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la notification", e);
            throw new DatabaseException("Erreur lors de la suppression de la notification: " + e.getMessage(), e);
        }
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM NOTIFICATION";

        try (Connection connection = dbUtil.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des notifications", e);
            throw new DatabaseException("Erreur lors du comptage des notifications: " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public List<Notification> findByUtilisateur(Integer idUtilisateur) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM NOTIFICATION WHERE id_utilisateur = ? ORDER BY date_creation DESC";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idUtilisateur);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des notifications par utilisateur", e);
            throw new DatabaseException(
                    "Erreur lors de la recherche des notifications par utilisateur: " + e.getMessage(), e);
        }

        return notifications;
    }

    @Override
    public List<Notification> findUnreadByUtilisateur(Integer idUtilisateur) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM NOTIFICATION WHERE id_utilisateur = ? AND vue = FALSE ORDER BY date_creation DESC";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idUtilisateur);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des notifications non lues", e);
            throw new DatabaseException("Erreur lors de la recherche des notifications non lues: " + e.getMessage(), e);
        }

        return notifications;
    }

    @Override
    public List<Notification> findByDateCreationBetween(LocalDateTime debut, LocalDateTime fin) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM NOTIFICATION WHERE date_creation BETWEEN ? AND ? ORDER BY date_creation DESC";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des notifications par période", e);
            throw new DatabaseException("Erreur lors de la recherche des notifications par période: " + e.getMessage(),
                    e);
        }

        return notifications;
    }

    @Override
    public boolean markAsRead(Integer idNotification) {
        String query = "UPDATE NOTIFICATION SET vue = TRUE WHERE id_notification = ?";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idNotification);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du marquage de la notification comme lue", e);
            throw new DatabaseException("Erreur lors du marquage de la notification comme lue: " + e.getMessage(), e);
        }
    }

    @Override
    public int markAllAsRead(Integer idUtilisateur) {
        String query = "UPDATE NOTIFICATION SET vue = TRUE WHERE id_utilisateur = ? AND vue = FALSE";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idUtilisateur);

            return ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du marquage de toutes les notifications comme lues", e);
            throw new DatabaseException(
                    "Erreur lors du marquage de toutes les notifications comme lues: " + e.getMessage(), e);
        }
    }

    @Override
    public int countUnread(Integer idUtilisateur) {
        String query = "SELECT COUNT(*) FROM NOTIFICATION WHERE id_utilisateur = ? AND vue = FALSE";

        try (Connection connection = dbUtil.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idUtilisateur);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des notifications non lues", e);
            throw new DatabaseException("Erreur lors du comptage des notifications non lues: " + e.getMessage(), e);
        }

        return 0;
    }
}
