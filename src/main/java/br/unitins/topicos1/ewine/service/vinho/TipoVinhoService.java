package br.unitins.topicos1.ewine.service.vinho; // Ajuste o pacote se necessário

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTO;
import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTOResponse;



public interface TipoVinhoService {
    
    TipoVinhoDTOResponse create(TipoVinhoDTO dto);
    
    TipoVinhoDTOResponse update(Long id, TipoVinhoDTO dto);
    
    void delete(Long id);
    
    TipoVinhoDTOResponse findById(Long id);
    
    List<TipoVinhoDTOResponse> findAll();
    
    List<TipoVinhoDTOResponse> findByNome(String nome);
}