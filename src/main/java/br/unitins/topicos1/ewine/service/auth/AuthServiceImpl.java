package br.unitins.topicos1.ewine.service.auth;

// src/main/java/br/unitins/topicos1/ewine/service/auth/AuthServiceImpl.java


import br.unitins.topicos1.ewine.dto. auth.LoginDTO;
import br.unitins.topicos1. ewine.dto.auth.LoginDTOResponse;
import br.unitins.topicos1. ewine.model.perfil. Usuario;
import br.unitins.topicos1.ewine. service.perfil.UsuarioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject. Inject;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    JWTService jwtService;

    @Override
    public LoginDTOResponse login(LoginDTO loginDTO) {
        Usuario usuario = usuarioService.findByLoginAndSenha(loginDTO. login(), loginDTO.senha());
        
        if (usuario == null) {
            throw new IllegalArgumentException("Login ou senha inválidos");
        }

        String token = jwtService.generateToken(usuario);
        
        return LoginDTOResponse.valueOf(token, usuario);
    }
}