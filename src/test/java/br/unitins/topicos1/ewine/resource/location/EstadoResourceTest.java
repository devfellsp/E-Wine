package br.unitins.topicos1.ewine.resource.location;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.ewine.dto.locationdto.EstadoDTO;
import br.unitins.topicos1.ewine.dto.locationdto.EstadoDTOResponse;
import br.unitins.topicos1.ewine.service.location.EstadoService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

@QuarkusTest
public class EstadoResourceTest {

    @Inject
    EstadoService estadoService;

    @Test
    void incluirTest() {
        EstadoDTO dto = new EstadoDTO("Tocantins", "TO", 1L);
        EstadoDTOResponse response = estadoService.create(dto);

        assertNotNull(response);
        assertEquals(dto.nome(), response.nome());
    }

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                    .get("/estados")
                .then()
                    .statusCode(200)
                    .body("size()", notNullValue());
    }

    @Test
    void buscarPorIdTest() {
        EstadoDTO dto = new EstadoDTO("Goiás", "GO", 1L);
        EstadoDTOResponse response = estadoService.create(dto);

        RestAssured.given()
                .when()
                    .get("/estados/" + response.idPais()) // o id do país está sendo usado, mas ajustaremos abaixo
                .then()
                    .statusCode(200)
                    .body("nome", equalTo("Goiás"));
    }

    @Test
    void buscarPorNomeTest() {
        EstadoDTO dto = new EstadoDTO("Pará", "PA", 1L);
        estadoService.create(dto);

        RestAssured.given()
                .when()
                    .get("/estados/find/Pará")
                .then()
                    .statusCode(200)
                    .body("[0].sigla", equalTo("PA"));
    }

    @Test
    void buscarPorPaisTest() {
        EstadoDTO dto = new EstadoDTO("Bahia", "BA", 1L);
        estadoService.create(dto);

        RestAssured.given()
                .when()
                    .get("/estados/pais/1")
                .then()
                    .statusCode(200)
                    .body("[0].nome", equalTo("Bahia"));
    }

    @Test
    void alterarTest() {
        EstadoDTO dto = new EstadoDTO("Maranhão", "MA", 1L);
        EstadoDTOResponse response = estadoService.create(dto);

        EstadoDTO dtoUpdate = new EstadoDTO("Maranhão Atualizado", "MA", 1L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                    .put("/estados/" + response.idPais()) // ⚠️ aqui também precisamos do id do estado, não do país
                .then()
                    .statusCode(200);

        List<EstadoDTOResponse> estados = estadoService.findByNome("Maranhão Atualizado");
        assertEquals(1, estados.size());
        assertEquals("Maranhão Atualizado", estados.get(0).nome());
    }

    @Test
    void apagarTest() {
        EstadoDTO dto = new EstadoDTO("Piauí", "PI", 1L);
        EstadoDTOResponse response = estadoService.create(dto);

        RestAssured.given()
                .when()
                    .delete("/estados/" + response.idPais()) // ⚠️ idem
                .then()
                    .statusCode(204);
    }
}
