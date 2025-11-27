// src/main/java/br/unitins/topicos1/ewine/service/product/ProdutoService.java
package br.unitins.topicos1.ewine.service.comercio;
import java.util.List;

import br.unitins.topicos1.ewine.dto.comercio.ProdutoDTO;
import br.unitins.topicos1.ewine.dto.comercio.ProdutoResponseDTO;
import br.unitins.topicos1.ewine.dto.comercio.ProdutoUpdateDTO;

public interface ProdutoService {
    
    // CRUD básico
    ProdutoResponseDTO create(ProdutoDTO dto);
    ProdutoResponseDTO update(Long id, ProdutoUpdateDTO dto);
    void delete(Long id);
    ProdutoResponseDTO findById(Long id);
    List<ProdutoResponseDTO> findAll();
    
    // Busca por SKU
    ProdutoResponseDTO findBySku(String sku);
    
    // Filtros de status
    List<ProdutoResponseDTO> findAtivos();
    List<ProdutoResponseDTO> findInativos();
    List<ProdutoResponseDTO> findDisponiveis();
    List<ProdutoResponseDTO> findEsgotados();
    
    // Busca por vinho
    List<ProdutoResponseDTO> findByVinhoId(Long idVinho);
    List<ProdutoResponseDTO> findByVinhoNome(String nomeVinho);
    List<ProdutoResponseDTO> findByMarcaId(Long idMarca);
    List<ProdutoResponseDTO> findByTipoVinhoId(Long idTipoVinho);
    
    // Busca por preço
    List<ProdutoResponseDTO> findByPrecoRange(Double precoMin, Double precoMax);
    
    // Gestão de status
    ProdutoResponseDTO ativar(Long id);
    ProdutoResponseDTO desativar(Long id);
    
    // Validações
    boolean existsBySku(String sku);
    boolean isDisponivel(Long id);
}