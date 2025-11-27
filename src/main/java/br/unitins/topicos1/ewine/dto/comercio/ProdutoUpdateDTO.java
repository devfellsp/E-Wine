// src/main/java/br/unitins/topicos1/ewine/dto/productdto/ProdutoUpdateDTO.java
package br.unitins.topicos1.ewine.dto.comercio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta. validation.constraints.Size;

public record ProdutoUpdateDTO(
    
    @NotBlank(message = "SKU é obrigatório")
    @Pattern(regexp = "^[A-Z0-9-_]+$", message = "SKU deve conter apenas letras maiúsculas, números, hífen e underscore")
    @Size(min = 3, max = 20, message = "SKU deve ter entre 3 e 20 caracteres")
    String sku,
    
    @Positive(message = "Preço deve ser positivo")
    Double preco,
    
    Boolean ativo,
    
    @Size(max = 500, message = "Descrição comercial não pode exceder 500 caracteres")
    String descricaoComercial
    
    // NÃO inclui dados de estoque - isso é gerenciado separadamente
) {}