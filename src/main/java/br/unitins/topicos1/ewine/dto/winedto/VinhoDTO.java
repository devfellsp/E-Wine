// src/main/java/br/unitins/topicos1/ewine/dto/winedto/VinhoDTO. java
package br.unitins.topicos1.ewine.dto.winedto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta. validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VinhoDTO(
    
    @NotBlank(message = "Nome do vinho é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String nome,
    
    @Size(max = 1000, message = "Descrição não pode exceder 1000 caracteres")
    String descricao,
    
    String imagem,
    
    @Positive(message = "Teor alcoólico deve ser positivo")
    Double teorAlcoolico,
    
    @Positive(message = "Volume deve ser positivo")
    Integer volume,
    
    @Size(max = 500, message = "Harmonização não pode exceder 500 caracteres")
    String harmonizacao,
    
    @Size(max = 1000, message = "Notas de degustação não podem exceder 1000 caracteres")
    String notasDegustacao,
    
    @Size(max = 50, message = "Temperatura de serviço não pode exceder 50 caracteres")
    String servicoTemperatura,
    
    // IDs dos relacionamentos
    List<Long> idsUvas,
    
    @NotNull(message = "Tipo de vinho é obrigatório")
    Long idTipoVinho,
    
    @NotNull(message = "País de origem é obrigatório")
    Long idPais,
    
    Long idEstilo,
    Long idOcasiao,
    Long idSafra,
    
    @NotNull(message = "Marca é obrigatória")
    Long idMarca
    
    // ============================================
    // CAMPOS REMOVIDOS (agora estão em ProdutoDTO):
    // - Double preco
    // - Integer quantEstoque  
    // - String sku
    // ============================================
) {}