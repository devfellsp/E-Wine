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

import br.unitins.topicos1.ewine.dto.winedto.EstiloDTO; // Importando o DTO de Estilo
import br.unitins.topicos1.ewine.dto.winedto.EstiloDTOResponse; // Importando o DTO de Resposta de Estilo
import br.unitins.topicos1.ewine.service.vinho.EstiloService;

@Path("/estilos") // Caminho ajustado para Estilo
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Estilo", description = "Operações relacionadas aos estilos de vinho") // Tag ajustada
public class EstiloResource {

    @Inject
    EstiloService estiloService; // Injeção ajustada para EstiloService

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Criar novo estilo", description = "Cria um novo estilo de vinho (ex: Seco, Doce, etc.) no sistema")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Estilo criado com sucesso",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = EstiloDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response create(@Schema(implementation = EstiloDTO.class) EstiloDTO dto) {
        EstiloDTOResponse criado = estiloService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
       @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
 
    @Path("/{id}")
    @Operation(summary = "Buscar estilo por ID", description = "Retorna um estilo específico pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Estilo encontrado",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = EstiloDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Estilo não encontrado")
    })
    public Response findById(
            @Parameter(description = "ID do estilo", required = true)
            @PathParam("id") Long id) {
        EstiloDTOResponse estilo = estiloService.findById(id);
        return Response.ok(estilo).build();
    }

    @GET
       @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
 
    @Operation(summary = "Listar todos os estilos", description = "Retorna uma lista com todos os estilos")
    @APIResponse(responseCode = "200", description = "Lista de estilos retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = EstiloDTOResponse.class)))
    public Response findAll() {
        List<EstiloDTOResponse> estilos = estiloService.findAll();
        return Response.ok(estilos).build();
    }

    @GET
       @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
 
    @Path("/search")
    @Operation(summary = "Buscar estilos por nome", description = "Busca estilos que contenham o nome especificado")
    @APIResponse(responseCode = "200", description = "Lista de estilos encontrados",
                content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = SchemaType.ARRAY, implementation = EstiloDTOResponse.class)))
    public Response findByNome(
            @Parameter(description = "Nome do estilo para busca")
            @QueryParam("nome") String nome) {
        List<EstiloDTOResponse> resultados = estiloService.findByNome(nome);
        return Response.ok(resultados).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Path("/{id}")
    @Operation(summary = "Atualizar estilo", description = "Atualiza um estilo existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Estilo atualizado com sucesso",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = EstiloDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Estilo não encontrado"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID do estilo", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = EstiloDTO.class) EstiloDTO dto) {
        EstiloDTOResponse atualizado = estiloService.update(id, dto);
        return Response.ok(atualizado).build();
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Deletar estilo", description = "Remove um estilo do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Estilo deletado com sucesso"),
        @APIResponse(responseCode = "404", description = "Estilo não encontrado")
    })
    public Response delete(
            @Parameter(description = "ID do estilo", required = true)
            @PathParam("id") Long id) {
        estiloService.delete(id);
        return Response.noContent().build();
    }
}