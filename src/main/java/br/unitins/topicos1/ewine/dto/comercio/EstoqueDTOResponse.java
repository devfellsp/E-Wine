// src/main/java/br/unitins/topicos1/ewine/dto/productdto/EstoqueDTORespo
package br.unitins.topicos1.ewine.dto.comercio;

import java.time.LocalDateTime;

import br.unitins.topicos1.ewine.model.comercio.Estoque;
public record EstoqueDTOResponse(
    Long id,
    Long idProduto,
    String skuProduto,
    Integer quantidade,
    Integer estoqueMinimo,
    Integer estoqueMaximo,
    LocalDateTime ultimaMovimentacao,
    String observacoes,
    boolean estoqueBaixo,
    boolean estoqueAlto,
    String statusEstoque // "NORMAL", "BAIXO", "ALTO", "ESGOTADO"
) {
    
    public static EstoqueDTOResponse valueOf(Estoque estoque) {
        String status = "NORMAL";
        if (estoque.getQuantidade() == 0) {
            status = "ESGOTADO";
        } else if (estoque. isEstoqueBaixo()) {
            status = "BAIXO";
        } else if (estoque.isEstoqueAlto()) {
            status = "ALTO";
        }
        
        return new EstoqueDTOResponse(
            estoque.getId(),
            estoque.getProduto().getId(),
            estoque.getProduto().getSku(),
            estoque.getQuantidade(),
            estoque. getEstoqueMinimo(),
            estoque.getEstoqueMaximo(),
            estoque. getUltimaMovimentacao(),
            estoque.getObservacoes(),
            estoque.isEstoqueBaixo(),
            estoque.isEstoqueAlto(),
            status
        );
    }
}