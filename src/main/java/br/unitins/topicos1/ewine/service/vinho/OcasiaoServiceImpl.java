package br.unitins.topicos1.ewine.service.vinho;



import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTO;
import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTOResponse;
import br.unitins.topicos1.ewine.model.wineentities.Ocasiao;
import br.unitins.topicos1.ewine.repository.winerepository.OcasiaoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class OcasiaoServiceImpl implements OcasiaoService {
    
    @Inject
    OcasiaoRepository ocasiaoRepository;

    @Override
    @Transactional
    public OcasiaoDTOResponse create(OcasiaoDTO ocasiaoDTO) {
        Ocasiao novaOcasiao = new Ocasiao();
        novaOcasiao.setNome(ocasiaoDTO.nome());
        
        ocasiaoRepository.persist(novaOcasiao);
        return OcasiaoDTOResponse.valueOf(novaOcasiao);
    }

    @Override
    @Transactional
    public OcasiaoDTOResponse update(Long id, OcasiaoDTO ocasiaoDTO) {
        Ocasiao ocasiao = ocasiaoRepository.findById(id);
        if (ocasiao == null) {
            throw new NotFoundException("Ocasião não encontrada para atualização. ID: " + id);
        }

        ocasiao.setNome(ocasiaoDTO.nome());
        
        return OcasiaoDTOResponse.valueOf(ocasiao);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = ocasiaoRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Ocasião não encontrada para exclusão. ID: " + id);
        }
    }

    @Override
    public OcasiaoDTOResponse findById(Long id) {
        Ocasiao ocasiao = ocasiaoRepository.findById(id);
        if (ocasiao == null) {
            throw new NotFoundException("Ocasião não encontrada com ID: " + id);
        }
        return OcasiaoDTOResponse.valueOf(ocasiao);
    }

    @Override
    public List<OcasiaoDTOResponse> findAll() {
        return ocasiaoRepository.listAll().stream()
                .map(OcasiaoDTOResponse::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<OcasiaoDTOResponse> findByNome(String nome) {
        return ocasiaoRepository.findByNome(nome).stream()
                .map(OcasiaoDTOResponse::valueOf)
                .collect(Collectors.toList());
    }
}