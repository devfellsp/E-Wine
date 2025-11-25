package br.unitins.topicos1.ewine.dto.perfil;
import br.unitins.topicos1.ewine.model.perfil.Perfil;

public record UsuarioDTOResponse(
    Long id,
    String nome,
    String login,
    Perfil perfil
) {

    public static UsuarioDTOResponse valueOf(br.unitins.topicos1.ewine.model.perfil.Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDTOResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getLogin(),
            usuario.getPerfil()
        );
    }
}
