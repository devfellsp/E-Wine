package br.unitins.topicos1.ewine;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

/**
 * Declaração do SecurityScheme para o OpenAPI/Swagger UI.
 * Isso faz o botão "Authorize" aparecer como esquema Bearer (JWT).
 */
@ApplicationScoped
@SecurityScheme(
    securitySchemeName = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig { }