package com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DocumentSocietaireRepository;
import main.java.com.miage.parcauto.dto.DocumentSocietaireDTO;
import main.java.com.miage.parcauto.exception.DocumentNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.mapper.DocumentSocietaireMapper;
import main.java.com.miage.parcauto.model.document.DocumentSocietaire;
import main.java.com.miage.parcauto.model.document.TypeDocument;
import main.java.com.miage.parcauto.service.DocumentSocietaireService;
import main.java.com.miage.parcauto.service.ValidationService; // Assurez-vous que ValidationService est utilisable

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DocumentSocietaireServiceImpl implements DocumentSocietaireService {

    private final DocumentSocietaireRepository documentSocietaireRepository;
    private final DocumentSocietaireMapper documentSocietaireMapper;
    private final ValidationService validationService;

    public DocumentSocietaireServiceImpl(DocumentSocietaireRepository documentSocietaireRepository,
            DocumentSocietaireMapper documentSocietaireMapper,
            ValidationService validationService) {
        this.documentSocietaireRepository = documentSocietaireRepository;
        this.documentSocietaireMapper = documentSocietaireMapper;
        this.validationService = validationService;
    }

    @Override
    public DocumentSocietaireDTO createDocumentSocietaire(DocumentSocietaireDTO documentSocietaireDTO) {
        validationService.validateDocumentSocietaire(documentSocietaireDTO);
        DocumentSocietaire documentSocietaire = documentSocietaireMapper.toEntity(documentSocietaireDTO);
        DocumentSocietaire savedDocument = documentSocietaireRepository.save(documentSocietaire);
        if (savedDocument == null) {
            throw new OperationFailedException("La création du document sociétaire a échoué");
        }
        return documentSocietaireMapper.toDto(savedDocument);
    }

    @Override
    public Optional<DocumentSocietaireDTO> getDocumentSocietaireById(Integer idDocument) {
        return documentSocietaireRepository.findById(idDocument)
                .map(documentSocietaireMapper::toDto);
    }

    @Override
    public List<DocumentSocietaireDTO> getAllDocumentsSocietaire() {
        return documentSocietaireRepository.findAll().stream()
                .map(documentSocietaireMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentSocietaireDTO> getDocumentsByCompteSocietaireId(Integer idSocietaire) {
        return documentSocietaireRepository.findBySocietaireId(idSocietaire).stream()
                .map(documentSocietaireMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentSocietaireDTO> getDocumentsByCompteSocietaireIdAndType(Integer idSocietaire,
            TypeDocument typeDocument) {
        return documentSocietaireRepository.findBySocietaireIdAndType(idSocietaire, typeDocument).stream()
                .map(documentSocietaireMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentSocietaireDTO updateDocumentSocietaire(Integer idDocument,
            DocumentSocietaireDTO documentSocietaireDTO) {
        validationService.validateDocumentSocietaire(documentSocietaireDTO);
        if (!documentSocietaireRepository.findById(idDocument).isPresent()) {
            throw new DocumentNotFoundException("Document sociétaire non trouvé avec l'ID : " + idDocument);
        }
        DocumentSocietaire documentSocietaire = documentSocietaireMapper.toEntity(documentSocietaireDTO);
        documentSocietaire.setIdDocument(idDocument); // Assurez-vous que l'ID est bien défini pour la mise à jour
        DocumentSocietaire updatedDocument = documentSocietaireRepository.update(documentSocietaire);
        if (updatedDocument == null) {
            throw new OperationFailedException(
                    "La mise à jour du document sociétaire a échoué pour l'ID : " + idDocument);
        }
        return documentSocietaireMapper.toDto(updatedDocument);
    }

    @Override
    public void deleteDocumentSocietaire(Integer idDocument) {
        if (!documentSocietaireRepository.findById(idDocument).isPresent()) {
            throw new DocumentNotFoundException(
                    "Document sociétaire non trouvé avec l'ID : " + idDocument + ", suppression annulée.");
        }
        boolean deleted = documentSocietaireRepository.delete(idDocument);
        if (!deleted) {
            throw new OperationFailedException(
                    "La suppression du document sociétaire a échoué pour l'ID : " + idDocument);
        }
    }
}