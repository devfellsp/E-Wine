package br.unitins.topicos1.ewine.resource.perfil;

import java.util.List;

import org.eclipse.microprofile.openapi. annotations.Operation;
import org. eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media. Schema;
import org.eclipse. microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse. microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse. microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi. annotations.tags.Tag;

import br.unitins.topicos1. ewine.dto.perfil.UsuarioDTO;
import br.unitins.topicos1.ewine. dto.perfil.UsuarioDTOResponse;
import br.unitins.topicos1. ewine.service.perfil.UsuarioService;
import io.quarkus.arc.All;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs. core.Response;

import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.PermitAll;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/usuarios")
@Produces(MediaType. APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuário", description = "Operações relacionadas aos usuários (ADMIN/CLIENTE)")
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @PermitAll // Permitir criação de usuário sem autenticação
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário (ADMIN ou CLIENTE)")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos ou login já existe")
    })
    public Response create(@Schema(implementation = UsuarioDTO. class) UsuarioDTO dto) {
        try {
            var usuario = usuarioService.create(dto);
            var response = UsuarioDTOResponse.valueOf(usuario);
            return Response. status(Response.Status.CREATED).entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status. BAD_REQUEST)
                          .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários")
    @APIResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = org.eclipse.microprofile.openapi.annotations.enums.SchemaType.ARRAY, 
                                                implementation = UsuarioDTOResponse.class)))
    public List<UsuarioDTOResponse> findAll() {
        return usuarioService.findAll()
            .stream()
            .map(UsuarioDTOResponse::valueOf)
            .toList();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public Response findById(
            @Parameter(description = "ID do usuário", required = true)
            @PathParam("id") Long id) {
        var usuario = usuarioService.findById(id);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                          . entity("{\"error\":\"Usuário não encontrado\"}").build();
        }
        return Response.ok(UsuarioDTOResponse.valueOf(usuario)).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    @Path("/search/login/{login}")
    @Operation(summary = "Buscar usuário por login", description = "Retorna um usuário específico pelo login")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioDTOResponse. class))),
        @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public Response findByLogin(
            @Parameter(description = "Login do usuário", required = true)
            @PathParam("login") String login) {
        var usuario = usuarioService.findByLogin(login);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                          .entity("{\"error\":\"Usuário não encontrado\"}").build();
        }
        return Response.ok(UsuarioDTOResponse. valueOf(usuario)).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")

    @Path("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Usuário não encontrado"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID do usuário", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = UsuarioDTO. class) UsuarioDTO dto) {
        try {
            usuarioService.update(id, dto);
            var usuario = usuarioService.findById(id);
            return Response.ok(UsuarioDTOResponse.valueOf(usuario)). build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("não encontrado")) {
                return Response.status(Response.Status.NOT_FOUND)
                              . entity("{\"error\":\"" + e.getMessage() + "\"}").build();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                          .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")

    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public Response delete(
            @Parameter(description = "ID do usuário", required = true)
            @PathParam("id") Long id) {
        // Verificar se usuário existe antes de deletar
        var usuario = usuarioService.findById(id);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                          . entity("{\"error\":\"Usuário não encontrado\"}").build();
        }
        
        usuarioService.delete(id);
        return Response.noContent().build();
    }
}               