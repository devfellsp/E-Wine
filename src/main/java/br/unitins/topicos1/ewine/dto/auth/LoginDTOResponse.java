package br.unitins.topicos1.ewine.dto.auth;



import br.unitins.topicos1. ewine.model.perfil.Perfil;

public record LoginDTOResponse(
    String token,
    UsuarioLogado usuario,
    String expiresIn
) {
    
    public static record UsuarioLogado(
        Long id,
        String nome,
        String login,
        Perfil perfil
    ) {}
    
    public static LoginDTOResponse valueOf(String token, br.unitins.topicos1. ewine.model.perfil. Usuario usuario) {
        return new LoginDTOResponse(
            token,
            new UsuarioLogado(usuario.getId(), usuario.getNome(), usuario.getLogin(), usuario.getPerfil()),
            "24h"
        );
    }
}
