package br.unitins.topicos1.ewine.resource.vinho;

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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.unitins.topicos1.ewine.dto.winedto.SafraDTO; // Importando o DTO de Safra
import br.unitins.topicos1.ewine.dto.winedto.SafraDTOResponse; // Importando o DTO de Resposta de Safra
import br.unitins.topicos1.ewine.service.vinho.SafraService;

@Path("/safras") // Caminho ajustado para Safra
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Safra", description = "Operações relacionadas às safras de vinho") // Tag ajustada
public class SafraResource {

    @Inject
    SafraService safraService; // Injeção ajustada para SafraService

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @Operation(summary = "Criar nova safra", description = "Cria uma nova safra (ano) no sistema")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Safra criada com sucesso",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = SafraDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response create(@Schema(implementation = SafraDTO.class) SafraDTO dto) {
        SafraDTOResponse criado = safraService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar safra por ID", description = "Retorna uma safra específica pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Safra encontrada",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = SafraDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Safra não encontrada")
    })
    public Response findById(
            @Parameter(description = "ID da safra", required = true)
            @PathParam("id") Long id) {
        SafraDTOResponse safra = safraService.findById(id);
        return Response.ok(safra).build();
    }

    @GET
    @Operation(summary = "Listar todas as safras", description = "Retorna uma lista com todas as safras")
    @APIResponse(responseCode = "200", description = "Lista de safras retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = SafraDTOResponse.class)))
    public Response findAll() {
        List<SafraDTOResponse> safras = safraService.findAll();
        return Response.ok(safras).build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Buscar safras por ano", description = "Busca safras que contenham o ano especificado (busca parcial)")
    @APIResponse(responseCode = "200", description = "Lista de safras encontradas",
                content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = SafraDTOResponse.class)))
    public Response findByAno(
            @Parameter(description = "Ano da safra para busca")
            @QueryParam("ano") String ano) { // QueryParam ajustado para "ano"
        List<SafraDTOResponse> resultados = safraService.findByAno(ano); // Método de busca ajustado
        return Response.ok(resultados).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar safra", description = "Atualiza uma safra existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Safra atualizada com sucesso",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = SafraDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Safra não encontrada"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID da safra", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = SafraDTO.class) SafraDTO dto) {
        SafraDTOResponse atualizado = safraService.update(id, dto);
        return Response.ok(atualizado).build();
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar safra", description = "Remove uma safra do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Safra deletada com sucesso"),
        @APIResponse(responseCode = "404", description = "Safra não encontrada")
    })
    public Response delete(
            @Parameter(description = "ID da safra", required = true)
            @PathParam("id") Long id) {
        safraService.delete(id);
        return Response.noContent().build();
    }
}
