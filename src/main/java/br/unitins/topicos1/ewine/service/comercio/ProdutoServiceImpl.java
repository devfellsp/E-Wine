// src/main/java/br/unitins/topicos1/ewine/service/product/ProdutoServiceImpl.java
package br.unitins.topicos1.ewine.service.comercio;
import java.time.LocalDateTime;
import java. util.List;

import br.unitins.topicos1.ewine.dto.comercio.ProdutoDTO;
import br.unitins.topicos1.ewine.dto.comercio.ProdutoResponseDTO;
import br.unitins.topicos1.ewine.dto.comercio.ProdutoUpdateDTO;
import br.unitins.topicos1.ewine.model.comercio.Estoque;
import br.unitins.topicos1.ewine.model.comercio.Produto;
import br.unitins.topicos1.ewine.model.wineentities.Vinho;
import br.unitins.topicos1.ewine.repository.comercio.ProdutoRepository;

import br.unitins. topicos1.ewine.repository.winerepository.VinhoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject. Inject;
import jakarta.transaction. Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ProdutoServiceImpl implements ProdutoService {

    @Inject
    ProdutoRepository produtoRepository;
    
    @Inject
    VinhoRepository vinhoRepository;

    @Override
    @Transactional
    public ProdutoResponseDTO create(ProdutoDTO dto) {
        validarProdutoDTO(dto);
        
        // Verificar se SKU já existe
        if (produtoRepository.existsBySku(dto. sku())) {
            throw new BadRequestException("SKU já existe: " + dto.sku());
        }
        
        // Buscar vinho
        Vinho vinho = vinhoRepository.findByIdOptional(dto.idVinho())
                .orElseThrow(() -> new NotFoundException("Vinho não encontrado.  ID: " + dto.idVinho()));
        
        // Criar produto
        Produto produto = new Produto();
        produto.setSku(dto.sku());
        produto. setPreco(dto.preco());
        produto.setAtivo(dto.ativo());
        produto. setDescricaoComercial(dto.descricaoComercial());
        produto.setVinho(vinho);
        produto. setDataCriacao(LocalDateTime.now());
        
        // Criar estoque associado
        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQuantidade(dto. quantidadeInicial());
        estoque.setEstoqueMinimo(dto.estoqueMinimo());
        estoque.setEstoqueMaximo(dto.estoqueMaximo());
        estoque.setObservacoes(dto.observacoesEstoque());
        estoque.setUltimaMovimentacao(LocalDateTime.now());
        
        produto.setEstoque(estoque);
        
        produtoRepository.persist(produto);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO update(Long id, ProdutoUpdateDTO dto) {
        validarProdutoUpdateDTO(dto);
        
        Produto produto = produtoRepository. findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado. ID: " + id));
        
        // Verificar se SKU já existe em outro produto
        if (! produto.getSku().equals(dto. sku()) && produtoRepository. existsBySkuAndIdNot(dto.sku(), id)) {
            throw new BadRequestException("SKU já existe em outro produto: " + dto.sku());
        }
        
        // Atualizar dados
        produto.setSku(dto.sku());
        if (dto.preco() != null) {
            produto.setPreco(dto.preco());
        }
        if (dto.ativo() != null) {
            produto.setAtivo(dto.ativo());
        }
        produto.setDescricaoComercial(dto.descricaoComercial());
        
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = produtoRepository.deleteById(id);
        if (! deleted) {
            throw new NotFoundException("Produto não encontrado para exclusão.  ID: " + id);
        }
    }

    @Override
    public ProdutoResponseDTO findById(Long id) {
        return produtoRepository.findByIdOptional(id)
                .map(ProdutoResponseDTO::valueOf)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado. ID: " + id));
    }

    @Override
    public List<ProdutoResponseDTO> findAll() {
        return produtoRepository.listAll()
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public ProdutoResponseDTO findBySku(String sku) {
        return produtoRepository.findBySku(sku)
                . map(ProdutoResponseDTO::valueOf)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado. SKU: " + sku));
    }

    @Override
    public List<ProdutoResponseDTO> findAtivos() {
        return produtoRepository.findAtivos()
                .stream()
                . map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findInativos() {
        return produtoRepository.findInativos()
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findDisponiveis() {
        return produtoRepository.findDisponiveis()
                .stream()
                . map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findEsgotados() {
        return produtoRepository. findEsgotados()
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findByVinhoId(Long idVinho) {
        return produtoRepository.findByVinhoId(idVinho)
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findByVinhoNome(String nomeVinho) {
        return produtoRepository.findByVinhoNome(nomeVinho)
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findByMarcaId(Long idMarca) {
        return produtoRepository.findByMarcaId(idMarca)
                . stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findByTipoVinhoId(Long idTipoVinho) {
        return produtoRepository.findByTipoVinhoId(idTipoVinho)
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> findByPrecoRange(Double precoMin, Double precoMax) {
        if (precoMin < 0 || precoMax < 0 || precoMin > precoMax) {
            throw new BadRequestException("Faixa de preço inválida");
        }
        
        return produtoRepository.findByPrecoRange(precoMin, precoMax)
                .stream()
                .map(ProdutoResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public ProdutoResponseDTO ativar(Long id) {
        Produto produto = produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado. ID: " + id));
        
        produto.setAtivo(true);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO desativar(Long id) {
        Produto produto = produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado. ID: " + id));
        
        produto.setAtivo(false);
        return ProdutoResponseDTO.valueOf(produto);
    }

    @Override
    public boolean existsBySku(String sku) {
        return produtoRepository.existsBySku(sku);
    }

    @Override
    public boolean isDisponivel(Long id) {
        try {
            Produto produto = produtoRepository.findByIdOptional(id). orElse(null);
            return produto != null && 
                   produto.getAtivo() && 
                   produto.getEstoque() != null && 
                   produto.getEstoque().getQuantidade() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos auxiliares
    private void validarProdutoDTO(ProdutoDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Dados do produto são obrigatórios");
        }
        if (dto.quantidadeInicial() < 0) {
            throw new BadRequestException("Quantidade inicial não pode ser negativa");
        }
        if (dto.estoqueMinimo() <= 0) {
            throw new BadRequestException("Estoque mínimo deve ser maior que zero");
        }
        if (dto.estoqueMaximo() <= dto.estoqueMinimo()) {
            throw new BadRequestException("Estoque máximo deve ser maior que o mínimo");
        }
    }
    
    private void validarProdutoUpdateDTO(ProdutoUpdateDTO dto) {
        if (dto == null) {
            throw new BadRequestException("Dados do produto são obrigatórios");
        }
    }
}