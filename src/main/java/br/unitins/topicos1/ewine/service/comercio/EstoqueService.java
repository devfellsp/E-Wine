// src/main/java/br/unitins/topicos1/ewine/service/product/EstoqueService.java
package br.unitins.topicos1.ewine.service.comercio;

import java.util.List;

import br.unitins.topicos1.ewine.dto.comercio.EstoqueDTO;
import br.unitins.topicos1.ewine.dto.comercio.EstoqueDTOResponse;
import br.unitins.topicos1.ewine.dto.comercio.MovimentacaoEstoqueDTO;

public interface EstoqueService {
    
    // Consultas básicas
    EstoqueDTOResponse findById(Long id);
    EstoqueDTOResponse findByProdutoId(Long idProduto);
    List<EstoqueDTOResponse> findAll();
    
    // Movimentações
    EstoqueDTOResponse adicionarEstoque(Long idProduto, MovimentacaoEstoqueDTO movimentacao);
    EstoqueDTOResponse removerEstoque(Long idProduto, MovimentacaoEstoqueDTO movimentacao);
    EstoqueDTOResponse ajustarEstoque(Long idProduto, Integer novaQuantidade, String motivo);
    
    // Consultas especiais
    List<EstoqueDTOResponse> findEstoqueBaixo();
    List<EstoqueDTOResponse> findEstoqueAlto();
    List<EstoqueDTOResponse> findEsgotados();
    List<EstoqueDTOResponse> findByQuantidadeMenorQue(Integer quantidade);
    
    // Validações
    boolean temEstoqueDisponivel(Long idProduto, Integer quantidadeDesejada);
    boolean isEstoqueBaixo(Long idProduto);
    
    // Atualização de limites
    EstoqueDTOResponse atualizarLimites(Long idProduto, Integer estoqueMinimo, Integer estoqueMaximo);
}