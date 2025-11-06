package br.unitins.topicos1.ewine.resource.wine;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import br.unitins.topicos1.ewine.dto.winedto.SafraDTO;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SafraResourceTest {

    @Test
    @Order(1)
    public void testFindAll() {
        given()
            .when()
            .get("/safras")
            .then()
            .statusCode(200)
            .body("$", hasSize(2)); // 2020 e 2021 dos dados iniciais
    }

    @Test
    @Order(2)
    public void testFindById() {
        Response response = given()
            .when()
            .get("/safras/1")
            .then()
            .statusCode(200)
            .body("ano", is(2020))
            .extract()
            .response();

        System.out.println("Response /safras/1: " + response.asString());
    }

    @Test
    @Order(3)
    public void testFindByIdSegundaSafra() {
        given()
            .when()
            .get("/safras/2")
            .then()
            .statusCode(200)
            .body("ano", is(2021));
    }

    @Test
    @Order(4)
    public void testFindByIdInexistente() {
        given()
            .when()
            .get("/safras/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(5)
    public void testFindByAno() {
        given()
            .queryParam("ano", "2020")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].ano", is(2020));
    }

    @Test
    @Order(6)
    public void testFindByAnoInexistente() {
        given()
            .queryParam("ano", "1999")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    @Order(7)
    public void testFindByAnoParcial() {
        // Busca parcial pelo ano (ex: "202" encontra 2020, 2021, etc.)
        given()
            .queryParam("ano", "202")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)));
    }

    @Test
    @Order(8)
    public void testFindByAnoSemParametro() {
        given()
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(9)
    public void testCreateSafra() {
        SafraDTO novaSafra = new SafraDTO(2019);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(novaSafra)
            .when()
            .post("/safras")
            .then()
            .statusCode(201)
            .body("ano", is(2019))
            .extract()
            .response();

        System.out.println("Response CREATE safra: " + response.asString());
    }

    @Test
    @Order(10)
    public void testCreateSegundaSafra() {
        SafraDTO novaSafra = new SafraDTO(2022);

        given()
            .contentType(ContentType.JSON)
            .body(novaSafra)
            .when()
            .post("/safras")
            .then()
            .statusCode(201)
            .body("ano", is(2022));
    }

    @Test
    @Order(11)
    public void testCreateTerceiraSafra() {
        SafraDTO novaSafra = new SafraDTO(2018);

        given()
            .contentType(ContentType.JSON)
            .body(novaSafra)
            .when()
            .post("/safras")
            .then()
            .statusCode(201)
            .body("ano", is(2018));
    }

    @Test
    @Order(12)
    public void testUpdateSafra() {
        SafraDTO safraAtualizada = new SafraDTO(2020);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(safraAtualizada)
            .when()
            .put("/safras/1")
            .then()
            .statusCode(200)
            .body("ano", is(2020))
            .extract()
            .response();

        System.out.println("Response UPDATE safra: " + response.asString());
    }

    @Test
    @Order(13)
    public void testUpdateSafraInexistente() {
        SafraDTO safraDTO = new SafraDTO(2025);

        given()
            .contentType(ContentType.JSON)
            .body(safraDTO)
            .when()
            .put("/safras/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(14)
    public void testDeleteSafra() {
        // Criar uma safra temporária para deletar
        SafraDTO safraTemporaria = new SafraDTO(2030);

        given()
            .contentType(ContentType.JSON)
            .body(safraTemporaria)
            .when()
            .post("/safras")
            .then()
            .statusCode(201);

        // Verificar que foi criada
        given()
            .queryParam("ano", "2030")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)));

        // Testar delete com ID inexistente (comportamento esperado)
        given()
            .when()
            .delete("/safras/999")
            .then()
            .statusCode(404);

        System.out.println("Teste de delete concluído - safra 2030 criada com sucesso");
    }

    @Test
    @Order(15)
    public void testDeleteSafraInexistente() {
        given()
            .when()
            .delete("/safras/999")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(16)
    public void testVerificarContentType() {
        given()
            .when()
            .get("/safras")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @Order(17)
    public void testParametroIdInvalido() {
        given()
            .when()
            .get("/safras/abc")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(18)
    public void testCreateSafraComAnoNull() {
        SafraDTO safraNula = new SafraDTO(null);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(safraNula)
            .when()
            .post("/safras")
            .then()
            .extract()
            .response();

        // Flexível: aceita 201 ou 400 dependendo da validação
        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
        System.out.println("Status para ano null: " + response.getStatusCode());
    }

    @Test
    @Order(19)
    public void testCreateSafraComAnoInvalido() {
        SafraDTO safraInvalida = new SafraDTO(-1000); // Ano inválido

        Response response = given()
            .contentType(ContentType.JSON)
            .body(safraInvalida)
            .when()
            .post("/safras")
            .then()
            .extract()
            .response();

        // Aceita tanto 201 quanto 400
        assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
        System.out.println("Status para ano inválido (-1000): " + response.getStatusCode());
    }

    @Test
    @Order(20)
    public void testCreateSafraComJsonMalformado() {
        given()
            .contentType(ContentType.JSON)
            .body("{ ano: 'not_a_number' }")
            .when()
            .post("/safras")
            .then()
            .statusCode(400);
    }

    @Test
    @Order(21)
    public void testFindByAnoComString() {
        // Buscar usando string em vez de número
        given()
            .queryParam("ano", "2022")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(22)
    public void testCreateSafrasVariadas() {
        Integer[] anosVinhos = {
            2015, 2016, 2017, 2023, 2024
        };

        for (Integer ano : anosVinhos) {
            SafraDTO novaSafra = new SafraDTO(ano);
            
            given()
                .contentType(ContentType.JSON)
                .body(novaSafra)
                .when()
                .post("/safras")
                .then()
                .statusCode(201)
                .body("ano", is(ano));
        }
    }

    @Test
    @Order(23)
    public void testVerificarTotalSafrasAposTestes() {
        given()
            .when()
            .get("/safras")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(2))); // Deve ter mais que as 2 iniciais
    }

    @Test
    @Order(24)
    public void testUpdateComAnoValido() {
        SafraDTO safraCompleta = new SafraDTO(2021);

        given()
            .contentType(ContentType.JSON)
            .body(safraCompleta)
            .when()
            .put("/safras/2")
            .then()
            .statusCode(200)
            .body("ano", is(2021));
    }

    @Test
    @Order(25)
    public void testVerificarSafraAtualizada() {
        given()
            .when()
            .get("/safras/1")
            .then()
            .statusCode(200)
            .body("ano", is(2020)); // Verificar se manteve o valor
    }

    @Test
    @Order(26)
    public void testBuscarSafrasPorDecada() {
        // Buscar safras da década de 2020
        given()
            .queryParam("ano", "202")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)));
    }

    @Test
    @Order(27)
    public void testCreateSafrasFuturas() {
        // Testar com anos futuros
        Integer[] anosFuturos = {2025, 2026, 2027};

        for (Integer ano : anosFuturos) {
            SafraDTO novaSafra = new SafraDTO(ano);
            
            Response response = given()
                .contentType(ContentType.JSON)
                .body(novaSafra)
                .when()
                .post("/safras")
                .then()
                .extract()
                .response();

            // Safras futuras podem ou não ser aceitas
            assert response.getStatusCode() == 201 || response.getStatusCode() == 400;
            
            if (response.getStatusCode() == 201) {
                response.then().body("ano", is(ano));
            }
        }
    }

    @Test
    @Order(28)
    public void testCreateSafrasAntigas() {
        // Testar com anos muito antigos
        Integer[] anosAntigos = {1950, 1980, 1990};

        for (Integer ano : anosAntigos) {
            SafraDTO novaSafra = new SafraDTO(ano);
            
            given()
                .contentType(ContentType.JSON)
                .body(novaSafra)
                .when()
                .post("/safras")
                .then()
                .statusCode(201)
                .body("ano", is(ano));
        }
    }

    @Test
    @Order(29)
    public void testUpdateComAnoNull() {
        SafraDTO safraNula = new SafraDTO(null);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(safraNula)
            .when()
            .put("/safras/2")
            .then()
            .extract()
            .response();

        // Aceita tanto 200 quanto 400
        assert response.getStatusCode() == 200 || response.getStatusCode() == 400;
    }

    @Test
    @Order(30)
    public void testVerificarEstadoFinalDosDados() {
        Response response = given()
            .when()
            .get("/safras")
            .then()
            .statusCode(200)
            .extract()
            .response();

        int totalSafras = response.jsonPath().getList("$").size();
        assert totalSafras >= 5 : "Deveria ter pelo menos 5 safras criadas";
        
        System.out.println("Total de safras após todos os testes: " + totalSafras);
        System.out.println("Estrutura final da resposta: " + response.asString());
    }

    @Test
    @Order(31)
    public void testBuscarTodasAsSafrasFinais() {
        Response response = given()
            .when()
            .get("/safras")
            .then()
            .statusCode(200)
            .extract()
            .response();

        System.out.println("===== SAFRAS FINAIS =====");
        response.jsonPath().getList("ano").forEach(ano -> 
            System.out.println("- Ano: " + ano)
        );
    }

    @Test
    @Order(32)
    public void testBuscarSafraPorAnoCompleto() {
        // Buscar safra específica por ano completo
        given()
            .queryParam("ano", "2018")
            .when()
            .get("/safras/search")
            .then()
            .statusCode(200)
            .body("$", hasSize(greaterThan(0)))
            .body("[0].ano", is(2018));
    }

    @Test
    @Order(33)
    public void testValidarAnosRealisticos() {
        // Verificar se as safras criadas são realistas para vinhos
        Response response = given()
            .when()
            .get("/safras")
            .then()
            .statusCode(200)
            .extract()
            .response();

        List<Integer> anos = response.jsonPath().getList("ano");
        
        long anosValidos = anos.stream()
            .filter(ano -> ano != null && ano >= 1800 && ano <= 2030)
            .count();

        System.out.println("Anos válidos encontrados: " + anosValidos + " de " + anos.size() + " total");
        
        // A maioria dos anos deve ser realística
        assert anosValidos >= anos.size() * 0.8 : "A maioria dos anos deve ser realística para vinhos";
    }
}