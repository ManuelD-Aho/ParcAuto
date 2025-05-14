package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import java.sql.*;
import java.util.*;

/**
 * Implémentation du repository pour la gestion des utilisateurs.
 * Remplace UtilisateurDao.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class UtilisateurRepositoryImpl implements UtilisateurRepository {
    private final DbUtil dbUtil;

    public UtilisateurRepositoryImpl() {
        this.dbUtil = DbUtil.getInstance();
    }

    @Override
    public Optional<Utilisateur> findById(Integer id) {
        // ...implémentation JDBC...
        return Optional.empty();
    }

    @Override
    public List<Utilisateur> findAll() {
        // ...implémentation JDBC...
        return new ArrayList<>();
    }

    @Override
    public List<Utilisateur> findAll(int page, int size) {
        // ...implémentation JDBC avec LIMIT/OFFSET...
        return new ArrayList<>();
    }

    @Override
    public Utilisateur save(Utilisateur entity) {
        // ...implémentation JDBC...
        return entity;
    }

    @Override
    public Utilisateur update(Utilisateur entity) {
        // ...implémentation JDBC...
        return entity;
    }

    @Override
    public boolean delete(Integer id) {
        // ...implémentation JDBC...
        return false;
    }

    @Override
    public long count() {
        // ...implémentation JDBC...
        return 0;
    }

    @Override
    public Optional<Utilisateur> findByLogin(String login) {
        // ...implémentation JDBC...
        return Optional.empty();
    }

    @Override
    public Optional<Utilisateur> authenticate(String login, String passwordHash) {
        // ...implémentation JDBC...
        return Optional.empty();
    }
}
