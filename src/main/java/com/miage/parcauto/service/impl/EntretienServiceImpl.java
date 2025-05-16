package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.dao.EntretienRepository;
import main.java.com.miage.parcauto.dao.VehiculeRepository;
import main.java.com.miage.parcauto.dao.impl.EntretienRepositoryImpl;
import main.java.com.miage.parcauto.dao.impl.VehiculeRepositoryImpl;
import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.EntretienMapper;
import main.java.com.miage.parcauto.mapper.impl.EntretienMapperImpl;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.StatutOT;
import main.java.com.miage.parcauto.model.vehicule.Vehicule; // Pour mettre à jour le km
import main.java.com.miage.parcauto.service.EntretienService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EntretienServiceImpl implements EntretienService {

    private final EntretienRepository entretienRepository;
    private final VehiculeRepository vehiculeRepository; // Nécessaire pour vérifier l'existence du véhicule
    private final EntretienMapper entretienMapper;
    // private final ValidationService validationService;

    public EntretienServiceImpl() {
        this.entretienRepository = new EntretienRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.entretienMapper = new EntretienMapperImpl();
        // this.validationService = new ValidationServiceImpl();
    }

    public EntretienServiceImpl(EntretienRepository entretienRepository, VehiculeRepository vehiculeRepository, EntretienMapper entretienMapper) {
        this.entretienRepository = entretienRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.entretienMapper = entretienMapper;
    }

    @Override
    public EntretienDTO createEntretien(EntretienDTO entretienDTO) throws ValidationException, VehiculeNotFoundException, OperationFailedException {
        // validationService.validateEntretien(entretienDTO);
        if (entretienDTO == null || entretienDTO.getIdVehicule() == null) {
            throw new ValidationException("ID Véhicule est requis pour créer un entretien.");
        }
        if (entretienDTO.getDateEntree() == null || entretienDTO.getMotif() == null || entretienDTO.getMotif().trim().isEmpty()) {
            throw new ValidationException("Date d'entrée et motif sont requis.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findById(conn, entretienDTO.getIdVehicule()).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + entretienDTO.getIdVehicule());
            }

            Entretien entretien = entretienMapper.toEntity(entretienDTO);
            if (entretien.getStatutOt() == null) { // Statut par défaut si non fourni
                entretien.setStatutOt(StatutOT.OUVERT);
            }

            Entretien savedEntretien = entretienRepository.save(conn, entretien);
            conn.commit();
            return entretienMapper.toDTO(savedEntretien);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création de l'entretien.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<EntretienDTO> getEntretienById(Integer idEntretien) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Entretien> entretienOpt = entretienRepository.findById(conn, idEntretien);
            return entretienOpt.map(entretienMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de l'entretien par ID.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<EntretienDTO> getAllEntretiens() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Entretien> entretiens = entretienRepository.findAll(conn);
            return entretienMapper.toDTOList(entretiens);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de tous les entretiens.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<EntretienDTO> getEntretiensByVehiculeId(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            List<Entretien> entretiens = entretienRepository.findByVehiculeId(conn, idVehicule);
            return entretienMapper.toDTOList(entretiens);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des entretiens pour le véhicule ID: " + idVehicule, e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public EntretienDTO updateEntretien(EntretienDTO entretienDTO) throws ValidationException, EntretienNotFoundException, VehiculeNotFoundException, OperationFailedException {
        // validationService.validateEntretien(entretienDTO);
        if (entretienDTO == null || entretienDTO.getIdEntretien() == null) {
            throw new ValidationException("ID Entretien est requis pour la mise à jour.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Entretien existingEntretien = entretienRepository.findById(conn, entretienDTO.getIdEntretien())
                    .orElseThrow(() -> new EntretienNotFoundException("Entretien non trouvé avec l'ID: " + entretienDTO.getIdEntretien()));

            if (entretienDTO.getIdVehicule() != null && !entretienDTO.getIdVehicule().equals(existingEntretien.getIdVehicule())) {
                if (vehiculeRepository.findById(conn, entretienDTO.getIdVehicule()).isEmpty()) {
                    throw new VehiculeNotFoundException("Nouveau véhicule associé non trouvé avec l'ID: " + entretienDTO.getIdVehicule());
                }
                existingEntretien.setIdVehicule(entretienDTO.getIdVehicule());
            }

            if(entretienDTO.getDateEntree() != null) existingEntretien.setDateEntree(entretienDTO.getDateEntree());
            if(entretienDTO.getDateSortie() != null) existingEntretien.setDateSortie(entretienDTO.getDateSortie());
            if(entretienDTO.getMotif() != null) existingEntretien.setMotif(entretienDTO.getMotif());
            if(entretienDTO.getObservations() != null) existingEntretien.setObservations(entretienDTO.getObservations());
            if(entretienDTO.getCoutEstime() != null) existingEntretien.setCoutEstime(entretienDTO.getCoutEstime());
            if(entretienDTO.getCoutReel() != null) existingEntretien.setCoutReel(entretienDTO.getCoutReel());
            if(entretienDTO.getLieu() != null) existingEntretien.setLieu(entretienDTO.getLieu());
            if(entretienDTO.getTypeEntretien() != null) existingEntretien.setType(main.java.com.miage.parcauto.model.entretien.TypeEntretien.valueOf(entretienDTO.getTypeEntretien()));
            if(entretienDTO.getStatutOt() != null) existingEntretien.setStatutOt(main.java.com.miage.parcauto.model.entretien.StatutOT.valueOf(entretienDTO.getStatutOt()));


            Entretien updatedEntretien = entretienRepository.update(conn, existingEntretien);
            conn.commit();
            return entretienMapper.toDTO(updatedEntretien);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour de l'entretien.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void deleteEntretien(Integer idEntretien) throws EntretienNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (entretienRepository.findById(conn, idEntretien).isEmpty()) {
                throw new EntretienNotFoundException("Entretien non trouvé avec l'ID: " + idEntretien);
            }
            boolean deleted = entretienRepository.delete(conn, idEntretien);
            if(!deleted) {
                throw new OperationFailedException("La suppression de l'entretien a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la suppression de l'entretien.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public EntretienDTO terminerEntretien(Integer idEntretien, LocalDateTime dateRealisation, BigDecimal coutReel, Integer kmVehicule, Integer kmProchainEntretien, String observations) throws EntretienNotFoundException, ValidationException, OperationFailedException {
        if (dateRealisation == null || coutReel == null || kmVehicule == null) {
            throw new ValidationException("Date de réalisation, coût réel et kilométrage véhicule sont requis pour terminer un entretien.");
        }
        if (coutReel.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Le coût réel ne peut pas être négatif.");
        }
        if (kmVehicule < 0) {
            throw new ValidationException("Le kilométrage du véhicule ne peut pas être négatif.");
        }
        if (kmProchainEntretien != null && kmProchainEntretien < kmVehicule) {
            throw new ValidationException("Le kilométrage du prochain entretien ne peut pas être inférieur au kilométrage actuel du véhicule.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Entretien entretien = entretienRepository.findById(conn, idEntretien)
                    .orElseThrow(() -> new EntretienNotFoundException("Entretien non trouvé avec l'ID: " + idEntretien));

            if (entretien.getStatutOt() == StatutOT.CLOTURE) {
                throw new ValidationException("L'entretien est déjà clôturé.");
            }

            Vehicule vehicule = vehiculeRepository.findById(conn, entretien.getIdVehicule())
                    .orElseThrow(() -> new OperationFailedException("Véhicule associé à l'entretien non trouvé. ID: " + entretien.getIdVehicule()));


            entretien.setDateSortie(dateRealisation); // Date de sortie = date de réalisation
            entretien.setCoutReel(coutReel);
            entretien.setObservations((entretien.getObservations() == null ? "" : entretien.getObservations() + "\n") + "Clôture: " + (observations == null ? "" : observations));
            entretien.setStatutOt(StatutOT.CLOTURE);
            // entretien.setKmRealisation(kmVehicule); // Si vous avez ce champ dans le modèle Entretien
            // entretien.setKmProchainEntretien(kmProchainEntretien); // Si vous avez ce champ

            // Mettre à jour le kilométrage du véhicule s'il est supérieur à l'actuel
            if (kmVehicule > vehicule.getKmActuels()) {
                vehicule.setKmActuels(kmVehicule);
                vehiculeRepository.update(conn, vehicule);
            } else if (kmVehicule < vehicule.getKmActuels()){
                // Potentiellement une alerte ou une validation plus stricte
                System.err.println("Avertissement: Kilométrage à la fin de l'entretien ("+kmVehicule+") est inférieur au km actuel du véhicule ("+vehicule.getKmActuels()+").");
            }


            Entretien updatedEntretien = entretienRepository.update(conn, entretien);
            conn.commit();
            return entretienMapper.toDTO(updatedEntretien);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la clôture de l'entretien.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<EntretienDTO> getEntretiensPlanifiesEntre(LocalDateTime dateDebut, LocalDateTime dateFin) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            // La méthode findScheduledBetween dans le repo doit utiliser date_entree_entr pour les entretiens planifiés
            List<Entretien> entretiens = entretienRepository.findScheduledBetween(conn, dateDebut, dateFin);
            return entretienMapper.toDTOList(entretiens);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des entretiens planifiés.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}