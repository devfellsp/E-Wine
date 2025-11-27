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

import br.unitins.topicos1.ewine.dto.winedto.VinhoDTO;
import br.unitins.topicos1.ewine.dto.winedto.VinhoDTOResponse;
import br.unitins.topicos1.ewine.service.vinho.VinhoService;

@Path("/vinhos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Vinho", description = "Operações relacionadas aos vinhos")
public class VinhoResource {

    @Inject
    VinhoService vinhoService;

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")

    @Operation(summary = "Criar novo vinho", description = "Cria um novo vinho no sistema")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Vinho criado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = VinhoDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response create(@Schema(implementation = VinhoDTO.class) VinhoDTO dto) {
        VinhoDTOResponse criado = vinhoService.create(dto);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "Cliente"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar vinho por ID", description = "Retorna um vinho específico pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Vinho encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = VinhoDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Vinho não encontrado")
    })
    public Response findById(
            @Parameter(description = "ID do vinho", required = true)
            @PathParam("id") Long id) {
        VinhoDTOResponse vinho = vinhoService.findById(id);
        return Response.ok(vinho).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "Cliente"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Listar todos os vinhos", description = "Retorna uma lista com todos os vinhos")
    @APIResponse(responseCode = "200", description = "Lista de vinhos retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = VinhoDTOResponse.class)))
    public Response findAll() {
        List<VinhoDTOResponse> vinhos = vinhoService.findAll();
        return Response.ok(vinhos).build();
    }

    @GET
    @Path("/search")
    @RolesAllowed({"ADMIN", "Cliente"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar vinhos por nome", description = "Busca vinhos que contenham o nome especificado")
    @APIResponse(responseCode = "200", description = "Lista de vinhos encontrados",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = VinhoDTOResponse.class)))
    public Response findByNome(
            @Parameter(description = "Nome do vinho para busca")
            @QueryParam("nome") String nome) {
        List<VinhoDTOResponse> resultados = vinhoService.findByNome(nome);
        return Response.ok(resultados).build();
    }

    @GET
    @Path("/marca/{idMarca}")
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar vinhos por marca", description = "Retorna vinhos de uma marca específica")
    @APIResponse(responseCode = "200", description = "Lista de vinhos da marca",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = VinhoDTOResponse.class)))
    public Response findByMarca(
            @Parameter(description = "ID da marca", required = true)
            @PathParam("idMarca") Long idMarca) {
        List<VinhoDTOResponse> resultados = vinhoService.findByMarcaId(idMarca);
        return Response.ok(resultados).build();
    }

    @GET
    @Path("/tipo/{idTipoVinho}")
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar vinhos por tipo", description = "Retorna vinhos de um tipo específico")
    @APIResponse(responseCode = "200", description = "Lista de vinhos do tipo especificado",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = VinhoDTOResponse.class)))
    public Response findByTipoVinho(
            @Parameter(description = "ID do tipo de vinho", required = true)
            @PathParam("idTipoVinho") Long idTipoVinho) {
        List<VinhoDTOResponse> resultados = vinhoService.findByTipoVinhoId(idTipoVinho);
        return Response.ok(resultados).build();
    }

    @GET
    @Path("/pais/{idPais}")
    @RolesAllowed({"ADMIN", "CLIENTE"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Buscar vinhos por país", description = "Retorna vinhos de um país específico")
    @APIResponse(responseCode = "200", description = "Lista de vinhos do país especificado",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = VinhoDTOResponse.class)))
    public Response findByPais(
            @Parameter(description = "ID do país", required = true)
            @PathParam("idPais") Long idPais) {
        List<VinhoDTOResponse> resultados = vinhoService.findByPaisId(idPais);
        return Response.ok(resultados).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Atualizar vinho", description = "Atualiza um vinho existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Vinho atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = VinhoDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Vinho não encontrado"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID do vinho", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = VinhoDTO.class) VinhoDTO dto) {
        VinhoDTOResponse atualizado = vinhoService.update(id, dto);
        return Response.ok(atualizado).build();
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name = "bearerAuth")
    
    @Operation(summary = "Deletar vinho", description = "Remove um vinho do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Vinho deletado com sucesso"),
        @APIResponse(responseCode = "404", description = "Vinho não encontrado")
    })
    public Response delete(
            @Parameter(description = "ID do vinho", required = true)
            @PathParam("id") Long id) {
        vinhoService.delete(id);
        return Response.noContent().build();
    }
}