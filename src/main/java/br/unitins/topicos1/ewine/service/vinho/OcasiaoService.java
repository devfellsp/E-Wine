package br.unitins.topicos1.ewine.service.vinho;


import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTO;
import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTOResponse;

public interface OcasiaoService {
    
    OcasiaoDTOResponse create(OcasiaoDTO dto);
    
    OcasiaoDTOResponse update(Long id, OcasiaoDTO dto);
    
    void delete(Long id);
    
    OcasiaoDTOResponse findById(Long id);
    
    List<OcasiaoDTOResponse> findAll();
    
    List<OcasiaoDTOResponse> findByNome(String nome);
}