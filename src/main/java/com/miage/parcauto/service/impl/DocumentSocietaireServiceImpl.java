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
        if (document == null) {
            return null;
        }
        DocumentSocietaireDTO dto = new DocumentSocietaireDTO();
        dto.setIdDocument(document.getIdDoc());
        dto.setIdSocietaire(document.getIdSocietaire());
        dto.setTypeDocument(document.getTypeDoc() != null ? document.getTypeDoc().name() : null);
        dto.setCheminFichier(document.getCheminFichier());
        dto.setDateUpload(document.getDateUpload());
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentSocietaire toEntity(DocumentSocietaireDTO dto) {
        if (dto == null) {
            return null;
        }
        DocumentSocietaire entity = new DocumentSocietaire();
        entity.setIdDoc(dto.getIdDocument());
        entity.setIdSocietaire(dto.getIdSocietaire());
        if (dto.getTypeDocument() != null) {
            try {
                entity.setTypeDoc(TypeDocument.valueOf(dto.getTypeDocument()));
            } catch (IllegalArgumentException e) {
                // Logguer l'erreur ou lever une exception de mapping spécifique si nécessaire
                System.err.println("Type de document invalide dans DTO lors du mapping vers entité: " + dto.getTypeDocument());
                // Selon la politique de gestion d'erreur, on pourrait affecter null ou lever une exception.
                // Pour l'instant, on laisse le champ TypeDoc à null si la conversion échoue.
            }
        }
        entity.setCheminFichier(dto.getCheminFichier());
        entity.setDateUpload(dto.getDateUpload());
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaireDTO> toDTOList(List<DocumentSocietaire> documents) {
        if (documents == null || documents.isEmpty()) {
            return Collections.emptyList();
        }
        return documents.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentSocietaire> toEntityList(List<DocumentSocietaireDTO> documentDTOs) {
        if (documentDTOs == null || documentDTOs.isEmpty()) {
            return Collections.emptyList();
        }
        return documentDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}