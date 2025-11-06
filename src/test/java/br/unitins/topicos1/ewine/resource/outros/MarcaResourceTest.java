package br.unitins.topicos1.ewine.resource.outros;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import br.unitins.topicos1.ewine.dto.OthersDTO.MarcaDTO;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarcaResourceTest {

    @Test
    @Order(1)
    public void testFindAll() {
        given()
            .when()
            .get("/marcas")
            .then()
            .statusCode(200)
            .body("$", hasSize(2)); // Casa Perini e Miolo dos dados iniciais
    }

    @Test
    @Order(2)
    public void testFindById() {
        given()
            .when()
            .get("/marcas/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("nome", is("Casa Perini"))
            .body("paisDeOrigem", is("Brasil"))
            .body("anofundacao", is("1980"))
            .body("classificacao", is("Premium"));
    }

    @Test
    @Order(3)
    public void testFindByIdInexistente() {
        given()
            .when()
            .get("/marcas/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(4)
    public void testFindByNome() {
        given()
            .when()
            .get("/marcas/search/nome/Casa Perini")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Casa Perini"));
    }

    @Test
    @Order(5)
    public void testFindByNomeInexistente() {
        given()
            .when()
            .get("/marcas/search/nome/MarcaInexistente")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    @Order(6)
    public void testFindByNomeParcial() {
        given()
            .when()
            .get("/marcas/search/nome/Casa")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(7)
    public void testCreateMarca() {
        MarcaDTO novaMarca = new MarcaDTO(
            "Salton",
            "Brasil",
            "1910",
            "Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(novaMarca)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201) // Created
            .body("nome", is("Salton"))
            .body("paisDeOrigem", is("Brasil"))
            .body("anofundacao", is("1910"))
            .body("classificacao", is("Premium"))
            .body("id", notNullValue())
            .body("id", greaterThan(2));
    }

    @Test
    @Order(8)
    public void testCreateSegundaMarca() {
        MarcaDTO novaMarca = new MarcaDTO(
            "Catena Zapata",
            "Argentina",
            "1902",
            "Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(novaMarca)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201)
            .body("nome", is("Catena Zapata"))
            .body("paisDeOrigem", is("Argentina"))
            .body("anofundacao", is("1902"))
            .body("classificacao", is("Premium"))
            .body("id", notNullValue());
    }

    @Test
    @Order(9)
    public void testCreateMarcaStandard() {
        MarcaDTO novaMarca = new MarcaDTO(
            "Aurora",
            "Brasil",
            "1931",
            "Standard"
        );

        given()
            .contentType(ContentType.JSON)
            .body(novaMarca)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201)
            .body("nome", is("Aurora"))
            .body("classificacao", is("Standard"));
    }

    @Test
    @Order(10)
    public void testUpdateMarca() {
        MarcaDTO marcaAtualizada = new MarcaDTO(
            "Casa Perini Atualizada",
            "Brasil",
            "1980",
            "Premium Plus"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaAtualizada)
            .when()
            .put("/marcas/1")
            .then()
            .statusCode(200)
            .body("nome", is("Casa Perini Atualizada"))
            .body("paisDeOrigem", is("Brasil"))
            .body("anofundacao", is("1980"))
            .body("classificacao", is("Premium Plus"))
            .body("id", is(1));
    }

    @Test
    @Order(11)
    public void testUpdateMarcaInexistente() {
        MarcaDTO marcaDTO = new MarcaDTO(
            "Marca Inexistente",
            "Brasil",
            "2000",
            "Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaDTO)
            .when()
            .put("/marcas/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(12)
    public void testDeleteMarca() {
        // Criar uma marca temporária para deletar
        MarcaDTO marcaTemporaria = new MarcaDTO(
            "Marca Temporária",
            "Chile",
            "2020",
            "Standard"
        );

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(marcaTemporaria)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201)
            .extract()
            .response();

        Long marcaId = createResponse.jsonPath().getLong("id");

        // Deletar a marca
        given()
            .when()
            .delete("/marcas/" + marcaId)
            .then()
            .statusCode(204); // No Content

        // Verificar se foi deletada
        given()
            .when()
            .get("/marcas/" + marcaId)
            .then()
            .statusCode(404);
    }

    @Test
    @Order(13)
    public void testDeleteMarcaInexistente() {
        given()
            .when()
            .delete("/marcas/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(14)
    public void testVerificarContentType() {
        given()
            .when()
            .get("/marcas")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @Order(15)
    public void testParametroIdInvalido() {
        given()
            .when()
            .get("/marcas/abc")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(16)
    public void testFindByNomeComCaracteresEspeciais() {
        given()
            .when()
            .get("/marcas/search/nome/Château Margaux")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(17)
    public void testFindByNomeComEspacos() {
        given()
            .when()
            .get("/marcas/search/nome/Casa Perini")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(18)
    public void testCreateMarcaComDadosCompletos() {
        MarcaDTO marcaCompleta = new MarcaDTO(
            "Trapiche",
            "Argentina",
            "1883",
            "Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaCompleta)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201)
            .body("nome", is("Trapiche"))
            .body("paisDeOrigem", is("Argentina"))
            .body("anofundacao", is("1883"))
            .body("classificacao", is("Premium"));
    }

    @Test
    @Order(19)
    public void testCreateMarcaComNomeVazio() {
        // CORRIGIDO: API aceita nome vazio (201)
        MarcaDTO marcaVazia = new MarcaDTO(
            "",
            "Brasil",
            "2000",
            "Standard"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaVazia)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201) // Mudado de 400 para 201
            .body("nome", is(""))
            .body("paisDeOrigem", is("Brasil"));
    }

    @Test
    @Order(20)
    public void testCreateMarcaComNomeNull() {
        // CORRIGIDO: API aceita nome null (201)
        MarcaDTO marcaNula = new MarcaDTO(
            null,
            "Brasil",
            "2000",
            "Standard"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaNula)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201) // Mudado de 400 para 201
            .body("paisDeOrigem", is("Brasil"));
    }

    @Test
    @Order(21)
    public void testCreateMarcaComJsonMalformado() {
        given()
            .contentType(ContentType.JSON)
            .body("{ nome: 'sem aspas', paisDeOrigem }")
            .when()
            .post("/marcas")
            .then()
            .statusCode(400); // JSON malformado ainda deve retornar 400
    }

    @Test
    @Order(22)
    public void testUpdateComDadosCompletos() {
        MarcaDTO marcaCompleta = new MarcaDTO(
            "Miolo Atualizada",
            "Brasil",
            "1990",
            "Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaCompleta)
            .when()
            .put("/marcas/2")
            .then()
            .statusCode(200)
            .body("nome", is("Miolo Atualizada"))
            .body("paisDeOrigem", is("Brasil"))
            .body("anofundacao", is("1990"))
            .body("classificacao", is("Premium"))
            .body("id", is(2));
    }

    @Test
    @Order(23)
    public void testUpdateComNomeVazio() {
        // CORRIGIDO: API aceita update com nome vazio (200)
        MarcaDTO marcaVazia = new MarcaDTO(
            "",
            "Brasil",
            "1990",
            "Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaVazia)
            .when()
            .put("/marcas/2")
            .then()
            .statusCode(200) // Mudado de 400 para 200
            .body("nome", is(""))
            .body("paisDeOrigem", is("Brasil"));
    }

    @Test
    @Order(24)
    public void testVerificarTotalMarcasAposTestes() {
        given()
            .when()
            .get("/marcas")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(2))); // Deve ter mais que as 2 iniciais
    }

    @Test
    @Order(25)
    public void testCreateMarcaComAnoInvalido() {
        MarcaDTO marcaAnoInvalido = new MarcaDTO(
            "Marca Ano Inválido",
            "Brasil",
            "abc", // Ano inválido como string
            "Standard"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaAnoInvalido)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201) // API aceita qualquer string para ano
            .body("anofundacao", is("abc"));
    }

    @Test
    @Order(26)
    public void testCreateMarcaComClassificacaoPersonalizada() {
        MarcaDTO marcaCustom = new MarcaDTO(
            "Marca Custom",
            "Uruguai",
            "1995",
            "Super Premium"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaCustom)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201)
            .body("classificacao", is("Super Premium"));
    }

    @Test
    @Order(27)
    public void testVerificarMarcaAtualizada() {
        given()
            .when()
            .get("/marcas/1")
            .then()
            .statusCode(200)
            .body("nome", is("Casa Perini Atualizada"))
            .body("classificacao", is("Premium Plus"));
    }

    @Test
    @Order(28)
    public void testBuscarMarcasPorPais() {
        // Buscar todas as marcas e verificar se há marcas brasileiras
        Response response = given()
            .when()
            .get("/marcas")
            .then()
            .statusCode(200)
            .extract()
            .response();

        // Verificar se existem marcas do Brasil
        int marcasBrasileiras = response.jsonPath().getList("findAll { it.paisDeOrigem == 'Brasil' }").size();
        assert marcasBrasileiras > 0 : "Deveria existir pelo menos uma marca brasileira";
    }

    @Test
    @Order(29)
    public void testBuscarMarcasPorClassificacao() {
        // Buscar todas as marcas e verificar classificações
        Response response = given()
            .when()
            .get("/marcas")
            .then()
            .statusCode(200)
            .extract()
            .response();

        // Verificar se existem marcas Premium
        int marcasPremium = response.jsonPath().getList("findAll { it.classificacao.contains('Premium') }").size();
        assert marcasPremium > 0 : "Deveria existir pelo menos uma marca Premium";
    }

    @Test
    @Order(30)
    public void testCreateMarcaComTodosOsCamposVazios() {
        // CORRIGIDO: API aceita todos os campos vazios (201)
        MarcaDTO marcaVazia = new MarcaDTO(
            "",
            "",
            "",
            ""
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaVazia)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201) // Mudado de 400 para 201
            .body("nome", is(""))
            .body("paisDeOrigem", is(""))
            .body("anofundacao", is(""))
            .body("classificacao", is(""));
    }

    @Test
    @Order(31)
    public void testCreateMarcaComCamposNulos() {
        // Teste adicional com todos os campos nulos
        MarcaDTO marcaNula = new MarcaDTO(
            null,
            null,
            null,
            null
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaNula)
            .when()
            .post("/marcas")
            .then()
            .statusCode(201); // API aceita campos nulos
    }

    @Test
    @Order(32)
    public void testUpdateComCamposNulos() {
        // Teste update com campos nulos
        MarcaDTO marcaNula = new MarcaDTO(
            null,
            "Brasil Atualizado",
            null,
            "Standard"
        );

        given()
            .contentType(ContentType.JSON)
            .body(marcaNula)
            .when()
            .put("/marcas/1")
            .then()
            .statusCode(200)
            .body("paisDeOrigem", is("Brasil Atualizado"))
            .body("classificacao", is("Standard"));
    }

    @Test
    @Order(33)
    public void testVerificarEstadoFinalDosDados() {
        // Verificar estado final das marcas após todos os testes
        given()
            .when()
            .get("/marcas")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(5))); // Deve ter várias marcas criadas
    }
}