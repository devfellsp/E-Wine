package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.UvaDTO;
import br.unitins.topicos1.ewine.dto.winedto.UvaDTOResponse;
import br.unitins.topicos1.ewine.model.wineentities.Uva;
import br.unitins.topicos1.ewine.repository.winerepository.UvaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UvaServiceImpl implements UvaService {

    @Inject
    UvaRepository uvaRepository;

    @Override
    @Transactional
    public UvaDTOResponse create(UvaDTO uvaDTO) {
        validateUvaDTO(uvaDTO);

        Uva novaUva = new Uva();
        novaUva.setNome(uvaDTO.nome());

        uvaRepository.persist(novaUva);
        return mapToDTO(novaUva);
    }

    @Override
    @Transactional
    public UvaDTOResponse update(Long id, UvaDTO uvaDTO) {
        validateUvaDTO(uvaDTO);

        Uva uva = uvaRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Uva não encontrada para atualização. ID: " + id));

        uva.setNome(uvaDTO.nome());
        // No Panache, alterações são automáticas

        return mapToDTO(uva);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = uvaRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Uva não encontrada para exclusão. ID: " + id);
        }
    }

    @Override
    public UvaDTOResponse findById(Long id) {
        return uvaRepository.findByIdOptional(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new NotFoundException("Uva não encontrada com ID: " + id));
    }

    @Override
    public List<UvaDTOResponse> findAll() {
        return uvaRepository.listAll()
                .stream()
                .map(this::mapToDTO)
                .toList(); // Java 16+
    }

    @Override
    public List<UvaDTOResponse> findByNome(String nome) {
        return uvaRepository.findByNome(nome)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ------------------------------
    // Métodos auxiliares
    // ------------------------------

    private void validateUvaDTO(UvaDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO não pode ser nulo.");
        if (dto.nome() == null || dto.nome().isBlank())
            throw new IllegalArgumentException("Nome da uva é obrigatório.");
    }

    private UvaDTOResponse mapToDTO(Uva uva) {
        return UvaDTOResponse.valueOf(uva);
    }
}