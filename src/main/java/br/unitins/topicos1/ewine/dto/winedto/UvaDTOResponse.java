package br.unitins.topicos1.ewine.dto.winedto;

import br.unitins.topicos1.ewine.model.wineentities.Uva;

public record UvaDTOResponse(
    String nome
    
) {
    public static UvaDTOResponse valueOf(Uva uva ){
        return new UvaDTOResponse(
            uva.getNome()
        );
    }
}