package br.unitins.topicos1.ewine.dto;

public record AuthDTOResponse(
    String token,
    String tokenType,
    Long expiresAt, // epoch milliseconds (nullable se não informado)
    String login,
    String perfil
) {

    public static AuthDTOResponse of(String token, String tokenType, Long expiresAt, String login, String perfil) {
        return new AuthDTOResponse(token, tokenType, expiresAt, login, perfil);
    }

}