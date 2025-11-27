// src/main/java/br/unitins/topicos1/ewine/resource/product/ProdutoResource.java
package br.unitins.topicos1.ewine.resource.comercio;
import java.util.List;

import org.eclipse.microprofile.openapi. annotations.Operation;
import org. eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile. openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations. media.Schema;
import org. eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations. responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse. microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.unitins.topicos1.ewine.dto.comercio.ProdutoDTO;
import br.unitins.topicos1.ewine.dto.comercio.ProdutoResponseDTO;
import br.unitins.topicos1.ewine. dto.comercio.ProdutoUpdateDTO;
import br. unitins.topicos1.ewine.service.comercio.ProdutoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta. ws.rs. Consumes;
import jakarta. ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws. rs.GET;
import jakarta. ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs. Path;
import jakarta.ws. rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs. QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws. rs.core.Response;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produto", description = "Operações relacionadas aos produtos (vinhos comerciais)")
public class ProdutoResource {

    @Inject
    ProdutoService produtoService;

    // ===============================================
    // CREATE
    // ===============================================
    @POST
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Criar novo produto", 
              description = "Cria um novo produto com vinho associado e estoque inicial")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos ou SKU já existe"),
        @APIResponse(responseCode = "404", description = "Vinho não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response create(@Schema(implementation = ProdutoDTO.class) ProdutoDTO dto) {
        ProdutoResponseDTO criado = produtoService.create(dto);
        return Response. status(Response.Status.CREATED).entity(criado).build();
    }

    // ===============================================
    // READ - Consultas básicas
    // ===============================================
    @GET
    @Operation(summary = "Listar todos os produtos", 
              description = "Retorna uma lista com todos os produtos cadastrados")
    @APIResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoResponseDTO. class)))
    public Response findAll() {
        List<ProdutoResponseDTO> produtos = produtoService.findAll();
        return Response.ok(produtos).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar produto por ID", 
              description = "Retorna um produto específico pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response findById(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("id") Long id) {
        ProdutoResponseDTO produto = produtoService.findById(id);
        return Response.ok(produto).build();
    }

    @GET
    @Path("/sku/{sku}")
    @Operation(summary = "Buscar produto por SKU", 
              description = "Retorna um produto específico pelo código SKU")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @APIResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public Response findBySku(
            @Parameter(description = "SKU do produto", required = true, 
                      example = "CAB-2020-750")
            @PathParam("sku") String sku) {
        ProdutoResponseDTO produto = produtoService.findBySku(sku);
        return Response. ok(produto).build();
    }

    // ===============================================
    // READ - Consultas por status
    // ===============================================
    @GET
    @Path("/ativos")
    @Operation(summary = "Listar produtos ativos", 
              description = "Retorna apenas produtos com status ativo")
    @APIResponse(responseCode = "200", description = "Lista de produtos ativos",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoResponseDTO.class)))
    public Response findAtivos() {
        List<ProdutoResponseDTO> produtos = produtoService.findAtivos();
        return Response.ok(produtos). build();
    }

    @GET
    @Path("/inativos")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar produtos inativos", 
              description = "Retorna apenas produtos com status inativo")
    @APIResponse(responseCode = "200", description = "Lista de produtos inativos",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoResponseDTO.class)))
    public Response findInativos() {
        List<ProdutoResponseDTO> produtos = produtoService.findInativos();
        return Response.ok(produtos).build();
    }

    @GET
    @Path("/disponiveis")
    @Operation(summary = "Listar produtos disponíveis", 
              description = "Retorna produtos ativos e com estoque disponível")
    @APIResponse(responseCode = "200", description = "Lista de produtos disponíveis",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoResponseDTO.class)))
    public Response findDisponiveis() {
        List<ProdutoResponseDTO> produtos = produtoService.findDisponiveis();
        return Response.ok(produtos).build();
    }

    @GET
    @Path("/esgotados")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar produtos esgotados", 
              description = "Retorna produtos ativos mas sem estoque")
    @APIResponse(responseCode = "200", description = "Lista de produtos esgotados",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoResponseDTO. class)))
    public Response findEsgotados() {
        List<ProdutoResponseDTO> produtos = produtoService.findEsgotados();
        return Response. ok(produtos).build();
    }

    // ===============================================
    // READ - Consultas por vinho/características
    // ===============================================
    @GET
    @Path("/vinho/{idVinho}")
    @Operation(summary = "Buscar produtos por vinho", 
              description = "Retorna todos os produtos de um vinho específico")
    public Response findByVinhoId(
            @Parameter(description = "ID do vinho", required = true)
            @PathParam("idVinho") Long idVinho) {
        List<ProdutoResponseDTO> produtos = produtoService.findByVinhoId(idVinho);
        return Response.ok(produtos). build();
    }

    @GET
    @Path("/search/vinho")
    @Operation(summary = "Buscar produtos por nome do vinho", 
              description = "Busca produtos cujo vinho contenha o nome especificado")
    public Response findByVinhoNome(
            @Parameter(description = "Nome do vinho para busca", 
                      example = "Cabernet")
            @QueryParam("nome") String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.findByVinhoNome(nome);
        return Response.ok(produtos).build();
    }

    @GET
    @Path("/marca/{idMarca}")
    @Operation(summary = "Buscar produtos por marca", 
              description = "Retorna produtos de uma marca específica")
    public Response findByMarcaId(
            @Parameter(description = "ID da marca", required = true)
            @PathParam("idMarca") Long idMarca) {
        List<ProdutoResponseDTO> produtos = produtoService. findByMarcaId(idMarca);
        return Response. ok(produtos).build();
    }

    @GET
    @Path("/tipo/{idTipoVinho}")
    @Operation(summary = "Buscar produtos por tipo de vinho", 
              description = "Retorna produtos de um tipo de vinho específico")
    public Response findByTipoVinhoId(
            @Parameter(description = "ID do tipo de vinho", required = true)
            @PathParam("idTipoVinho") Long idTipoVinho) {
        List<ProdutoResponseDTO> produtos = produtoService.findByTipoVinhoId(idTipoVinho);
        return Response.ok(produtos).build();
    }

    // ===============================================
    // READ - Consultas por preço
    // ===============================================
    @GET
    @Path("/search/preco")
    @Operation(summary = "Buscar produtos por faixa de preço", 
              description = "Retorna produtos dentro da faixa de preço especificada")
    public Response findByPrecoRange(
            @Parameter(description = "Preço mínimo", example = "50.00")
            @QueryParam("precoMin") @DefaultValue("0") Double precoMin,
            
            @Parameter(description = "Preço máximo", example = "200.00")
            @QueryParam("precoMax") @DefaultValue("999999") Double precoMax) {
        List<ProdutoResponseDTO> produtos = produtoService.findByPrecoRange(precoMin, precoMax);
        return Response.ok(produtos).build();
    }

    // ===============================================
    // UPDATE
    // ===============================================
    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar produto", 
              description = "Atualiza dados básicos de um produto existente")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @APIResponse(responseCode = "400", description = "Dados inválidos ou SKU já existe"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response update(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("id") Long id,
            @Schema(implementation = ProdutoUpdateDTO.class) ProdutoUpdateDTO dto) {
        ProdutoResponseDTO atualizado = produtoService.update(id, dto);
        return Response.ok(atualizado). build();
    }

    @PUT
    @Path("/{id}/ativar")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ativar produto", 
              description = "Altera o status do produto para ativo")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Produto ativado com sucesso"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response ativar(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("id") Long id) {
        ProdutoResponseDTO produto = produtoService.ativar(id);
        return Response.ok(produto).build();
    }

    @PUT
    @Path("/{id}/desativar")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Desativar produto", 
              description = "Altera o status do produto para inativo")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Produto desativado com sucesso"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response desativar(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("id") Long id) {
        ProdutoResponseDTO produto = produtoService.desativar(id);
        return Response.ok(produto).build();
    }

    // ===============================================
    // DELETE
    // ===============================================
    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deletar produto", 
              description = "Remove um produto do sistema (cuidado: remove também o estoque! )")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Produto deletado com sucesso"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response delete(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("id") Long id) {
        produtoService.delete(id);
        return Response.noContent().build();
    }

    // ===============================================
    // VALIDAÇÕES
    // ===============================================
    @GET
    @Path("/validar/sku/{sku}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Verificar se SKU existe", 
              description = "Valida se um SKU já está cadastrado no sistema")
    public Response validarSku(
            @Parameter(description = "SKU para verificação", required = true)
            @PathParam("sku") String sku) {
        boolean existe = produtoService.existsBySku(sku);
        return Response.ok("{\"existe\":" + existe + "}").build();
    }

    @GET
    @Path("/{id}/disponivel")
    @Operation(summary = "Verificar disponibilidade", 
              description = "Verifica se um produto está disponível para venda")
    public Response verificarDisponibilidade(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("id") Long id) {
        boolean disponivel = produtoService.isDisponivel(id);
        return Response. ok("{\"disponivel\":" + disponivel + "}").build();
    }
}