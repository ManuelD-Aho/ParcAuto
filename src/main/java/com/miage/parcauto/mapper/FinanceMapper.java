package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.model.finance.BilanFinancier; // Supposons que ce modèle existe

/**
 * Interface pour le mapping entre l'objet BilanFinancier et son DTO.
 */
public interface FinanceMapper {

    /**
     * Convertit un objet BilanFinancier en BilanFinancierDTO.
     * @param bilan L'objet BilanFinancier.
     * @return Le DTO correspondant, ou null si l'objet est nul.
     */
    BilanFinancierDTO toDTO(BilanFinancier bilan);

    // La conversion de DTO vers Entité n'est généralement pas nécessaire pour les objets de reporting
}