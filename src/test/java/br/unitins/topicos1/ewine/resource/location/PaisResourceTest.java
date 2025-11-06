package br.unitins.topicos1.ewine.resource.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import br.unitins.topicos1.ewine.dto.locationdto.PaisDTO;
import br.unitins.topicos1.ewine.dto.locationdto.PaisDTOResponse;
import br.unitins.topicos1.ewine.service.location.PaisService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

@QuarkusTest
class PaisResourceTest {

    @Inject
    PaisService paisService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                    .get("/paises")
                .then()
                    .statusCode(200);
    }

    @Test
    void incluirTest() {
        PaisDTO dto = new PaisDTO("Brasil", "BR");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                    .post("/paises")
                .then()
                    .statusCode(200)
                    .body("id", CoreMatchers.notNullValue(),
                            "nome", CoreMatchers.is("Brasil"),
                            "sigla", CoreMatchers.is("BR"));
    }

    @Test
    void alterarTest() {
        PaisDTO dto = new PaisDTO("Argentina", "AR");
        PaisDTOResponse response = paisService.create(dto);

        PaisDTO dtoUpdate = new PaisDTO("Argentina Atualizada", "ARG");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                    .put("/paises/" + response.id())
                .then()
                    .statusCode(200);

        response = paisService.findById(response.id());
        assertEquals(dtoUpdate.nome(), response.nome());
        assertEquals(dtoUpdate.sigla(), response.sigla());
    }

@Test
void apagarTest() {
    // Cria um país primeiro
    PaisDTO dto = new PaisDTO("Brasil", "América do Sul");
    PaisDTOResponse response = paisService.create(dto);

    // Agora sim, apaga o país criado
    RestAssured.given()
            .when()
                .delete("/paises/" + response.id())
            .then()
                .statusCode(204);
}

    @Test
    void buscarPorIdTest() {
        PaisDTO dto = new PaisDTO("Uruguai", "UY");
        PaisDTOResponse response = paisService.create(dto);

        RestAssured.given()
                .when()
                    .get("/paises/" + response.id())
                .then()
                    .statusCode(200)
                    .body("id", CoreMatchers.is(response.id().intValue()),
                            "nome", CoreMatchers.is("Uruguai"),
                            "sigla", CoreMatchers.is("UY"));

        // Limpar
        paisService.delete(response.id());
    }

    @Test
    void buscarPorNomeTest() {
        PaisDTO dto = new PaisDTO("Portugal", "PT");
        PaisDTOResponse response = paisService.create(dto);

        RestAssured.given()
                .when()
                    .get("/paises/find/Portugal")
                .then()
                    .statusCode(200)
                    .body("size()", CoreMatchers.not(0));

        // Limpar
        paisService.delete(response.id());
    }

    @Test
    void buscarPorIdInexistenteTest() {
        RestAssured.given()
                .when()
                    .get("/paises/999999")
                .then()
                    .statusCode(404);
    }

    @Test
    void alterarPaisInexistenteTest() {
        PaisDTO dtoUpdate = new PaisDTO("País Inexistente", "XX");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                    .put("/paises/999999")
                .then()
                    .statusCode(404);
    }

    @Test
    void apagarPaisInexistenteTest() {
        RestAssured.given()
                .when()
                    .delete("/paises/999999")
                .then()
                    .statusCode(404);
    }

    @Test
    void buscarPorNomeInexistenteTest() {
        RestAssured.given()
                .when()
                    .get("/paises/find/PaisQueNaoExiste123456")
                .then()
                    .statusCode(200)
                    .body("size()", CoreMatchers.is(0));
    }
}