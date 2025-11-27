package br.unitins.topicos1.ewine.service.auth;

import br.unitins.topicos1.ewine.model. perfil.Usuario;

public interface JWTService {
    String generateToken(Usuario usuario);
    
}