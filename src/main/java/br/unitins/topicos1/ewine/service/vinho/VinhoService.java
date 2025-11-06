package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.VinhoDTO;
import br.unitins.topicos1.ewine.dto.winedto.VinhoDTOResponse;


public interface VinhoService {
   
    VinhoDTOResponse create(VinhoDTO dto);
    
    VinhoDTOResponse update(Long id, VinhoDTO dto);
    
    void delete(Long id);
    
    VinhoDTOResponse findById(Long id);
    
    List<VinhoDTOResponse> findAll();
    
    List<VinhoDTOResponse> findByNome(String nome);
    
    List<VinhoDTOResponse> findByMarcaId(Long idMarca);
    
    List<VinhoDTOResponse> findByTipoVinhoId(Long idTipoVinho);
    
    List<VinhoDTOResponse> findByPaisId(Long idPais);
}
