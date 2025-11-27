// src/main/java/br/unitins/topicos1/ewine/dto/productdto/EstoqueDTO.java
package br.unitins.topicos1.ewine.dto.comercio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints. PositiveOrZero;

public record EstoqueDTO(
    
    @NotNull(message = "Quantidade é obrigatória")
    @PositiveOrZero(message = "Quantidade não pode ser negativa")
    Integer quantidade,
    
    @Min(value = 1, message = "Estoque mínimo deve ser maior que 0")
    Integer estoqueMinimo,
    
    @Min(value = 1, message = "Estoque máximo deve ser maior que 0")
    Integer estoqueMaximo,
    
    String observacoes
) {
    
    // Construtor com valores padrão
    public EstoqueDTO {
        if (estoqueMinimo == null) {
            estoqueMinimo = 5;
        }
        if (estoqueMaximo == null) {
            estoqueMaximo = 1000;
        }
    }
}