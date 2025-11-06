package br.unitins.topicos1.ewine.dto.winedto;

// --- Imports de Entidades/Models que serão MANTIDOS (para o mapeamento no valueOf)
import br.unitins.topicos1.ewine.model.wineentities.Vinho;
// IMPORTANTE: Mantenha apenas o import de Vinho, os demais (Marca, TipoVinho, etc.)
// devem ser substituídos no corpo do record pelos seus DTOs correspondentes.
import br.unitins.topicos1.ewine.dto.OthersDTO.MarcaDTOResponse;
import br.unitins.topicos1.ewine.dto.locationdto.PaisDTOResponse;
import java.util.List;

public record VinhoDTOResponse(
    Long id,
    String nome,
    Double preco,
    Integer quantEstoque,
    String descricao,
    String imagem,
    String sku,
    
    // CORREÇÕES APLICADAS AQUI: Substituindo Models por DTOs
    MarcaDTOResponse marca,
    TipoVinhoDTOResponse tipoVinho,
    List<UvaDTOResponse> uvas,
    PaisDTOResponse paisDeOrigem, 
    EstiloDTOResponse estilo,
    OcasiaoDTOResponse ocasiao,
    SafraDTOResponse safra
) {
    public static VinhoDTOResponse valueOf(Vinho vinho) {
        return new VinhoDTOResponse(
            vinho.getId(),
            vinho.getNome(),
            vinho.getPreco(),
            vinho.getQuantEstoque(),
            vinho.getDescricao(),
            vinho.getImagem(),
            vinho.getSku(),
            
            // CORREÇÕES APLICADAS AQUI: Mapeando as Entidades para DTOs de Resposta
            MarcaDTOResponse.valueOf(vinho.getMarca()),
            TipoVinhoDTOResponse.valueOf(vinho.getTipoVinho()),
            vinho.getUvas().stream().map(UvaDTOResponse::valueOf).toList(),
            PaisDTOResponse.valueOf(vinho.getPaisDeOrigem()),
            EstiloDTOResponse.valueOf(vinho.getEstilo()),
            OcasiaoDTOResponse.valueOf(vinho.getOcasiao()),
            SafraDTOResponse.valueOf(vinho.getSafra())
        );
    }
}