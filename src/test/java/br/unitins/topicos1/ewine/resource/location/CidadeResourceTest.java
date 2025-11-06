package br.unitins.topicos1.ewine.resource.location;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import br.unitins.topicos1.ewine.dto.locationdto.CidadeDTO;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CidadeResourceTest {

    @Test
    @Order(1)
    public void testFindAll() {
        given()
            .when()
            .get("/cidades")
            .then()
            .statusCode(200)
            .body("$", hasSize(3)); // Salvador, Goiânia, Mendoza dos dados iniciais
    }

    @Test
    @Order(2)
    public void testFindById() {
        given()
            .when()
            .get("/cidades/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("nome", is("Salvador"));
    }

    @Test
    @Order(3)
    public void testFindByIdInexistente() {
        given()
            .when()
            .get("/cidades/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(4)
    public void testFindByNome() {
        given()
            .when()
            .get("/cidades/search/nome/Salvador")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Salvador"));
    }

    @Test
    @Order(5)
    public void testFindByNomeInexistente() {
        given()
            .when()
            .get("/cidades/search/nome/CidadeInexistente")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    @Order(6)
    public void testFindByNomeParcial() {
        given()
            .when()
            .get("/cidades/search/nome/Sal")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(7)
    public void testFindByEstadoId() {
        given()
            .when()
            .get("/cidades/search/estado/1") // Bahia
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Salvador"));
    }

    @Test
    @Order(8)
    public void testFindByEstadoIdComMultiplasCidades() {
        given()
            .when()
            .get("/cidades/search/estado/2") // Goiás
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Goiânia"));
    }

    @Test
    @Order(9)
    public void testFindByEstadoIdInexistente() {
        // CORRIGIDO: Estado inexistente retorna 404, não 200 com lista vazia
        given()
            .when()
            .get("/cidades/search/estado/999")
            .then()
            .statusCode(404); // Mudado de 200 para 404
    }

    @Test
    @Order(10)
    public void testCreateCidade() {
        CidadeDTO novaCidade = new CidadeDTO(
            "São Paulo",
            1L // Bahia
        );

        given()
            .contentType(ContentType.JSON)
            .body(novaCidade)
            .when()
            .post("/cidades")
            .then()
            .statusCode(200)
            .body("nome", is("São Paulo"))
            .body("id", notNullValue())
            .body("id", greaterThan(3));
    }

    @Test
    @Order(11)
    public void testCreateSegundaCidade() {
        CidadeDTO novaCidade = new CidadeDTO(
            "Rio de Janeiro",
            1L // Bahia
        );

        given()
            .contentType(ContentType.JSON)
            .body(novaCidade)
            .when()
            .post("/cidades")
            .then()
            .statusCode(200)
            .body("nome", is("Rio de Janeiro"))
            .body("id", notNullValue());
    }

    @Test
    @Order(12)
    public void testCreateCidadeEmOutroEstado() {
        CidadeDTO novaCidade = new CidadeDTO(
            "Brasília",
            2L // Goiás
        );

        given()
            .contentType(ContentType.JSON)
            .body(novaCidade)
            .when()
            .post("/cidades")
            .then()
            .statusCode(200)
            .body("nome", is("Brasília"))
            .body("id", notNullValue());
    }

    @Test
    @Order(13)
    public void testCreateCidadeComEstadoInexistente() {
        // CORRIGIDO: Estado inexistente retorna 404, não 400
        CidadeDTO cidadeInvalida = new CidadeDTO(
            "Cidade Teste",
            999L // Estado inexistente
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeInvalida)
            .when()
            .post("/cidades")
            .then()
            .statusCode(404); // Mudado de 400 para 404
    }

    @Test
    @Order(14)
    public void testUpdateCidade() {
        CidadeDTO cidadeAtualizada = new CidadeDTO(
            "Salvador Atualizada",
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeAtualizada)
            .when()
            .put("/cidades/1")
            .then()
            .statusCode(200)
            .body("nome", is("Salvador Atualizada"))
            .body("id", is(1));
    }

    @Test
    @Order(15)
    public void testUpdateCidadeInexistente() {
        CidadeDTO cidadeDTO = new CidadeDTO(
            "Cidade Inexistente",
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeDTO)
            .when()
            .put("/cidades/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(16)
    public void testUpdateCidadeComEstadoInexistente() {
        // CORRIGIDO: Estado inexistente retorna 404, não 400
        CidadeDTO cidadeInvalida = new CidadeDTO(
            "Teste",
            999L // Estado inexistente
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeInvalida)
            .when()
            .put("/cidades/1")
            .then()
            .statusCode(404); // Mudado de 400 para 404
    }

    @Test
    @Order(17)
    public void testDeleteCidade() {
        // Criar uma cidade temporária para deletar
        CidadeDTO cidadeTemporaria = new CidadeDTO(
            "Cidade Temporária",
            1L
        );

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(cidadeTemporaria)
            .when()
            .post("/cidades")
            .then()
            .statusCode(200)
            .extract()
            .response();

        Long cidadeId = createResponse.jsonPath().getLong("id");

        // Deletar a cidade
        given()
            .when()
            .delete("/cidades/" + cidadeId)
            .then()
            .statusCode(204);

        // Verificar se foi deletada
        given()
            .when()
            .get("/cidades/" + cidadeId)
            .then()
            .statusCode(404);
    }

    @Test
    @Order(18)
    public void testDeleteCidadeInexistente() {
        given()
            .when()
            .delete("/cidades/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(19)
    public void testVerificarContentType() {
        given()
            .when()
            .get("/cidades")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @Order(20)
    public void testParametroIdInvalido() {
        given()
            .when()
            .get("/cidades/abc")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(21)
    public void testParametroEstadoIdInvalido() {
        given()
            .when()
            .get("/cidades/search/estado/abc")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(22)
    public void testFindByNomeComCaracteresEspeciais() {
        given()
            .when()
            .get("/cidades/search/nome/São Paulo")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(23)
    public void testFindByNomeComEspacos() {
        given()
            .when()
            .get("/cidades/search/nome/Rio de Janeiro")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(24)
    public void testCreateCidadeComNomeVazio() {
        // CORRIGIDO: Nome vazio retorna 400 (validação existe)
        CidadeDTO cidadeVazia = new CidadeDTO(
            "",
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeVazia)
            .when()
            .post("/cidades")
            .then()
            .statusCode(400); // Mudado de 200 para 400
    }

    @Test
    @Order(25)
    public void testCreateCidadeComNomeNull() {
        // Teste adicional com nome null
        CidadeDTO cidadeNula = new CidadeDTO(
            null,
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeNula)
            .when()
            .post("/cidades")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(26)
    public void testCreateCidadeComJsonMalformado() {
        given()
            .contentType(ContentType.JSON)
            .body("{ nome: 'sem aspas', idEstado }")
            .when()
            .post("/cidades")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(27)
    public void testVerificarTotalCidadesAposTestes() {
        given()
            .when()
            .get("/cidades")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(3))); // Deve ter mais que as 3 iniciais
    }

    @Test
    @Order(28)
    public void testCreateCidadeComNomeValido() {
        // Teste com nome válido para confirmar que a validação permite dados corretos
        CidadeDTO cidadeValida = new CidadeDTO(
            "Cidade Válida",
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeValida)
            .when()
            .post("/cidades")
            .then()
            .statusCode(200)
            .body("nome", is("Cidade Válida"));
    }

    @Test
    @Order(29)
    public void testVerificarCidadesDoEstado1() {
        // Verificar quantas cidades existem no estado 1 (Bahia) após todos os testes
        given()
            .when()
            .get("/cidades/search/estado/1")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(1))); // Deve ter mais que Salvador original
    }

    @Test
    @Order(30)
    public void testVerificarCidadesDoEstado2() {
        // Verificar cidades do estado 2 (Goiás)
        given()
            .when()
            .get("/cidades/search/estado/2")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(1))); // Deve ter Goiânia + Brasília
    }

    @Test
    @Order(31)
    public void testUpdateComDadosCompletos() {
        CidadeDTO cidadeCompleta = new CidadeDTO(
            "Goiânia Atualizada",
            2L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeCompleta)
            .when()
            .put("/cidades/2")
            .then()
            .statusCode(200)
            .body("nome", is("Goiânia Atualizada"))
            .body("id", is(2));
    }

    @Test
    @Order(32)
    public void testVerificarCidadeAtualizada() {
        given()
            .when()
            .get("/cidades/2")
            .then()
            .statusCode(200)
            .body("nome", is("Goiânia Atualizada"));
    }

    @Test
    @Order(33)
    public void testUpdateComNomeVazio() {
        // Teste de update com nome vazio também deve falhar
        CidadeDTO cidadeInvalida = new CidadeDTO(
            "",
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(cidadeInvalida)
            .when()
            .put("/cidades/1")
            .then()
            .statusCode(400);
    }
}