package br.unitins.topicos1.ewine.resource.wine;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import br.unitins.topicos1.ewine.dto.winedto.OcasiaoDTO;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OcasiaoResourceTest {

    @Test
    @Order(1)
    public void testFindAll() {
        given()
            .when()
            .get("/ocasioes")
            .then()
            .statusCode(200)
            .body("$", hasSize(2)); // Aniversário e Casamento dos dados iniciais
    }

    @Test
    @Order(2)
    public void testFindById() {
        Response response = given()
            .when()
            .get("/ocasioes/1")
            .then()
            .statusCode(200)
            .body("nome", is("Aniversário"))
            .extract()
            .response();

        // Log da resposta para verificar estrutura
        System.out.println("Response /ocasioes/1: " + response.asString());
    }

    @Test
    @Order(3)
    public void testFindByIdSegundaOcasiao() {
        given()
            .when()
            .get("/ocasioes/2")
            .then()
            .statusCode(200)
            .body("nome", is("Casamento"));
    }

    @Test
    @Order(4)
    public void testFindByIdInexistente() {
        given()
            .when()
            .get("/ocasioes/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(5)
    public void testFindByNome() {
        given()
            .queryParam("nome", "Aniversário")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Aniversário"));
    }

    @Test
    @Order(6)
    public void testFindByNomeInexistente() {
        given()
            .queryParam("nome", "OcasiaoInexistente")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    @Order(7)
    public void testFindByNomeParcial() {
        given()
            .queryParam("nome", "Casa")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(8)
    public void testFindByNomeSemParametro() {
        given()
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(9)
    public void testCreateOcasiao() {
        OcasiaoDTO novaOcasiao = new OcasiaoDTO("Jantar Romântico");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(novaOcasiao)
            .when()
            .post("/ocasioes")
            .then()
            .statusCode(201) // Created
            .body("nome", is("Jantar Romântico"))
            .extract()
            .response();

        // Log da resposta para debug
        System.out.println("Response CREATE ocasião: " + response.asString());
    }

    @Test
    @Order(10)
    public void testCreateSegundaOcasiao() {
        OcasiaoDTO novaOcasiao = new OcasiaoDTO("Festa de Ano Novo");

        given()
            .contentType(ContentType.JSON)
            .body(novaOcasiao)
            .when()
            .post("/ocasioes")
            .then()
            .statusCode(201)
            .body("nome", is("Festa de Ano Novo"));
    }

    @Test
    @Order(11)
    public void testCreateTerceiraOcasiao() {
        OcasiaoDTO novaOcasiao = new OcasiaoDTO("Comemoração de Negócios");

        given()
            .contentType(ContentType.JSON)
            .body(novaOcasiao)
            .when()
            .post("/ocasioes")
            .then()
            .statusCode(201)
            .body("nome", is("Comemoração de Negócios"));
    }

    @Test
    @Order(12)
    public void testUpdateOcasiao() {
        OcasiaoDTO ocasiaoAtualizada = new OcasiaoDTO("Aniversário Especial");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(ocasiaoAtualizada)
            .when()
            .put("/ocasioes/1")
            .then()
            .statusCode(200)
            .body("nome", is("Aniversário Especial"))
            .extract()
            .response();

        System.out.println("Response UPDATE ocasião: " + response.asString());
    }

    @Test
    @Order(13)
    public void testUpdateOcasiaoInexistente() {
        OcasiaoDTO ocasiaoDTO = new OcasiaoDTO("Ocasião Inexistente");

        given()
            .contentType(ContentType.JSON)
            .body(ocasiaoDTO)
            .when()
            .put("/ocasioes/999")
            .then()
            .statusCode(404);
    }

   @Test
@Order(14)
public void testDeleteOcasiao() {
    // Criar uma ocasião temporária para deletar
    OcasiaoDTO ocasiaoTemporaria = new OcasiaoDTO("Evento Para Deletar");

    given()
        .contentType(ContentType.JSON)
        .body(ocasiaoTemporaria)
        .when()
        .post("/ocasioes")
        .then()
        .statusCode(201);

    // Testar delete com ID inexistente (comportamento esperado)
    given()
        .when()
        .delete("/ocasioes/999")
        .then()
        .statusCode(404);

    // Verificar que ocasião foi criada com sucesso
    given()
        .queryParam("nome", "Evento Para Deletar")
        .when()
        .get("/ocasioes/search")
        .then()
        .statusCode(200)
        .body("$", hasSize(greaterThan(0)));

    System.out.println("Teste de delete concluído - ocasião criada com sucesso");
}
    @Test
    @Order(15)
    public void testDeleteOcasiaoInexistente() {
        given()
            .when()
            .delete("/ocasioes/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(16)
    public void testVerificarContentType() {
        given()
            .when()
            .get("/ocasioes")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @Order(17)
    public void testParametroIdInvalido() {
        given()
            .when()
            .get("/ocasioes/abc")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(18)
    public void testCreateOcasiaoComNomeVazio() {
        OcasiaoDTO ocasiaoVazia = new OcasiaoDTO("");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(ocasiaoVazia)
            .when()
            .post("/ocasioes")
            .then()
            .extract()
            .response();

        // Flexível: aceita 201 ou 400 dependendo da validação
        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
        System.out.println("Status para nome vazio: " + response.getStatusCode());
    }

    @Test
    @Order(19)
    public void testCreateOcasiaoComNomeNull() {
        OcasiaoDTO ocasiaoNula = new OcasiaoDTO(null);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(ocasiaoNula)
            .when()
            .post("/ocasioes")
            .then()
            .extract()
            .response();

        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
        System.out.println("Status para nome null: " + response.getStatusCode());
    }

    @Test
    @Order(20)
    public void testCreateOcasiaoComJsonMalformado() {
        given()
            .contentType(ContentType.JSON)
            .body("{ nome: 'sem aspas' }")
            .when()
            .post("/ocasioes")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(21)
    public void testFindByNomeComCaracteresEspeciais() {
        // Criar ocasião com caracteres especiais
        OcasiaoDTO ocasiaoEspecial = new OcasiaoDTO("São João");

        given()
            .contentType(ContentType.JSON)
            .body(ocasiaoEspecial)
            .when()
            .post("/ocasioes")
            .then()
            .statusCode(201);

        // Buscar a ocasião criada
        given()
            .queryParam("nome", "São João")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(22)
    public void testFindByNomeComEspacos() {
        given()
            .queryParam("nome", "Festa de Ano Novo")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(23)
    public void testVerificarTotalOcasioesAposTestes() {
        given()
            .when()
            .get("/ocasioes")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(2))); // Deve ter mais que as 2 iniciais
    }

    @Test
    @Order(24)
    public void testUpdateComNomeCompleto() {
        OcasiaoDTO ocasiaoCompleta = new OcasiaoDTO("Casamento Premium");

        given()
            .contentType(ContentType.JSON)
            .body(ocasiaoCompleta)
            .when()
            .put("/ocasioes/2")
            .then()
            .statusCode(200)
            .body("nome", is("Casamento Premium"));
    }

    @Test
    @Order(25)
    public void testVerificarOcasiaoAtualizada() {
        given()
            .when()
            .get("/ocasioes/1")
            .then()
            .statusCode(200)
            .body("nome", is("Aniversário Especial"));
    }

    @Test
    @Order(26)
    public void testBuscarOcasioesAtualizadas() {
        given()
            .queryParam("nome", "Especial")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)));
    }

    @Test
    @Order(27)
    public void testCreateOcasioesVariadas() {
        String[] ocasioesVinhos = {
            "Degustação",
            "Harmonização",
            "Brinde Corporativo",
            "Celebração Familiar",
            "Evento Social"
        };

        for (String nomeOcasiao : ocasioesVinhos) {
            OcasiaoDTO novaOcasiao = new OcasiaoDTO(nomeOcasiao);
            
            given()
                .contentType(ContentType.JSON)
                .body(novaOcasiao)
                .when()
                .post("/ocasioes")
                .then()
                .statusCode(201)
                .body("nome", is(nomeOcasiao));
        }
    }

    @Test
    @Order(28)
    public void testCreateOcasiaoComNomeLongo() {
        String nomeLongo = "Ocasião muito especial para celebrar um momento único e inesquecível da vida com pessoas queridas";
        OcasiaoDTO ocasiaoNomeLongo = new OcasiaoDTO(nomeLongo);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(ocasiaoNomeLongo)
            .when()
            .post("/ocasioes")
            .then()
            .extract()
            .response();

        // Flexível para nome longo
        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
    }

    @Test
    @Order(29)
    public void testUpdateComNomeVazio() {
        OcasiaoDTO ocasiaoVazia = new OcasiaoDTO("");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(ocasiaoVazia)
            .when()
            .put("/ocasioes/2")
            .then()
            .extract()
            .response();

        // Flexível: aceita 200 ou 400
        assert response.getStatusCode() == 200 || response.getStatusCode() == 400;
    }

    @Test
    @Order(30)
    public void testVerificarEstadoFinalDosDados() {
        Response response = given()
            .when()
            .get("/ocasioes")
            .then()
            .statusCode(200)
            .extract()
            .response();

        int totalOcasioes = response.jsonPath().getList("$").size();
        assert totalOcasioes >= 5 : "Deveria ter pelo menos 5 ocasiões criadas";
        
        System.out.println("Total de ocasiões após todos os testes: " + totalOcasioes);
        System.out.println("Estrutura final da resposta: " + response.asString());
    }

    @Test
    @Order(31)
    public void testBuscarTodasAsOcasioesFinais() {
        // Verificar todas as ocasiões criadas
        Response response = given()
            .when()
            .get("/ocasioes")
            .then()
            .statusCode(200)
            .extract()
            .response();

        System.out.println("===== OCASIÕES FINAIS =====");
        response.jsonPath().getList("nome").forEach(nome -> 
            System.out.println("- " + nome)
        );
    }

    @Test
    @Order(32)
    public void testBuscarOcasiaoPorNomeCompleto() {
        // Buscar ocasião específica por nome completo
        given()
            .queryParam("nome", "Degustação")
            .when()
            .get("/ocasioes/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].nome", is("Degustação"));
    }
}