// src/main/java/br/unitins/topicos1/ewine/service/product/EstoqueServiceImpl.java
package br.unitins.topicos1.ewine.service.comercio;
import java.time.LocalDateTime;
import java. util.List;
import br.unitins.topicos1.ewine.dto.comercio.EstoqueDTOResponse;
import br.unitins.topicos1.ewine.dto.comercio.MovimentacaoEstoqueDTO;
import br.unitins.topicos1.ewine.model.comercio.Estoque;
import br.unitins.topicos1.ewine.repository.comercio.EstoqueRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject. Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EstoqueServiceImpl implements EstoqueService {

    @Inject
    EstoqueRepository estoqueRepository;

    @Override
    public EstoqueDTOResponse findById(Long id) {
        return estoqueRepository.findByIdOptional(id)
                .map(EstoqueDTOResponse::valueOf)
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado.  ID: " + id));
    }

    @Override
    public EstoqueDTOResponse findByProdutoId(Long idProduto) {
        Estoque estoque = estoqueRepository.findByProdutoId(idProduto);
        if (estoque == null) {
            throw new NotFoundException("Estoque não encontrado para produto ID: " + idProduto);
        }
        return EstoqueDTOResponse.valueOf(estoque);
    }

    @Override
    public List<EstoqueDTOResponse> findAll() {
        return estoqueRepository.listAll()
                .stream()
                . map(EstoqueDTOResponse::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public EstoqueDTOResponse adicionarEstoque(Long idProduto, MovimentacaoEstoqueDTO movimentacao) {
        validarMovimentacao(movimentacao);
        
        Estoque estoque = buscarEstoquePorProduto(idProduto);
        
        Integer novaQuantidade = estoque. getQuantidade() + movimentacao.quantidade();
        estoque.setQuantidade(novaQuantidade);
        estoque.setUltimaMovimentacao(LocalDateTime.now());
        
        return EstoqueDTOResponse.valueOf(estoque);
    }

    @Override
    @Transactional
    public EstoqueDTOResponse removerEstoque(Long idProduto, MovimentacaoEstoqueDTO movimentacao) {
        validarMovimentacao(movimentacao);
        
        Estoque estoque = buscarEstoquePorProduto(idProduto);
        
        if (estoque.getQuantidade() < movimentacao.quantidade()) {
            throw new IllegalStateException(
                "Estoque insuficiente.  Disponível: " + estoque.getQuantidade() + 
                ", Solicitado: " + movimentacao.quantidade()
            );
        }
        
        Integer novaQuantidade = estoque.getQuantidade() - movimentacao.quantidade();
        estoque.setQuantidade(novaQuantidade);
        estoque.setUltimaMovimentacao(LocalDateTime. now());
        
        return EstoqueDTOResponse.valueOf(estoque);
    }

    @Override
    @Transactional
    public EstoqueDTOResponse ajustarEstoque(Long idProduto, Integer novaQuantidade, String motivo) {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        
        Estoque estoque = buscarEstoquePorProduto(idProduto);
        estoque.setQuantidade(novaQuantidade);
        estoque. setUltimaMovimentacao(LocalDateTime.now());
        estoque.setObservacoes("Ajuste: " + (motivo != null ? motivo : "Sem motivo informado"));
        
        return EstoqueDTOResponse.valueOf(estoque);
    }

    @Override
    public List<EstoqueDTOResponse> findEstoqueBaixo() {
        return estoqueRepository.findEstoqueBaixo()
                .stream()
                . map(EstoqueDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<EstoqueDTOResponse> findEstoqueAlto() {
        return estoqueRepository.findEstoqueAlto()
                .stream()
                .map(EstoqueDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<EstoqueDTOResponse> findEsgotados() {
        return estoqueRepository.findEsgotados()
                .stream()
                .map(EstoqueDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<EstoqueDTOResponse> findByQuantidadeMenorQue(Integer quantidade) {
        return estoqueRepository.findByQuantidadeMenorQue(quantidade)
                .stream()
                .map(EstoqueDTOResponse::valueOf)
                .toList();
    }

    @Override
    public boolean temEstoqueDisponivel(Long idProduto, Integer quantidadeDesejada) {
        try {
            Estoque estoque = buscarEstoquePorProduto(idProduto);
            return estoque.getQuantidade() >= quantidadeDesejada;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isEstoqueBaixo(Long idProduto) {
        try {
            Estoque estoque = buscarEstoquePorProduto(idProduto);
            return estoque.isEstoqueBaixo();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public EstoqueDTOResponse atualizarLimites(Long idProduto, Integer estoqueMinimo, Integer estoqueMaximo) {
        if (estoqueMinimo <= 0 || estoqueMaximo <= estoqueMinimo) {
            throw new IllegalArgumentException("Limites de estoque inválidos");
        }
        
        Estoque estoque = buscarEstoquePorProduto(idProduto);
        estoque.setEstoqueMinimo(estoqueMinimo);
        estoque.setEstoqueMaximo(estoqueMaximo);
        
        return EstoqueDTOResponse.valueOf(estoque);
    }

    // Métodos auxiliares
    private Estoque buscarEstoquePorProduto(Long idProduto) {
        Estoque estoque = estoqueRepository.findByProdutoId(idProduto);
        if (estoque == null) {
            throw new NotFoundException("Estoque não encontrado para produto ID: " + idProduto);
        }
        return estoque;
    }
    
    private void validarMovimentacao(MovimentacaoEstoqueDTO movimentacao) {
        if (movimentacao == null || movimentacao.quantidade() <= 0) {
            throw new IllegalArgumentException("Movimentação inválida");
        }
    }
}