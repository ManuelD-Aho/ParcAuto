package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.ServiceRHRepository;
import main.java.com.miage.parcauto.model.rh.Service;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceRHRepositoryImpl implements ServiceRHRepository {

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setIdService(rs.getInt("id_service"));
        service.setLibelle(rs.getString("libelle"));
        return service;
    }

    @Override
    public Optional<Service> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id_service, libelle FROM SERVICE WHERE id_service = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToService(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Service> findAll(Connection conn) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT id_service, libelle FROM SERVICE";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }

    @Override
    public List<Service> findAll(Connection conn, int page, int size) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT id_service, libelle FROM SERVICE LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des services RH", e);
        }
        return services;
    }

    @Override
    public Service save(Connection conn, Service service) throws SQLException {
        String sql = "INSERT INTO SERVICE (libelle) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, service.getLibelle());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création du service RH a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    service.setIdService(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création du service RH a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde du service RH: " + service.getLibelle(), e);
        }
        return service;
    }

    @Override
    public Service update(Connection conn, Service service) throws SQLException {
        String sql = "UPDATE SERVICE SET libelle = ? WHERE id_service = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, service.getLibelle());
            pstmt.setInt(2, service.getIdService());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour du service RH avec ID " + service.getIdService()
                        + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour du service RH: " + service.getIdService(), e);
        }
        return service;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM SERVICE WHERE id_service = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression du service RH: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SERVICE";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des services RH", e);
        }
        return 0;
    }

    @Override
    public Optional<Service> findByLibelle(Connection conn, String libelle) throws SQLException {
        String sql = "SELECT id_service, libelle FROM SERVICE WHERE libelle = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libelle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToService(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du service RH par libellé: " + libelle, e);
        }
        return Optional.empty();
    }
}