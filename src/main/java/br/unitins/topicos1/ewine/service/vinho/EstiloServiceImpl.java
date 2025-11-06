package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.ewine.dto.winedto.EstiloDTO;
import br.unitins.topicos1.ewine.dto.winedto.EstiloDTOResponse;
import br.unitins.topicos1.ewine.model.wineentities.Estilo;
import br.unitins.topicos1.ewine.repository.winerepository.EstiloRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EstiloServiceImpl implements EstiloService {
    
    @Inject
    EstiloRepository estiloRepository;

    @Override
    @Transactional
    public EstiloDTOResponse create(EstiloDTO estiloDTO) {
        Estilo novoEstilo = new Estilo();
        novoEstilo.setNome(estiloDTO.nome());
        
        estiloRepository.persist(novoEstilo);
        return EstiloDTOResponse.valueOf(novoEstilo);
    }

    @Override
    @Transactional
    public EstiloDTOResponse update(Long id, EstiloDTO estiloDTO) {
        Estilo estilo = estiloRepository.findById(id);
        if (estilo == null) {
            throw new NotFoundException("Estilo não encontrado para atualização. ID: " + id);
        }

        estilo.setNome(estiloDTO.nome());
        
        return EstiloDTOResponse.valueOf(estilo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = estiloRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Estilo não encontrado para exclusão. ID: " + id);
        }
    }

    @Override
    public EstiloDTOResponse findById(Long id) {
        Estilo estilo = estiloRepository.findById(id);
        if (estilo == null) {
            throw new NotFoundException("Estilo não encontrado com ID: " + id);
        }
        return EstiloDTOResponse.valueOf(estilo);
    }

    @Override
    public List<EstiloDTOResponse> findAll() {
        return estiloRepository.listAll().stream()
                .map(EstiloDTOResponse::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<EstiloDTOResponse> findByNome(String nome) {
        return estiloRepository.findByNome(nome).stream()
                .map(EstiloDTOResponse::valueOf)
                .collect(Collectors.toList());
    }
}