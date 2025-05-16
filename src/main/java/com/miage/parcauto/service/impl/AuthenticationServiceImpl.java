package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.dao.UtilisateurRepository;
import main.java.com.miage.parcauto.dao.impl.UtilisateurRepositoryImpl;
import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.exception.AuthenticationException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.mapper.UtilisateurMapper;
import main.java.com.miage.parcauto.mapper.impl.UtilisateurMapperImpl;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import main.java.com.miage.parcauto.security.PasswordUtil;
import main.java.com.miage.parcauto.service.AuthenticationService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    public AuthenticationServiceImpl() {
        this.utilisateurRepository = new UtilisateurRepositoryImpl();
        this.utilisateurMapper = new UtilisateurMapperImpl();
    }

    public AuthenticationServiceImpl(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    @Override
    public Optional<UtilisateurDTO> authenticate(String login, String motDePasse) throws AuthenticationException, OperationFailedException {
        if (login == null || login.trim().isEmpty() || motDePasse == null || motDePasse.isEmpty()) {
            throw new AuthenticationException("Login et mot de passe ne peuvent être vides.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByLogin(conn, login);

            if (utilisateurOpt.isEmpty()) {
                throw new AuthenticationException("Login ou mot de passe incorrect.");
            }

            Utilisateur utilisateur = utilisateurOpt.get();

            if (!utilisateur.isActif()) {
                throw new AuthenticationException("Le compte utilisateur est inactif.");
            }

            boolean passwordMatch;
            try {
                passwordMatch = PasswordUtil.verifyPassword(motDePasse, utilisateur.getSalt(), utilisateur.getHash());
            } catch (PasswordUtil.ParcAutoSecurityException e) {
                throw new OperationFailedException("Erreur de sécurité lors de la vérification du mot de passe.", e);
            }

            if (!passwordMatch) {
                throw new AuthenticationException("Login ou mot de passe incorrect.");
            }

            utilisateur.setDateDerniereConnexion(LocalDateTime.now());
            utilisateurRepository.update(conn, utilisateur);

            conn.commit();
            return Optional.of(utilisateurMapper.toDTO(utilisateur));

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de l'authentification.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}