// src/main/java/br/unitins/topicos1/ewine/dto/productdto/ProdutoDTO. java
package br.unitins.topicos1.ewine.dto.comercio;

import jakarta.validation.constraints. NotBlank;
import jakarta. validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints. Positive;
import jakarta.validation. constraints.Size;

public record ProdutoDTO(
    
    @NotBlank(message = "SKU é obrigatório")
    @Pattern(regexp = "^[A-Z0-9-_]+$", message = "SKU deve conter apenas letras maiúsculas, números, hífen e underscore")
    @Size(min = 3, max = 20, message = "SKU deve ter entre 3 e 20 caracteres")
    String sku,
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    Double preco,
    
    Boolean ativo, // Default será true no service
    
    @Size(max = 500, message = "Descrição comercial não pode exceder 500 caracteres")
    String descricaoComercial,
    
    @NotNull(message = "ID do vinho é obrigatório")
    Long idVinho,
    
    // Dados iniciais do estoque
    @NotNull(message = "Quantidade inicial é obrigatória")
    @Positive(message = "Quantidade inicial deve ser positiva")
    Integer quantidadeInicial,
    
    Integer estoqueMinimo, // Default será 5
    
    Integer estoqueMaximo, // Default será 1000
    
    String observacoesEstoque
    
) {
    
    // Construtor com valores padrão
    public ProdutoDTO {
        if (ativo == null) {
            ativo = true;
        }
        if (estoqueMinimo == null) {
            estoqueMinimo = 5;
        }
        if (estoqueMaximo == null) {
            estoqueMaximo = 1000;
        }
    }
}