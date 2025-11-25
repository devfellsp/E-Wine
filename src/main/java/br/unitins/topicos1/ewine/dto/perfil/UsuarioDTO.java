package br.unitins.topicos1.ewine.dto.perfil;

    

public record UsuarioDTO(
    String nome,
    String login,
    String senha,
    String perfil // exemplo: "ADM" ou "USER" (case-insensitive)
) {}

