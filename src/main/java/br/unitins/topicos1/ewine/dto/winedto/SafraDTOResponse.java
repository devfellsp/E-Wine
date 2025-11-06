package br.unitins.topicos1.ewine.dto.winedto;
import br.unitins.topicos1.ewine.model.wineentities.Safra;

public record SafraDTOResponse(
    Long id, // ESSENCIAL: Garante que o Swagger liste este DTO.
    Integer ano
) {
    public static SafraDTOResponse valueOf (Safra safra) {
        return new SafraDTOResponse(
            safra.getId(),
            safra.getAno()
        );
    }
}