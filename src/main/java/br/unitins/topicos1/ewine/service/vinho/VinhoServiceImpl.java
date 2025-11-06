package br.unitins.topicos1.ewine.service.vinho;

import java.util.List;

import br.unitins.topicos1.ewine.dto.winedto.VinhoDTO;
import br.unitins.topicos1.ewine.dto.winedto.VinhoDTOResponse;
import br.unitins.topicos1.ewine.model.locationentities.Pais;
import br.unitins.topicos1.ewine.model.others.Marca;
import br.unitins.topicos1.ewine.model.wineentities.TipoVinho;
import br.unitins.topicos1.ewine.model.wineentities.Vinho;
import br.unitins.topicos1.ewine.repository.locationrepository.PaisRepository;
import br.unitins.topicos1.ewine.repository.othersrepository.MarcaRepository;
import br.unitins.topicos1.ewine.repository.winerepository.TipoVinhoRepository;
import br.unitins.topicos1.ewine.repository.winerepository.VinhoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class VinhoServiceImpl implements VinhoService {

    @Inject
    VinhoRepository vinhoRepository;

    @Inject
    MarcaRepository marcaRepository;

    @Inject
    TipoVinhoRepository tipoVinhoRepository;

    @Inject
    PaisRepository paisRepository;

    // Placeholders para Uva, Estilo, Safra, Ocasiao
    // @Inject EstiloRepository estiloRepository;
    // @Inject SafraRepository safraRepository;
    // @Inject OcasiaoRepository ocasiaoRepository;
    // @Inject UvaRepository uvaRepository;

    @Override
    @Transactional
    public VinhoDTOResponse create(VinhoDTO dto) {
        validateVinhoDTO(dto);

        Vinho novoVinho = new Vinho();
        convertToEntity(dto, novoVinho);

        vinhoRepository.persist(novoVinho);
        return VinhoDTOResponse.valueOf(novoVinho);
    }

    @Override
    @Transactional
    public VinhoDTOResponse update(Long id, VinhoDTO dto) {
        validateVinhoDTO(dto);

        Vinho vinho = vinhoRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Vinho não encontrado para atualização. ID: " + id));

        convertToEntity(dto, vinho);
        return VinhoDTOResponse.valueOf(vinho);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boolean deleted = vinhoRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Vinho não encontrado para exclusão. ID: " + id);
        }
    }

    @Override
    public VinhoDTOResponse findById(Long id) {
        return vinhoRepository.findByIdOptional(id)
                .map(VinhoDTOResponse::valueOf)
                .orElseThrow(() -> new NotFoundException("Vinho não encontrado com ID: " + id));
    }

    @Override
    public List<VinhoDTOResponse> findAll() {
        return vinhoRepository.listAll().stream()
                .map(VinhoDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<VinhoDTOResponse> findByNome(String nome) {
        return vinhoRepository.findByNome(nome).stream()
                .map(VinhoDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<VinhoDTOResponse> findByMarcaId(Long idMarca) {
        return vinhoRepository.findByMarca(idMarca).stream()
                .map(VinhoDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<VinhoDTOResponse> findByTipoVinhoId(Long idTipoVinho) {
        return vinhoRepository.findByTipoVinho(idTipoVinho).stream()
                .map(VinhoDTOResponse::valueOf)
                .toList();
    }

    @Override
    public List<VinhoDTOResponse> findByPaisId(Long idPais) {
        return vinhoRepository.findByPais(idPais).stream()
                .map(VinhoDTOResponse::valueOf)
                .toList();
    }

    // ------------------------------
    // Métodos auxiliares
    // ------------------------------

    private void validateVinhoDTO(VinhoDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO não pode ser nulo.");
        if (dto.nome() == null || dto.nome().isBlank())
            throw new IllegalArgumentException("Nome do vinho é obrigatório.");
        if (dto.preco() == null || dto.preco() < 0)
            throw new IllegalArgumentException("Preço do vinho inválido.");
        if (dto.idMarca() == null) throw new IllegalArgumentException("ID da Marca é obrigatório.");
        if (dto.idTipoVinho() == null) throw new IllegalArgumentException("ID do Tipo de Vinho é obrigatório.");
        if (dto.idPais() == null) throw new IllegalArgumentException("ID do País de Origem é obrigatório.");
        // Pode adicionar validação de SKU, estoque, imagem, etc.
    }

    private void convertToEntity(VinhoDTO dto, Vinho vinho) {
        // Atributos simples
        vinho.setNome(dto.nome());
        vinho.setDescricao(dto.descricao());
        vinho.setPreco(dto.preco());
        vinho.setQuantEstoque(dto.quantEstoque());
        vinho.setSku(dto.sku());
        vinho.setImagem(dto.imagem());

        // Relacionamentos
        vinho.setMarca(findMarca(dto.idMarca()));
        vinho.setTipoVinho(findTipoVinho(dto.idTipoVinho()));
        vinho.setPaisDeOrigem(findPais(dto.idPais()));

        // *ADICIONAR: lógica para Estilo, Safra, Ocasiao, Uvas*
    }

    private Marca findMarca(Long id) {
        return marcaRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Marca não encontrada. ID: " + id));
    }

    private TipoVinho findTipoVinho(Long id) {
        return tipoVinhoRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Tipo de Vinho não encontrado. ID: " + id));
    }

    private Pais findPais(Long id) {
        return paisRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("País de Origem não encontrado. ID: " + id));
    }
}
