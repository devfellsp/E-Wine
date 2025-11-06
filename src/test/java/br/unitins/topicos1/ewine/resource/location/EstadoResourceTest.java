package br.unitins.topicos1.ewine.resource.location;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import br.unitins.topicos1.ewine.dto.locationdto.EstadoDTO;
import br.unitins.topicos1.ewine.model.locationentities.Regiao;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EstadoResourceTest {

    @Test
    @Order(1)
    public void testBuscarTodos() {
        given()
            .when()
            .get("/estados")
            .then()
            .statusCode(200)
            .body("$", hasSize(3)); // Bahia, Goiás, Mendoza
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        given()
            .when()
            .get("/estados/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("nome", is("Bahia"))
            .body("sigla", is("BA"));
    }

    @Test
    @Order(3)
    public void testBuscarPorNome() {
        given()
            .when()
            .get("/estados/find/Bahia")
            .then()
            .statusCode(200)
            .body("$", hasSize(1))
            .body("[0].nome", is("Bahia"));
    }

    @Test
    @Order(4)
    public void testBuscarPorPais() {
        given()
            .when()
            .get("/estados/pais/1")
            .then()
            .statusCode(200)
            .body("$", hasSize(2)); // Bahia e Goiás são do Brasil
    }

    @Test
    @Order(5)
    public void testCriarEstado() {
        EstadoDTO novoEstado = new EstadoDTO(
            "São Paulo",
            "SP",
            1L,
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(novoEstado)
            .when()
            .post("/estados")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(6)
    public void testAtualizarEstado() {
        EstadoDTO estadoAtualizado = new EstadoDTO(
            "Rio de Janeiro",
            "RJ",
            1L,
            1L
        );

        given()
            .contentType(ContentType.JSON)
            .body(estadoAtualizado)
            .when()
            .put("/estados/1")
            .then()
            .statusCode(200)
            .body("nome", is("Rio de Janeiro"))
            .body("sigla", is("RJ"));
    }

    @Test
    @Order(7)
    public void testDeletarEstado() {
        given()
            .when()
            .delete("/estados/3") // Deletar Mendoza
            .then()
            .statusCode(204);
    }
}