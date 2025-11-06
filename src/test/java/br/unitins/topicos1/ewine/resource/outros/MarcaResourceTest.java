package br.unitins.topicos1.ewine.resource.outros;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import br.unitins.topicos1.ewine.dto.OthersDTO.MarcaDTO;
import br.unitins.topicos1.ewine.dto.OthersDTO.MarcaDTOResponse;
import br.unitins.topicos1.ewine.service.others.MarcaService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

@QuarkusTest
class MarcaResourceTest {

    @Inject
    MarcaService marcaService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                    .get("/marcas")
                .then()
                    .statusCode(200);
    }

    @Test
    void incluirTest() {
        MarcaDTO dto = new MarcaDTO(
            "Concha y Toro",
            "Chile",
            "1883",
            "Premium"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                    .post("/marcas")
                .then()
                    .statusCode(201)
                    .body("nome", CoreMatchers.is("Concha y Toro"),
                            "paisDeOrigem", CoreMatchers.is("Chile"),
                            "anofundacao", CoreMatchers.is("1883"),
                            "classificacao", CoreMatchers.is("Premium"));
    }

    @Test
void alterarTest() {
    MarcaDTO dto = new MarcaDTO(
        "Casa Perini",
        "Brasil",
        "1980",
        "Standard"
    );
    // Cria a marca e guarda o response com o ID
    MarcaDTOResponse response = marcaService.create(dto);

    MarcaDTO dtoUpdate = new MarcaDTO(
        "Casa Perini Atualizada",
        "Brasil",
        "1980",
        "Premium"
    );

    // Usa o ID em vez do nome
    RestAssured.given()
            .contentType(ContentType.JSON)
            .body(dtoUpdate)
            .when()
                .put("/marcas/" + response.id())
            .then()
                .statusCode(200);

    // Verifica se foi realmente alterado
    List<MarcaDTOResponse> marcas = marcaService.findByNome("Casa Perini Atualizada");
    assertEquals(1, marcas.size());
    MarcaDTOResponse marcaAtualizada = marcas.get(0);
    assertEquals(dtoUpdate.nome(), marcaAtualizada.nome());
    assertEquals(dtoUpdate.classificacao(), marcaAtualizada.classificacao());
}

@Test
void buscarPorIdTest() {
    // Garante que há uma marca no banco de teste
    MarcaDTO dto = new MarcaDTO(
        "Miolo",
        "Brasil",
        "1990",
        "Premium"
    );
    MarcaDTOResponse response = marcaService.create(dto);

    RestAssured.given()
            .when()
                .get("/marcas/" + response.id())
            .then()
                .statusCode(200)
                .body("nome", CoreMatchers.equalTo(dto.nome()));
}


    @Test
    void apagarTest() {
        MarcaDTO dto = new MarcaDTO(
            "Vinícola Aurora",
            "Brasil",
            "1931",
            "Standard"
        );
        marcaService.create(dto);

        // Buscar para obter dados completos
        List<MarcaDTOResponse> marcas = marcaService.findByNome("Vinícola Aurora");
        
        RestAssured.given()
                .when()
                    .delete("/marcas/1") // Ajuste o ID conforme necessário
                .then()
                    .statusCode(204);

        // verificando se foi deletado
        marcas = marcaService.findByNome("Vinícola Aurora");
        assertEquals(0, marcas.size());
    }

    
    @Test
    void buscarPorNomeTest() {
        MarcaDTO dto = new MarcaDTO(
            "Salton",
            "Brasil",
            "1910",
            "Premium"
        );
        marcaService.create(dto);

        RestAssured.given()
                .when()
                    .get("/marcas/search/nome/Salton")
                .then()
                    .statusCode(200)
                    .body("size()", CoreMatchers.not(0));
    }

    @Test
    void buscarPorIdInexistenteTest() {
        RestAssured.given()
                .when()
                    .get("/marcas/999999")
                .then()
                    .statusCode(404);
    }

    @Test
    void alterarMarcaInexistenteTest() {
        MarcaDTO dtoUpdate = new MarcaDTO(
            "Marca Inexistente",
            "Brasil",
            "2000",
            "Standard"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                    .put("/marcas/999999")
                .then()
                    .statusCode(404);
    }

    @Test
    void apagarMarcaInexistenteTest() {
        RestAssured.given()
                .when()
                    .delete("/marcas/999999")
                .then()
                    .statusCode(404);
    }
}