package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.DocumentSocietaireDTO;
import main.java.com.miage.parcauto.mapper.DocumentSocietaireMapper;
import main.java.com.miage.parcauto.model.document.DocumentSocietaire;
import main.java.com.miage.parcauto.model.document.TypeDocument;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation de {@link DocumentSocietaireMapper}.
 */
public class DocumentSocietaireMapperImpl implements DocumentSocietaireMapper {
    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentSocietaireDTO toDTO(DocumentSocietaire document) {
        if (document == null)
            return null;
        DocumentSocietaireDTO dto = new DocumentSocietaireDTO();
        dto.setIdDocument(document.getIdDocument());
        dto.setIdSocietaire(document.getIdSocietaire());
        dto.setTypeDocument(document.getTypeDocument() != null ? document.getTypeDocument().name() : null);
        dto.setCheminFichier(document.getCheminFichier());
        dto.setDateDebutValidite(document.getDateUpload());
        // Pas de dateFinValidite côté modèle, donc non mappé
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentSocietaire toEntity(DocumentSocietaireDTO dto) {
        if (dto == null)
            return null;
        DocumentSocietaire document = new DocumentSocietaire();
        document.setIdDocument(dto.getIdDocument());
        document.setIdSocietaire(dto.getIdSocietaire());
        document.setTypeDocument(dto.getTypeDocument() != null ? TypeDocument.valueOf(dto.getTypeDocument()) : null);
        document.setCheminFichier(dto.getCheminFichier());
        document.setDateUpload(dto.getDateDebutValidite());
        return document;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaireDTO> toDTOList(List<DocumentSocietaire> documents) {
        if (documents == null)
            return Collections.emptyList();
        return documents.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> toEntityList(List<DocumentSocietaireDTO> documentDTOs) {
        if (documentDTOs == null)
            return Collections.emptyList();
        return documentDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}