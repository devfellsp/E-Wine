package br.unitins.topicos1.ewine.service.auth;

import io.jsonwebtoken.Claims;

public interface JWTService {
    String generateToken(String login, String role);
    Claims validateToken(String token); // lança exceção se inválido

    /**
     * Retorna o timestamp de expiração calculado a partir do momento atual, em epoch milliseconds.
     * Útil para preencher o expiresAt no AuthResponseDTO.
     */
    long getExpirationEpochMillis();
}
