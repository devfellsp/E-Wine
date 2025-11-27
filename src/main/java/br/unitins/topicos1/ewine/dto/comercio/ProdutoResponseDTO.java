// src/main/java/br/unitins/topicos1/ewine/dto/productdto/Produto
package br.unitins.topicos1.ewine.dto.comercio;
import java.time.LocalDateTime;
import br.unitins.topicos1.ewine.model.comercio.Produto;
import br.unitins.topicos1.ewine.dto.winedto.VinhoDTOResponse;
import br.unitins.topicos1.ewine.dto.comercio.EstoqueDTOResponse;
import br.unitins.topicos1.ewine.model.comercio.Estoque;
public record ProdutoResponseDTO(
    Long id,
    String sku,
    Double preco,
    String precoFormatado, // Ex: "R$ 89,90"
    Boolean ativo,
    String statusProduto, // "ATIVO", "INATIVO"
    String descricaoComercial,
    LocalDateTime dataCriacao,
    
    // Vinho relacionado
    VinhoDTOResponse vinho,
    
    // Estoque relacionado
    EstoqueDTOResponse estoque,
    
    // Campos calculados
    String nomeCompleto, // Ex: "Cabernet Sauvignon 2020 - 750ml (SKU: CAB-2020-750)"
    boolean disponivel, // Se tem estoque e está ativo
    String statusDisponibilidade // "DISPONÍVEL", "ESGOTADO", "INATIVO"
    
) {
    
    public static ProdutoResponseDTO valueOf(Produto produto) {
        String precoFormatado = String. format("R$ %.2f", produto.getPreco()). replace(".", ",");
        String statusProduto = produto.getAtivo() ? "ATIVO" : "INATIVO";
        
        // Status de disponibilidade
        String statusDisponibilidade;
        boolean disponivel = false;
        
        if (! produto.getAtivo()) {
            statusDisponibilidade = "INATIVO";
        } else if (produto.getEstoque() == null || produto.getEstoque().getQuantidade() <= 0) {
            statusDisponibilidade = "ESGOTADO";
        } else {
            statusDisponibilidade = "DISPONÍVEL";
            disponivel = true;
        }
        
        // Nome completo do produto
        String nomeCompleto = produto.getVinho() != null ? 
            produto. getVinho().getNomeCompleto() + " (SKU: " + produto.getSku() + ")" : 
            "Produto " + produto.getSku();
        
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getSku(),
            produto.getPreco(),
            precoFormatado,
            produto.getAtivo(),
            statusProduto,
            produto.getDescricaoComercial(),
            produto.getDataCriacao(),
            
            // Vinho
            produto.getVinho() != null ?  VinhoDTOResponse.valueOf(produto.getVinho()) : null,
            
            // Estoque
            produto.getEstoque() != null ? EstoqueDTOResponse.valueOf(produto. getEstoque()) : null,
            
            // Campos calculados
            nomeCompleto,
            disponivel,
            statusDisponibilidade
        );
    }
}