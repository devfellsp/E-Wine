package br.unitins.topicos1.ewine.service.vinho; // Ajuste o pacote se necessário

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTO;
import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTOResponse;
import br.unitins.topicos1.ewine.model.wineentities.TipoVinho;
import br.unitins.topicos1.ewine.repository.winerepository.TipoVinhoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;





@ApplicationScoped
public class TipoVinhoServiceImpl implements TipoVinhoService {

    @Inject
    TipoVinhoRepository tipoVinhoRepository;

    @Override
    @Transactional
    public TipoVinhoDTOResponse create(TipoVinhoDTO tipoVinhoDTO) {
        validateTipoVinhoDTO(tipoVinhoDTO);

        TipoVinho novoTipoVinho = new TipoVinho();
        novoTipoVinho.setNome(tipoVinhoDTO.nome());

        tipoVinhoRepository.persist(novoTipoVinho);
        return mapToDTO(novoTipoVinho);
    }

    @Override
    @Transactional
    public TipoVinhoDTOResponse update(Long id, TipoVinhoDTO tipoVinhoDTO) {
        validateTipoVinhoDTO(tipoVinhoDTO);

        TipoVinho tipoVinho = tipoVinhoRepository.findById(id);
        if (tipoVinho == null) {
            throw new NotFoundException("Tipo de Vinho não encontrado para atualização. ID: " + id);
        }

        tipoVinho.setNome(tipoVinhoDTO.nome());
        // Não precisa de persist() no Panache; alterações são automáticas
        return mapToDTO(tipoVinho);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!tipoVinhoRepository.deleteById(id)) {
            throw new NotFoundException("Tipo de Vinho não encontrado para exclusão. ID: " + id);
        }
    }

    @Override
    public TipoVinhoDTOResponse findById(Long id) {
        return tipoVinhoRepository.findByIdOptional(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new NotFoundException("Tipo de Vinho não encontrado com ID: " + id));
    }

    @Override
    public List<TipoVinhoDTOResponse> findAll() {
        return tipoVinhoRepository.listAll()
                .stream()
                .map(this::mapToDTO)
                .toList(); // Java 16+, evita Collectors.toList()
    }

    @Override
    public List<TipoVinhoDTOResponse> findByNome(String nome) {
        return tipoVinhoRepository.findByNome(nome)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ------------------------------
    // Métodos auxiliares
    // ------------------------------

    private void validateTipoVinhoDTO(TipoVinhoDTO dto) {
        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("O nome do Tipo de Vinho não pode ser vazio.");
        }
    }

    private TipoVinhoDTOResponse mapToDTO(TipoVinho tipoVinho) {
        return TipoVinhoDTOResponse.valueOf(tipoVinho);
    }
}
