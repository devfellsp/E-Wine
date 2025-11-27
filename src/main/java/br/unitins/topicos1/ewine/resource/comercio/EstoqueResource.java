// src/main/java/br/unitins/topicos1/ewine/resource/product/EstoqueResource.java
package br.unitins.topicos1.ewine.resource.comercio;

import java.util.List;

import org.eclipse.microprofile.openapi. annotations.Operation;
import org. eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile. openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations. media.Schema;
import org. eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi. annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses. APIResponses;
import org. eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.unitins.topicos1.ewine.dto.comercio.EstoqueDTOResponse;
import br.unitins. topicos1.ewine.dto.comercio.MovimentacaoEstoqueDTO;
import br.unitins.topicos1.ewine.service.comercio.EstoqueService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject. Inject;
import jakarta.ws. rs. Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws. rs.GET;
import jakarta. ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs. Path;
import jakarta.ws. rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs. QueryParam;
import jakarta.ws. rs.core.MediaType;
import jakarta.ws. rs.core.Response;

@Path("/estoque")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Estoque", description = "Operações relacionadas ao controle de estoque")
public class EstoqueResource {

    @Inject
    EstoqueService estoqueService;

    // ===============================================
    // CONSULTAS BÁSICAS
    // ===============================================
    @GET
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar todos os estoques", 
              description = "Retorna uma lista com todos os estoques cadastrados")
    @APIResponse(responseCode = "200", description = "Lista de estoques retornada com sucesso",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = EstoqueDTOResponse.class)))
    public Response findAll() {
        List<EstoqueDTOResponse> estoques = estoqueService.findAll();
        return Response.ok(estoques).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar estoque por ID", 
              description = "Retorna um estoque específico pelo ID")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Estoque encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = EstoqueDTOResponse. class))),
        @APIResponse(responseCode = "404", description = "Estoque não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response findById(
            @Parameter(description = "ID do estoque", required = true)
            @PathParam("id") Long id) {
        EstoqueDTOResponse estoque = estoqueService.findById(id);
        return Response.ok(estoque).build();
    }

    @GET
    @Path("/produto/{idProduto}")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar estoque por produto", 
              description = "Retorna o estoque de um produto específico")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Estoque encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = EstoqueDTOResponse.class))),
        @APIResponse(responseCode = "404", description = "Estoque não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response findByProdutoId(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto) {
        EstoqueDTOResponse estoque = estoqueService.findByProdutoId(idProduto);
        return Response.ok(estoque).build();
    }

    // ===============================================
    // CONSULTAS ESPECIAIS - STATUS DO ESTOQUE
    // ===============================================
    @GET
    @Path("/status/baixo")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar produtos com estoque baixo", 
              description = "Retorna produtos com quantidade menor ou igual ao estoque mínimo")
    @APIResponse(responseCode = "200", description = "Lista de produtos com estoque baixo",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = EstoqueDTOResponse.class)))
    public Response findEstoqueBaixo() {
        List<EstoqueDTOResponse> estoques = estoqueService.findEstoqueBaixo();
        return Response.ok(estoques).build();
    }

    @GET
    @Path("/status/alto")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar produtos com estoque alto", 
              description = "Retorna produtos com quantidade maior ou igual ao estoque máximo")
    @APIResponse(responseCode = "200", description = "Lista de produtos com estoque alto",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = EstoqueDTOResponse. class)))
    public Response findEstoqueAlto() {
        List<EstoqueDTOResponse> estoques = estoqueService.findEstoqueAlto();
        return Response.ok(estoques).build();
    }

    @GET
    @Path("/status/esgotado")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar produtos esgotados", 
              description = "Retorna produtos com quantidade zero")
    @APIResponse(responseCode = "200", description = "Lista de produtos esgotados",
                content = @Content(mediaType = "application/json",
                                 schema = @Schema(type = SchemaType.ARRAY, implementation = EstoqueDTOResponse.class)))
    public Response findEsgotados() {
        List<EstoqueDTOResponse> estoques = estoqueService.findEsgotados();
        return Response.ok(estoques).build();
    }

    @GET
    @Path("/status/critico")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar produtos com estoque crítico", 
              description = "Retorna produtos com quantidade menor que o valor especificado")
    public Response findEstoqueCritico(
            @Parameter(description = "Quantidade limite para considerar crítico", example = "5")
            @QueryParam("limite") @DefaultValue("5") Integer limite) {
        List<EstoqueDTOResponse> estoques = estoqueService.findByQuantidadeMenorQue(limite);
        return Response.ok(estoques).build();
    }

    // ===============================================
    // MOVIMENTAÇÕES DE ESTOQUE
    // ===============================================
    @POST
    @Path("/produto/{idProduto}/entrada")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Adicionar estoque (ENTRADA)", 
              description = "Adiciona quantidade ao estoque de um produto")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Entrada de estoque realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = EstoqueDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados da movimentação inválidos"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response adicionarEstoque(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto,
            @Schema(implementation = MovimentacaoEstoqueDTO.class) MovimentacaoEstoqueDTO movimentacao) {
        EstoqueDTOResponse estoque = estoqueService.adicionarEstoque(idProduto, movimentacao);
        return Response.ok(estoque).build();
    }

    @POST
    @Path("/produto/{idProduto}/saida")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remover estoque (SAÍDA)", 
              description = "Remove quantidade do estoque de um produto")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Saída de estoque realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = EstoqueDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Dados da movimentação inválidos ou estoque insuficiente"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response removerEstoque(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto,
            @Schema(implementation = MovimentacaoEstoqueDTO.class) MovimentacaoEstoqueDTO movimentacao) {
        EstoqueDTOResponse estoque = estoqueService.removerEstoque(idProduto, movimentacao);
        return Response.ok(estoque).build();
    }

    @PUT
    @Path("/produto/{idProduto}/ajustar")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ajustar estoque", 
              description = "Define uma quantidade específica no estoque (ajuste manual)")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Ajuste de estoque realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = EstoqueDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Quantidade inválida (não pode ser negativa)"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response ajustarEstoque(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto,
            
            @Parameter(description = "Nova quantidade de estoque", required = true, example = "50")
            @QueryParam("quantidade") Integer novaQuantidade,
            
            @Parameter(description = "Motivo do ajuste", example = "Inventário físico")
            @QueryParam("motivo") @DefaultValue("Ajuste manual") String motivo) {
        EstoqueDTOResponse estoque = estoqueService. ajustarEstoque(idProduto, novaQuantidade, motivo);
        return Response. ok(estoque).build();
    }

    // ===============================================
    // ATUALIZAÇÃO DE LIMITES
    // ===============================================
    @PUT
    @Path("/produto/{idProduto}/limites")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar limites de estoque", 
              description = "Atualiza os valores de estoque mínimo e máximo")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Limites atualizados com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = EstoqueDTOResponse.class))),
        @APIResponse(responseCode = "400", description = "Limites inválidos"),
        @APIResponse(responseCode = "404", description = "Produto não encontrado"),
        @APIResponse(responseCode = "401", description = "Token JWT inválido ou expirado"),
        @APIResponse(responseCode = "403", description = "Acesso negado - apenas ADMINs")
    })
    public Response atualizarLimites(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto,
            
            @Parameter(description = "Estoque mínimo", required = true, example = "5")
            @QueryParam("estoqueMinimo") Integer estoqueMinimo,
            
            @Parameter(description = "Estoque máximo", required = true, example = "100")
            @QueryParam("estoqueMaximo") Integer estoqueMaximo) {
        EstoqueDTOResponse estoque = estoqueService.atualizarLimites(idProduto, estoqueMinimo, estoqueMaximo);
        return Response.ok(estoque). build();
    }

    // ===============================================
    // VALIDAÇÕES E CONSULTAS
    // ===============================================
    @GET
    @Path("/produto/{idProduto}/disponivel")
    @Operation(summary = "Verificar estoque disponível", 
              description = "Verifica se há estoque suficiente para uma quantidade específica")
    public Response verificarDisponibilidade(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto,
            
            @Parameter(description = "Quantidade desejada", required = true, example = "3")
            @QueryParam("quantidade") Integer quantidadeDesejada) {
        boolean disponivel = estoqueService.temEstoqueDisponivel(idProduto, quantidadeDesejada);
        return Response.ok("{\"disponivel\":" + disponivel + "}"). build();
    }

    @GET
    @Path("/produto/{idProduto}/status")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Status do estoque", 
              description = "Retorna informações sobre o status do estoque (baixo, alto, normal)")
    public Response statusEstoque(
            @Parameter(description = "ID do produto", required = true)
            @PathParam("idProduto") Long idProduto) {
        boolean estoqueBaixo = estoqueService.isEstoqueBaixo(idProduto);
        EstoqueDTOResponse estoque = estoqueService.findByProdutoId(idProduto);
        
        String status = estoque.statusEstoque();
        
        String resposta = String.format("""
            {
              "status": "%s",
              "estoqueBaixo": %b,
              "quantidade": %d,
              "estoqueMinimo": %d,
              "estoqueMaximo": %d
            }
            """, status, estoqueBaixo, estoque.quantidade(), 
                 estoque.estoqueMinimo(), estoque.estoqueMaximo());
        
        return Response.ok(resposta).build();
    }

    // ===============================================
    // RELATÓRIOS RÁPIDOS
    // ===============================================
    @GET
    @Path("/relatorio/resumo")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Resumo do estoque", 
              description = "Retorna um resumo geral dos estoques")
    public Response resumoEstoque() {
        List<EstoqueDTOResponse> todos = estoqueService.findAll();
        List<EstoqueDTOResponse> baixo = estoqueService.findEstoqueBaixo();
        List<EstoqueDTOResponse> esgotados = estoqueService.findEsgotados();
        
        String resumo = String.format("""
            {
              "totalProdutos": %d,
              "produtosEstoqueBaixo": %d,
              "produtosEsgotados": %d,
              "produtosOk": %d,
              "percentualCritico": %.2f
            }
            """, todos.size(), baixo.size(), esgotados.size(), 
                 todos.size() - baixo.size() - esgotados.size(),
                 todos.size() > 0 ? (baixo.size() + esgotados.size()) * 100.0 / todos.size() : 0.0);
        
        return Response.ok(resumo).build();
    }
}