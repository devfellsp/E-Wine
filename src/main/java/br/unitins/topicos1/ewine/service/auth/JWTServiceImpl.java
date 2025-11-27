// src/main/java/br/unitins/topicos1/ewine/service/auth/JwtServiceImpl.java
package br.unitins.topicos1.ewine.service.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import br.unitins.topicos1. ewine.model.perfil.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JWTServiceImpl implements JWTService {

    @Override
    public String generateToken(Usuario usuario) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plus(24, ChronoUnit.HOURS);
            
            return Jwt.issuer("ewine-api")
                    . upn(usuario.getLogin())
                    .subject(usuario.getId().toString())
                    .claim("login", usuario.getLogin())
                    .claim("nome", usuario.getNome())
                    .claim("perfil", usuario.getPerfil().name())
                    .groups(usuario.getPerfil().name())
                    .issuedAt(now)
                    .expiresAt(expiry)
                    .signWithSecret("minha-chave-secreta-super-longa-para-jwt-funcionar-corretamente");
                    
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar JWT: " + e.getMessage(), e);
        }
    }
}