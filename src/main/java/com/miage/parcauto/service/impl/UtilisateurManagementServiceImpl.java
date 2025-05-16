package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.dao.UtilisateurRepository;
import main.java.com.miage.parcauto.dao.impl.UtilisateurRepositoryImpl;
import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.UtilisateurMapper;
import main.java.com.miage.parcauto.mapper.impl.UtilisateurMapperImpl;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import main.java.com.miage.parcauto.model.utilisateur.Role;
import main.java.com.miage.parcauto.security.PasswordUtil;
import main.java.com.miage.parcauto.service.UtilisateurManagementService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UtilisateurManagementServiceImpl implements UtilisateurManagementService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    // private final ValidationService validationService; // À injecter si vous l'utilisez

    public UtilisateurManagementServiceImpl() {
        this.utilisateurRepository = new UtilisateurRepositoryImpl();
        this.utilisateurMapper = new UtilisateurMapperImpl();
        // this.validationService = new ValidationServiceImpl();
    }

    public UtilisateurManagementServiceImpl(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }


    @Override
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO) throws ValidationException, DuplicateEntityException, OperationFailedException {
        // validationService.validateUtilisateur(utilisateurDTO, true); // Décommenter si ValidationService est prêt

        if (utilisateurDTO == null || utilisateurDTO.getLogin() == null || utilisateurDTO.getLogin().trim().isEmpty()
                || utilisateurDTO.getMotDePasse() == null || utilisateurDTO.getMotDePasse().isEmpty()) {
            throw new ValidationException("Login et mot de passe sont requis pour la création.");
        }
        if (utilisateurDTO.getRole() == null) {
            throw new ValidationException("Le rôle est requis pour la création.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (utilisateurRepository.findByLogin(conn, utilisateurDTO.getLogin()).isPresent()) {
                throw new DuplicateEntityException("Un utilisateur avec le login '" + utilisateurDTO.getLogin() + "' existe déjà.");
            }

            Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);

            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(utilisateurDTO.getMotDePasse(), salt);

            utilisateur.setSalt(salt);
            utilisateur.setHash(hashedPassword);
            utilisateur.setActif(true); // Actif par défaut à la création
            utilisateur.setDateCreation(LocalDateTime.now());
            // idPersonnel sera déjà dans l'entité via le mapper si présent dans DTO

            Utilisateur savedUtilisateur = utilisateurRepository.save(conn, utilisateur);
            conn.commit();
            return utilisateurMapper.toDTO(savedUtilisateur);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création de l'utilisateur.", e);
        } catch (PasswordUtil.ParcAutoSecurityException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur de sécurité lors du hachage du mot de passe.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<UtilisateurDTO> getUtilisateurById(Integer idUtilisateur) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(conn, idUtilisateur);
            return utilisateurOpt.map(utilisateurMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de l'utilisateur par ID.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<UtilisateurDTO> getUtilisateurByLogin(String login) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByLogin(conn, login);
            return utilisateurOpt.map(utilisateurMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de l'utilisateur par login.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<UtilisateurDTO> getAllUtilisateurs() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll(conn);
            return utilisateurMapper.toDTOList(utilisateurs);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de tous les utilisateurs.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public UtilisateurDTO updateUtilisateur(UtilisateurDTO utilisateurDTO) throws ValidationException, UtilisateurNotFoundException, DuplicateEntityException, OperationFailedException {
        // validationService.validateUtilisateur(utilisateurDTO, false); // Décommenter si ValidationService est prêt
        if (utilisateurDTO == null || utilisateurDTO.getId() == null) {
            throw new ValidationException("ID utilisateur est requis pour la mise à jour.");
        }
        if (utilisateurDTO.getLogin() != null && utilisateurDTO.getLogin().trim().isEmpty()) {
            throw new ValidationException("Le login ne peut être vide.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Utilisateur existingUtilisateur = utilisateurRepository.findById(conn, utilisateurDTO.getId())
                    .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + utilisateurDTO.getId()));

            // Vérifier si le login a changé et s'il entre en conflit
            if (utilisateurDTO.getLogin() != null && !utilisateurDTO.getLogin().equals(existingUtilisateur.getLogin())) {
                if (utilisateurRepository.findByLogin(conn, utilisateurDTO.getLogin()).isPresent()) {
                    throw new DuplicateEntityException("Un utilisateur avec le login '" + utilisateurDTO.getLogin() + "' existe déjà.");
                }
                existingUtilisateur.setLogin(utilisateurDTO.getLogin());
            }

            if (utilisateurDTO.getRole() != null) {
                try {
                    existingUtilisateur.setRole(Role.valueOf(utilisateurDTO.getRole()));
                } catch (IllegalArgumentException e) {
                    throw new ValidationException("Rôle invalide: " + utilisateurDTO.getRole());
                }
            }

            // Mise à jour du mot de passe si fourni
            if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty()) {
                String salt = PasswordUtil.generateSalt();
                String hashedPassword = PasswordUtil.hashPassword(utilisateurDTO.getMotDePasse(), salt);
                existingUtilisateur.setSalt(salt);
                existingUtilisateur.setHash(hashedPassword);
            }

            if (utilisateurDTO.getIdPersonnel() != null) { // Permet de lier/délier un personnel
                existingUtilisateur.setIdPersonnel(utilisateurDTO.getIdPersonnel());
            }
            if(utilisateurDTO.getMfaSecret() != null) { // Permet de mettre à jour le secret MFA
                existingUtilisateur.setMfaSecret(utilisateurDTO.getMfaSecret());
            }
            existingUtilisateur.setActif(utilisateurDTO.isActif()); // Permet de changer l'état actif/inactif

            Utilisateur updatedUtilisateur = utilisateurRepository.update(conn, existingUtilisateur);
            conn.commit();
            return utilisateurMapper.toDTO(updatedUtilisateur);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour de l'utilisateur.", e);
        } catch (PasswordUtil.ParcAutoSecurityException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur de sécurité lors de la mise à jour du mot de passe.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void deleteUtilisateur(Integer idUtilisateur) throws UtilisateurNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (utilisateurRepository.findById(conn, idUtilisateur).isEmpty()) {
                throw new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + idUtilisateur);
            }
            // Ajouter ici la logique pour vérifier les dépendances avant suppression si nécessaire
            // Par exemple, un utilisateur ne peut pas être supprimé s'il a des missions en cours, etc.

            boolean deleted = utilisateurRepository.delete(conn, idUtilisateur);
            if (!deleted) {
                throw new OperationFailedException("La suppression de l'utilisateur a échoué pour une raison inconnue.");
            }
            conn.commit();

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            // Gérer les contraintes de clé étrangère spécifiquement si possible
            if (e.getSQLState().startsWith("23")) { // Code d'erreur SQL pour violation de contrainte d'intégrité
                throw new OperationFailedException("Impossible de supprimer l'utilisateur car il est référencé ailleurs (ex: missions, affectations).", e);
            }
            throw new OperationFailedException("Erreur technique lors de la suppression de l'utilisateur.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void changePassword(Integer idUtilisateur, String nouveauMotDePasse) throws ValidationException, UtilisateurNotFoundException, OperationFailedException {
        if (nouveauMotDePasse == null || nouveauMotDePasse.trim().isEmpty()) {
            throw new ValidationException("Le nouveau mot de passe ne peut être vide.");
        }
        // Ajouter ici d'autres règles de validation pour le mot de passe si nécessaire (longueur, complexité)

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Utilisateur utilisateur = utilisateurRepository.findById(conn, idUtilisateur)
                    .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + idUtilisateur));

            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(nouveauMotDePasse, salt);

            utilisateur.setSalt(salt);
            utilisateur.setHash(hashedPassword);

            utilisateurRepository.update(conn, utilisateur); // update devrait mettre à jour seulement hash et salt
            conn.commit();

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors du changement de mot de passe.", e);
        } catch (PasswordUtil.ParcAutoSecurityException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur de sécurité lors du hachage du nouveau mot de passe.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void setUtilisateurActif(Integer idUtilisateur, boolean actif) throws UtilisateurNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Utilisateur utilisateur = utilisateurRepository.findById(conn, idUtilisateur)
                    .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + idUtilisateur));

            utilisateur.setActif(actif);
            utilisateurRepository.update(conn, utilisateur); // Assurez-vous que update peut gérer ce changement
            conn.commit();

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors du changement de l'état d'activité de l'utilisateur.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}