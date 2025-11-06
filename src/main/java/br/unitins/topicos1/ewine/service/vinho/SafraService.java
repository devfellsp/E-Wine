package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.SafraDTO;
import br.unitins.topicos1.ewine.dto.winedto.SafraDTOResponse;



public interface SafraService {
  
    SafraDTOResponse create(SafraDTO dto);
    
    SafraDTOResponse update(Long id, SafraDTO dto);
    
    void delete(Long id);
    
    SafraDTOResponse findById(Long id);
    
    List<SafraDTOResponse> findAll();
    
    List<SafraDTOResponse> findByAno(String ano);

    }