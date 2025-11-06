package br.unitins.topicos1.ewine.service.vinho;
import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.EstiloDTO;
import br.unitins.topicos1.ewine.dto.winedto.EstiloDTOResponse;

public interface EstiloService {
    
    EstiloDTOResponse create(EstiloDTO dto);
    
    EstiloDTOResponse update(Long id, EstiloDTO dto);
    
    void delete(Long id);
    
    EstiloDTOResponse findById(Long id);
    
    List<EstiloDTOResponse> findAll();
    
    List<EstiloDTOResponse> findByNome(String nome);
    }