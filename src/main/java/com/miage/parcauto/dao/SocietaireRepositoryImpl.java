package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Societaire;
import java.sql.*;
import java.util.*;

/**
 * Implémentation du repository pour la gestion des sociétaires.
 * Remplace SocietaireDao.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocietaireRepositoryImpl implements SocietaireRepository {
    private final DbUtil dbUtil;

    public SocietaireRepositoryImpl() {
        this.dbUtil = DbUtil.getInstance();
    }

    @Override
    public Optional<Societaire> findById(Integer id) {
        // ...implémentation JDBC...
        return Optional.empty();
    }

    @Override
    public List<Societaire> findAll() {
        // ...implémentation JDBC...
        return new ArrayList<>();
    }

    @Override
    public List<Societaire> findAll(int page, int size) {
        // ...implémentation JDBC avec LIMIT/OFFSET...
        return new ArrayList<>();
    }

    @Override
    public Societaire save(Societaire entity) {
        // ...implémentation JDBC...
        return entity;
    }

    @Override
    public Societaire update(Societaire entity) {
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
    public List<Societaire> findByNom(String nom) {
        // ...implémentation JDBC...
        return new ArrayList<>();
    }
}
