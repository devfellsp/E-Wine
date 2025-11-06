package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.SafraDTO;
import br.unitins.topicos1.ewine.dto.winedto.SafraDTOResponse;



import java.util.stream.Collectors;

import br.unitins.topicos1.ewine.model.wineentities.Safra;
import br.unitins.topicos1.ewine.repository.winerepository.SafraRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class SafraServiceImpl implements SafraService {
    
    @Inject
    SafraRepository safraRepository;

    @Override
    @Transactional
    public SafraDTOResponse create(SafraDTO safraDTO) {
        Safra novaSafra = new Safra();
        novaSafra.setAno(safraDTO.ano());
        
        safraRepository.persist(novaSafra);
        return SafraDTOResponse.valueOf(novaSafra);
    }

    @Override
    @Transactional
    public SafraDTOResponse update(Long id, SafraDTO safraDTO) {
        Safra safra = safraRepository.findById(id);
        if (safra == null) {
            throw new NotFoundException("Safra não encontrada para atualização. ID: " + id);
        }

        safra.setAno(safraDTO.ano());
        
        return SafraDTOResponse.valueOf(safra);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = safraRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Safra não encontrada para exclusão. ID: " + id);
        }
    }

    @Override
    public SafraDTOResponse findById(Long id) {
        Safra safra = safraRepository.findById(id);
        if (safra == null) {
            throw new NotFoundException("Safra não encontrada com ID: " + id);
        }
        return SafraDTOResponse.valueOf(safra);
    }

    @Override
    public List<SafraDTOResponse> findAll() {
        return safraRepository.listAll().stream()
                .map(SafraDTOResponse::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<SafraDTOResponse> findByAno(String ano) {
        return safraRepository.findByAnoLike(ano).stream()
                .map(SafraDTOResponse::valueOf)
                .collect(Collectors.toList());
    }
}