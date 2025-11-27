// src/main/java/br/unitins/topicos1/ewine/dto/winedto/VinhoDTOResponse.java
package br.unitins.topicos1.ewine.dto.winedto;

import java.util.List;
import br.unitins.topicos1.ewine.dto.locationdto.PaisDTOResponse;
import br.unitins.topicos1.ewine.dto.OthersDTO.MarcaDTOResponse;
import br.unitins.topicos1. ewine.model.wineentities.Vinho;

public record VinhoDTOResponse(
    Long id,
    String nome,
    String descricao,
    String imagem,
    Double teorAlcoolico,
    String teorAlcoolicoFormatado, // Ex: "13.5%"
    Integer volume,
    String volumeFormatado, // Ex: "750ml"
    String harmonizacao,
    String notasDegustacao,
    String servicoTemperatura,
    String nomeCompleto, // Ex: "Cabernet Sauvignon 2020 - 750ml"
    
    // Relacionamentos (DTOs aninhados)
    List<UvaDTOResponse> uvas,
    TipoVinhoDTOResponse tipoVinho,
    PaisDTOResponse paisDeOrigem,
    EstiloDTOResponse estilo,
    OcasiaoDTOResponse ocasiao,
    SafraDTOResponse safra,
    MarcaDTOResponse marca
    
    // ============================================
    // CAMPOS REMOVIDOS (agora estão em ProdutoResponseDTO):
    // - Double preco
    // - Integer quantEstoque  
    // - String sku
    // ============================================
) {
    
    public static VinhoDTOResponse valueOf(Vinho vinho) {
        return new VinhoDTOResponse(
            vinho. getId(),
            vinho.getNome(),
            vinho.getDescricao(),
            vinho.getImagem(),
            vinho.getTeorAlcoolico(),
            vinho.getTeorAlcoolicoFormatado(),
            vinho.getVolume(),
            vinho.getVolumeFormatado(),
            vinho.getHarmonizacao(),
            vinho. getNotasDegustacao(),
            vinho.getServicoTemperatura(),
            vinho.getNomeCompleto(),
            
            // Relacionamentos (com null check)
            vinho.getUvas() != null ? vinho. getUvas().stream()
                .map(UvaDTOResponse::valueOf). toList() : null,
                
            vinho.getTipoVinho() != null ? TipoVinhoDTOResponse. valueOf(vinho.getTipoVinho()) : null,
            vinho.getPaisDeOrigem() != null ? PaisDTOResponse.valueOf(vinho. getPaisDeOrigem()) : null,
            vinho.getEstilo() != null ? EstiloDTOResponse.valueOf(vinho.getEstilo()) : null,
            vinho.getOcasiao() != null ? OcasiaoDTOResponse.valueOf(vinho.getOcasiao()) : null,
            vinho.getSafra() != null ? SafraDTOResponse. valueOf(vinho.getSafra()) : null,
            vinho.getMarca() != null ? MarcaDTOResponse.valueOf(vinho.getMarca()) : null
        );
    }
}