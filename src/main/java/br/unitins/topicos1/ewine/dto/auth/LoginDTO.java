package br.unitins.topicos1.ewine.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank String login,
    @NotBlank String senha
) {}
