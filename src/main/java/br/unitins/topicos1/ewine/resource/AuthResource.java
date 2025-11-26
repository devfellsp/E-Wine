package br.unitins.topicos1.ewine.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.unitins.topicos1.ewine.dto.AuthDTO;

import br.unitins.topicos1.ewine.model.perfil.Usuario;
import br.unitins.topicos1.ewine.dto.AuthDTOResponse;
import br.unitins.topicos1.ewine.service.auth.JWTService;
import br.unitins.topicos1.ewine.service.perfil.UsuarioService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    JWTService jwtService;

    @POST
    public Response login(AuthDTO dto) {
        if (dto == null || dto.login() == null || dto.senha() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        // Integração com seu UsuarioService:
        // Opção A: se você tem findByLoginAndSenha(login, senhaPlain) que já faz hashing internamente:
        Usuario usuario = usuarioService.findByLoginAndSenha(dto.login(), dto.senha());

        // Opção B (caso você NÃO tenha findByLoginAndSenha):
        // Usuario usuario = usuarioService.findByLogin(dto.login());
        // if (usuario != null) {
        //     // comparar senha plain com hash armazenado (ex: SHA-256 ou BCrypt)
        //     if (! hashCheck(dto.senha(), usuario.getSenha())) {
        //         usuario = null;
        //     }
        // }

        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(usuario.getLogin(), usuario.getPerfil() != null ? usuario.getPerfil().name() : null);
        Long expiresAt = jwtService.getExpirationEpochMillis();

        AuthDTOResponse response = AuthDTOResponse.of(
            token,
            "Bearer",
            expiresAt,
            usuario.getLogin(),
            usuario.getPerfil() != null ? usuario.getPerfil().name() : null
        );

        return Response.ok(response)
                       .header("Authorization", "Bearer " + token)
                       .build();
    }
}