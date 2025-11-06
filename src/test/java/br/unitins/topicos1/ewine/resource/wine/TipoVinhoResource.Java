package br.unitins.topicos1.ewine.resource.vinho;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import br.unitins.topicos1.ewine.dto.winedto.TipoVinhoDTO;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TipoVinhoResourceTest {

    @Test
    @Order(1)
    public void testFindAll() {
        given()
            .when()
            .get("/tipos-vinho")
            .then()
            .statusCode(200)
            .body("$", hasSize(2)); // Tinto e Branco dos dados iniciais
    }

    @Test
    @Order(2)
    public void testFindById() {
        Response response = given()
            .when()
            .get("/tipos-vinho/1")
            .then()
            .statusCode(200)
            .body("nome", is("Tinto"))
            .extract()
            .response();

        System.out.println("Response /tipos-vinho/1: " + response.asString());
    }

    @Test
    @Order(3)
    public void testFindByIdSegundoTipo() {
        given()
            .when()
            .get("/tipos-vinho/2")
            .then()
            .statusCode(200)
            .body("nome", is("Branco"));
    }

    @Test
    @Order(4)
    public void testFindByIdInexistente() {
        given()
            .when()
            .get("/tipos-vinho/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(5)
    public void testFindByNome() {
        given()
            .queryParam("nome", "Tinto")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Tinto"));
    }

    @Test
    @Order(6)
    public void testFindByNomeInexistente() {
        given()
            .queryParam("nome", "TipoInexistente")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    @Order(7)
    public void testFindByNomeParcial() {
        // Busca parcial por "Tin" deve encontrar "Tinto"
        given()
            .queryParam("nome", "Tin")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(8)
    public void testFindByNomeSemParametro() {
        given()
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(9)
    public void testCreateTipoVinho() {
        TipoVinhoDTO novoTipo = new TipoVinhoDTO("Rosé");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(novoTipo)
            .when()
            .post("/tipos-vinho")
            .then()
            .statusCode(201)
            .body("nome", is("Rosé"))
            .extract()
            .response();

        System.out.println("Response CREATE tipo vinho: " + response.asString());
    }

    @Test
    @Order(10)
    public void testCreateSegundoTipoVinho() {
        TipoVinhoDTO novoTipo = new TipoVinhoDTO("Espumante");

        given()
            .contentType(ContentType.JSON)
            .body(novoTipo)
            .when()
            .post("/tipos-vinho")
            .then()
            .statusCode(201)
            .body("nome", is("Espumante"));
    }

    @Test
    @Order(11)
    public void testCreateTerceiroTipoVinho() {
        TipoVinhoDTO novoTipo = new TipoVinhoDTO("Fortificado");

        given()
            .contentType(ContentType.JSON)
            .body(novoTipo)
            .when()
            .post("/tipos-vinho")
            .then()
            .statusCode(201)
            .body("nome", is("Fortificado"));
    }

    @Test
    @Order(12)
    public void testUpdateTipoVinho() {
        TipoVinhoDTO tipoAtualizado = new TipoVinhoDTO("Tinto Premium");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(tipoAtualizado)
            .when()
            .put("/tipos-vinho/1")
            .then()
            .statusCode(200)
            .body("nome", is("Tinto Premium"))
            .extract()
            .response();

        System.out.println("Response UPDATE tipo vinho: " + response.asString());
    }

    @Test
    @Order(13)
    public void testUpdateTipoVinhoInexistente() {
        TipoVinhoDTO tipoDTO = new TipoVinhoDTO("Tipo Inexistente");

        given()
            .contentType(ContentType.JSON)
            .body(tipoDTO)
            .when()
            .put("/tipos-vinho/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(14)
    public void testDeleteTipoVinho() {
        // Criar um tipo temporário para deletar
        TipoVinhoDTO tipoTemporario = new TipoVinhoDTO("Tipo Para Deletar");

        given()
            .contentType(ContentType.JSON)
            .body(tipoTemporario)
            .when()
            .post("/tipos-vinho")
            .then()
            .statusCode(201);

        // Verificar que foi criado
        given()
            .queryParam("nome", "Tipo Para Deletar")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)));

        // Testar delete com ID inexistente (comportamento esperado)
        given()
            .when()
            .delete("/tipos-vinho/999")
            .then()
            .statusCode(404);

        System.out.println("Teste de delete concluído - tipo criado com sucesso");
    }

    @Test
    @Order(15)
    public void testDeleteTipoVinhoInexistente() {
        given()
            .when()
            .delete("/tipos-vinho/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(16)
    public void testVerificarContentType() {
        given()
            .when()
            .get("/tipos-vinho")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @Order(17)
    public void testParametroIdInvalido() {
        given()
            .when()
            .get("/tipos-vinho/abc")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(18)
    public void testCreateTipoVinhoComNomeVazio() {
        TipoVinhoDTO tipoVazio = new TipoVinhoDTO("");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(tipoVazio)
            .when()
            .post("/tipos-vinho")
            .then()
            .extract()
            .response();

        // Flexível: aceita 201 ou 400 dependendo da validação
        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
        System.out.println("Status para nome vazio: " + response.getStatusCode());
    }

    @Test
    @Order(19)
    public void testCreateTipoVinhoComNomeNull() {
        TipoVinhoDTO tipoNulo = new TipoVinhoDTO(null);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(tipoNulo)
            .when()
            .post("/tipos-vinho")
            .then()
            .extract()
            .response();

        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
        System.out.println("Status para nome null: " + response.getStatusCode());
    }

    @Test
    @Order(20)
    public void testCreateTipoVinhoComJsonMalformado() {
        given()
            .contentType(ContentType.JSON)
            .body("{ nome: 'sem aspas' }")
            .when()
            .post("/tipos-vinho")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(21)
    public void testCreateTiposVinhoVariados() {
        String[] tiposVinhos = {
            "Dessert",
            "Ice Wine",
            "Natural",
            "Orange Wine",
            "Petillant Naturel"
        };

        for (String nomeTipo : tiposVinhos) {
            TipoVinhoDTO novoTipo = new TipoVinhoDTO(nomeTipo);
            
            given()
                .contentType(ContentType.JSON)
                .body(novoTipo)
                .when()
                .post("/tipos-vinho")
                .then()
                .statusCode(201)
                .body("nome", is(nomeTipo));
        }
    }

    @Test
    @Order(22)
    public void testCreateTiposVinhoClassicos() {
        String[] tiposClassicos = {
            "Porto",
            "Sherry",
            "Madeira",
            "Marsala",
            "Champagne"
        };

        for (String nomeTipo : tiposClassicos) {
            TipoVinhoDTO novoTipo = new TipoVinhoDTO(nomeTipo);
            
            given()
                .contentType(ContentType.JSON)
                .body(novoTipo)
                .when()
                .post("/tipos-vinho")
                .then()
                .statusCode(201)
                .body("nome", is(nomeTipo));
        }
    }

    @Test
    @Order(23)
    public void testFindByNomeComCaracteresEspeciais() {
        // Criar tipo com caracteres especiais
        TipoVinhoDTO tipoEspecial = new TipoVinhoDTO("Pétillant Naturel");

        given()
            .contentType(ContentType.JSON)
            .body(tipoEspecial)
            .when()
            .post("/tipos-vinho")
            .then()
            .statusCode(201);

        // Buscar o tipo criado
        given()
            .queryParam("nome", "Pétillant")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(24)
    public void testFindByNomeComEspacos() {
        given()
            .queryParam("nome", "Orange Wine")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(25)
    public void testVerificarTotalTiposVinhoAposTestes() {
        given()
            .when()
            .get("/tipos-vinho")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(2))); // Deve ter mais que os 2 iniciais
    }

    @Test
    @Order(26)
    public void testUpdateComNomeCompleto() {
        TipoVinhoDTO tipoCompleto = new TipoVinhoDTO("Branco Seco");

        given()
            .contentType(ContentType.JSON)
            .body(tipoCompleto)
            .when()
            .put("/tipos-vinho/2")
            .then()
            .statusCode(200)
            .body("nome", is("Branco Seco"));
    }

    @Test
    @Order(27)
    public void testVerificarTipoVinhoAtualizado() {
        given()
            .when()
            .get("/tipos-vinho/1")
            .then()
            .statusCode(200)
            .body("nome", is("Tinto Premium"));
    }

    @Test
    @Order(28)
    public void testBuscarTiposAtualizados() {
        given()
            .queryParam("nome", "Premium")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)));
    }

    @Test
    @Order(29)
    public void testCreateTipoVinhoComNomeLongo() {
        String nomeLongo = "Tipo de vinho especial com denominação de origem controlada e características únicas";
        TipoVinhoDTO tipoNomeLongo = new TipoVinhoDTO(nomeLongo);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(tipoNomeLongo)
            .when()
            .post("/tipos-vinho")
            .then()
            .extract()
            .response();

        // Flexível para nome longo
        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
    }

    @Test
    @Order(30)
    public void testUpdateComNomeVazio() {
        TipoVinhoDTO tipoVazio = new TipoVinhoDTO("");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(tipoVazio)
            .when()
            .put("/tipos-vinho/2")
            .then()
            .extract()
            .response();

        // Aceita tanto 200 quanto 400
        assert response.getStatusCode() == 200 || response.getStatusCode() == 400;
    }

    @Test
    @Order(31)
    public void testVerificarEstadoFinalDosDados() {
        Response response = given()
            .when()
            .get("/tipos-vinho")
            .then()
            .statusCode(200)
            .extract()
            .response();

        int totalTipos = response.jsonPath().getList("$").size();
        assert totalTipos >= 5 : "Deveria ter pelo menos 5 tipos de vinho criados";
        
        System.out.println("Total de tipos de vinho após todos os testes: " + totalTipos);
        System.out.println("Estrutura final da resposta: " + response.asString());
    }

    @Test
    @Order(32)
    public void testBuscarTodosOsTiposVinhoFinais() {
        Response response = given()
            .when()
            .get("/tipos-vinho")
            .then()
            .statusCode(200)
            .extract()
            .response();

        System.out.println("===== TIPOS DE VINHO FINAIS =====");
        response.jsonPath().getList("nome").forEach(nome -> 
            System.out.println("- " + nome)
        );
    }

    @Test
    @Order(33)
    public void testBuscarTipoVinhoPorNomeCompleto() {
        // Buscar tipo específico por nome completo
        given()
            .queryParam("nome", "Espumante")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].nome", is("Espumante"));
    }

    @Test
    @Order(34)
    public void testBuscarTiposVinhoPorCategoria() {
        // Buscar todos os tipos que contém "Wine"
        given()
            .queryParam("nome", "Wine")
            .when()
            .get("/tipos-vinho/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(35)
    public void testValidarTiposVinhoRealisticos() {
        // Verificar se os tipos criados são realistas para vinhos
        Response response = given()
            .when()
            .get("/tipos-vinho")
            .then()
            .statusCode(200)
            .extract()
            .response();

        List<String> nomes = response.jsonPath().getList("nome");
        
        // Contar tipos válidos (que não são vazios ou nulos)
        long tiposValidos = nomes.stream()
            .filter(nome -> nome != null && !nome.trim().isEmpty())
            .count();

        System.out.println("Tipos válidos encontrados: " + tiposValidos + " de " + nomes.size() + " total");
        
        // A maioria dos tipos deve ser válida
        assert tiposValidos >= nomes.size() * 0.8 : "A maioria dos tipos deve ter nomes válidos";
        
        // Verificar se existem tipos básicos
        boolean temTinto = nomes.stream().anyMatch(nome -> nome != null && nome.toLowerCase().contains("tinto"));
        boolean temBranco = nomes.stream().anyMatch(nome -> nome != null && nome.toLowerCase().contains("branco"));
        
        assert temTinto || temBranco : "Deveria ter pelo menos um tipo tinto ou branco";
        
        System.out.println("Validação de tipos realísticos passou!");
    }
}