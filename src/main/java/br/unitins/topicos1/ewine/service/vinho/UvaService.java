package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.UvaDTO;
import br.unitins.topicos1.ewine.dto.winedto.UvaDTOResponse;

public interface UvaService {

    UvaDTOResponse create(UvaDTO dto);
    
    UvaDTOResponse update(Long id, UvaDTO dto);
    
    void delete(Long id);
    
    UvaDTOResponse findById(Long id);
    
    List<UvaDTOResponse> findAll();
    
    List<UvaDTOResponse> findByNome(String nome);
} 