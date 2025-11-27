// src/main/java/br/unitins/topicos1/ewine/dto/productdto/MovimentacaoEstoqueDTO.java
package br.unitins.topicos1.ewine.dto.comercio;

import jakarta. validation.constraints.NotBlank;
import jakarta.validation.constraints. NotNull;
import jakarta.validation.constraints. Positive;

public record MovimentacaoEstoqueDTO(
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    Integer quantidade,
    
    @NotBlank(message = "Tipo de movimentação é obrigatório")
    String tipoMovimentacao, // "ENTRADA", "SAIDA", "AJUSTE"
    
    String motivo // "COMPRA", "VENDA", "DEVOLUCAO", "PERDA", "AJUSTE_INVENTARIO"
) {}