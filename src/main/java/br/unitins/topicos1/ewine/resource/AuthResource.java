// src/main/java/br/unitins/topicos1/ewine/resource/auth/AuthResource.ja
package br.unitins.topicos1.ewine.resource;
import br.unitins.topicos1.ewine.dto.auth.LoginDTO;
import br.unitins. topicos1.ewine.dto.auth.LoginDTOResponse;
import br.unitins. topicos1.ewine.model.perfil.Usuario;
import br.unitins. topicos1.ewine.service.auth.JWTService;
import br.unitins.topicos1.ewine.service.perfil.UsuarioService;
import jakarta.inject.Inject;
import jakarta. ws.rs.*;
import jakarta. ws.rs.core.MediaType;
import jakarta.ws. rs.core.Response;


// Substitua temporariamente o AuthResource por este:
 

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    JWTService jwtService;

    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        try {
            // Teste 1: Verificar injeções
            if (usuarioService == null) {
                return Response. status(500).entity("{\"error\":\"UsuarioService não foi injetado\"}").build();
            }
            
            if (jwtService == null) {
                return Response.status(500).entity("{\"error\":\"JwtService não foi injetado\"}").build();
            }
            
            // Teste 2: Buscar usuário
            Usuario usuario = usuarioService.findByLoginAndSenha(loginDTO.login(), loginDTO.senha());
            
            if (usuario == null) {
                return Response.status(401).entity("{\"error\":\"Login ou senha inválidos\"}").build();
            }
            
            // Teste 3: Gerar token JWT
            String token = jwtService.generateToken(usuario);
            
            if (token == null || token.isEmpty()) {
                return Response.status(500).entity("{\"error\":\"Falha ao gerar token JWT\"}").build();
            }
            
            // Teste 4: Criar resposta completa
            LoginDTOResponse response = LoginDTOResponse.valueOf(token, usuario);
            
            return Response.ok(response). build();
                          
        } catch (Exception e) {
            return Response.status(500)
                          .entity("{\"error\":\"Erro específico: " + e.getMessage() + "\", \"stack\": \"" + e.getClass().getSimpleName() + "\"}")
                          .build();
        }
    }
}
/*@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Autenticação", description = "Operações de login e autenticação JWT")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @PermitAll // Login deve ser público (sem token)
    @Operation(summary = "Login do usuário", description = "Autentica usuário e retorna token JWT")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = LoginDTOResponse.class))),
        @APIResponse(responseCode = "401", description = "Login ou senha inválidos"),
        @APIResponse(responseCode = "400", description = "Dados de login inválidos")
    })
    public Response login(@Schema(implementation = LoginDTO.class) LoginDTO loginDTO) {
        try {
            // Validações básicas
            if (loginDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                              .entity("{\"error\":\"Dados de login são obrigatórios\"}").build();
            }
            
            if (loginDTO. login() == null || loginDTO.login(). trim().isEmpty()) {
                return Response.status(Response.Status. BAD_REQUEST)
                              .entity("{\"error\":\"Login é obrigatório\"}"). build();
            }
            
            if (loginDTO.senha() == null || loginDTO.senha(). trim().isEmpty()) {
                return Response.status(Response.Status. BAD_REQUEST)
                              .entity("{\"error\":\"Senha é obrigatória\"}").build();
            }

            LoginDTOResponse response = authService.login(loginDTO);
            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status. UNAUTHORIZED)
                          .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status. INTERNAL_SERVER_ERROR)
                          .entity("{\"error\":\"Erro interno do servidor\"}"). build();
        }
    }

    // Endpoint adicional para teste (opcional)
    @POST
    @Path("/validate")
    @SecurityRequirement(name = "bearerAuth") // ← USANDO SEU NOME CORRETO
    @Operation(summary = "Validar token", description = "Valida se o token JWT está válido")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Token válido"),
        @APIResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    public Response validateToken() {
        // Se chegou até aqui, o token é válido (verificado pelo framework)
        return Response.ok("{\"message\":\"Token válido\"}").build();
    }
}*/
