package br.unitins.topicos1.ewine.resource.vinho;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.unitins.topicos1.ewine.dto.winedto.UvaDTO;
import br.unitins.topicos1.ewine.dto.winedto.UvaDTOResponse;
import br.unitins.topicos1.ewine.service.vinho.UvaService;

@Path("/uvas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Uva", description = "Operações relacionadas às uvas")
public class UvaResource {

    @Inject
    UvaService uvaService;

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Criar nova uva", description = "Cria uma nova uva no sistema")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Uva criada com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UvaDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response create(@Schema(implementation = UvaDTO.class) UvaDTO dto) {
        UvaDTOResponse criado = uvaService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar uva por ID", description = "Retorna uma uva específica pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Uva encontrada",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UvaDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Uva não encontrada")
    })
    public Response findById(
            @Parameter(description = "ID da uva", required = true)
            @PathParam("id") Long id) {
        UvaDTOResponse uva = uvaService.findById(id);
        return Response.ok(uva).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Listar todas as uvas", description = "Retorna uma lista com todas as uvas")
    @APIResponse(responseCode = "200", description = "Lista de uvas retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = UvaDTOResponse.class)))
    public Response findAll() {
        List<UvaDTOResponse> uvas = uvaService.findAll();
        return Response.ok(uvas).build();
    }

    @GET
    @Path("/search")
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar uvas por nome", description = "Busca uvas que contenham o nome especificado")
    @APIResponse(responseCode = "200", description = "Lista de uvas encontradas",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = UvaDTOResponse.class)))
    public Response findByNome(
            @Parameter(description = "Nome da uva para busca")
            @QueryParam("nome") String nome) {
        List<UvaDTOResponse> resultados = uvaService.findByNome(nome);
        return Response.ok(resultados).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Atualizar uva", description = "Atualiza uma uva existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Uva atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UvaDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Uva não encontrada"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID da uva", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = UvaDTO.class) UvaDTO dto) {
        UvaDTOResponse atualizado = uvaService.update(id, dto);
        return Response.ok(atualizado).build();
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Deletar uva", description = "Remove uma uva do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Uva deletada com sucesso"),
        @APIResponse(responseCode = "404", description = "Uva não encontrada")
    })
    public Response delete(
            @Parameter(description = "ID da uva", required = true)
            @PathParam("id") Long id) {
        uvaService.delete(id);
        return Response.noContent().build();
    }
}
