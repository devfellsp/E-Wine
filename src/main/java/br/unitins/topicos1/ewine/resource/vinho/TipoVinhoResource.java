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

import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTO;
import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTOResponse;
import br.unitins.topicos1.ewine.service.vinho.TipoVinhoService;

@Path("/tipos-vinho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tipo Vinho", description = "Operações relacionadas aos tipos de vinho")
public class TipoVinhoResource {

    @Inject
    TipoVinhoService tipoVinhoService;

    // ------------------------------
    // CREATE
    // ------------------------------
    @POST
    @Operation(summary = "Criar novo tipo de vinho", description = "Cria um novo tipo de vinho no sistema")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Tipo de vinho criado com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = TipoVinhoDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response create(
            @Schema(implementation = TipoVinhoDTO.class) TipoVinhoDTO tipoVinhoDTO) {
        TipoVinhoDTOResponse criado = tipoVinhoService.create(tipoVinhoDTO);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ------------------------------
    // READ
    // ------------------------------
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar tipo de vinho por ID", description = "Retorna um tipo de vinho específico pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Tipo de vinho encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoVinhoDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Tipo de vinho não encontrado")
    })
    public Response findById(
            @Parameter(description = "ID do tipo de vinho", required = true)
            @PathParam("id") Long id) {
        TipoVinhoDTOResponse tipoVinho = tipoVinhoService.findById(id);
        return Response.ok(tipoVinho).build();
    }

    @GET
    @Operation(summary = "Listar todos os tipos de vinho", description = "Retorna uma lista com todos os tipos de vinho")
    @APIResponse(responseCode = "200", description = "Lista de tipos de vinho retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = TipoVinhoDTOResponse.class)))
    public Response findAll() {
        List<TipoVinhoDTOResponse> tiposVinho = tipoVinhoService.findAll();
        return Response.ok(tiposVinho).build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Buscar tipos de vinho por nome", description = "Busca tipos de vinho que contenham o nome especificado")
    @APIResponse(responseCode = "200", description = "Lista de tipos de vinho encontrados",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = TipoVinhoDTOResponse.class)))
    public Response findByNome(
            @Parameter(description = "Nome do tipo de vinho para busca")
            @QueryParam("nome") String nome) {
        List<TipoVinhoDTOResponse> resultados = tipoVinhoService.findByNome(nome);
        return Response.ok(resultados).build();
    }

    // ------------------------------
    // UPDATE
    // ------------------------------
    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar tipo de vinho", description = "Atualiza um tipo de vinho existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Tipo de vinho atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoVinhoDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Tipo de vinho não encontrado"),
        @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response update(
            @Parameter(description = "ID do tipo de vinho", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = TipoVinhoDTO.class) TipoVinhoDTO tipoVinhoDTO) {
        TipoVinhoDTOResponse atualizado = tipoVinhoService.update(id, tipoVinhoDTO);
        return Response.ok(atualizado).build();
    }

    // ------------------------------
    // DELETE
    // ------------------------------
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar tipo de vinho", description = "Remove um tipo de vinho do sistema")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Tipo de vinho deletado com sucesso"),
        @APIResponse(responseCode = "404", description = "Tipo de vinho não encontrado")
    })
    public Response delete(
            @Parameter(description = "ID do tipo de vinho", required = true)
            @PathParam("id") Long id) {
        tipoVinhoService.delete(id);
        return Response.noContent().build();
    }
}