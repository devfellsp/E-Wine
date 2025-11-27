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

import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTO; // Importando o DTO de Ocasião
import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTOResponse; // Importando o DTO de Resposta de Ocasião
import br.unitins.topicos1.ewine.service.vinho.OcasiaoService;

@Path("/ocasioes") // Caminho ajustado para Ocasiões
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Ocasião", description = "Operações relacionadas às ocasiões de consumo de vinho") // Tag ajustada
public class OcasiaoResource {

    @Inject
    OcasiaoService ocasiaoService; // Injeção ajustada para OcasiaoService

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Criar nova ocasião", description = "Cria um novo tipo de ocasião (ex: Jantar Romântico, Festa, etc.) no sistema")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Ocasião criada com sucesso",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = OcasiaoDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response create(@Schema(implementation = OcasiaoDTO.class) OcasiaoDTO dto) {
        OcasiaoDTOResponse criado = ocasiaoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
       @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
 
    @Path("/{id}")
    @Operation(summary = "Buscar ocasião por ID", description = "Retorna uma ocasião específica pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Ocasião encontrada",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = OcasiaoDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Ocasião não encontrada")
    })
    public Response findById(
            @Parameter(description = "ID da ocasião", required = true)
            @PathParam("id") Long id) {
        OcasiaoDTOResponse ocasiao = ocasiaoService.findById(id);
        return Response.ok(ocasiao).build();
    }

    @GET
       @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
 
    @Operation(summary = "Listar todas as ocasiões", description = "Retorna uma lista com todas as ocasiões")
    @APIResponse(responseCode = "200", description = "Lista de ocasiões retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = OcasiaoDTOResponse.class)))
    public Response findAll() {
        List<OcasiaoDTOResponse> ocasioes = ocasiaoService.findAll();
        return Response.ok(ocasioes).build();
    }

    @GET
       @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
 
    @Path("/search")
    @Operation(summary = "Buscar ocasiões por nome", description = "Busca ocasiões que contenham o nome especificado")
    @APIResponse(responseCode = "200", description = "Lista de ocasiões encontradas",
                content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = OcasiaoDTOResponse.class)))
    public Response findByNome(
            @Parameter(description = "Nome da ocasião para busca")
            @QueryParam("nome") String nome) {
        List<OcasiaoDTOResponse> resultados = ocasiaoService.findByNome(nome);
        return Response.ok(resultados).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Atualizar ocasião", description = "Atualiza uma ocasião existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Ocasião atualizada com sucesso",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = OcasiaoDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Ocasião não encontrada"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID da ocasião", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = OcasiaoDTO.class) OcasiaoDTO dto) {
        OcasiaoDTOResponse atualizado = ocasiaoService.update(id, dto);
        return Response.ok(atualizado).build();
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Deletar ocasião", description = "Remove uma ocasião do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Ocasião deletada com sucesso"),
        @APIResponse(responseCode = "404", description = "Ocasião não encontrada")
    })
    public Response delete(
            @Parameter(description = "ID da ocasião", required = true)
            @PathParam("id") Long id) {
        ocasiaoService.delete(id);
        return Response.noContent().build();
    }
}
