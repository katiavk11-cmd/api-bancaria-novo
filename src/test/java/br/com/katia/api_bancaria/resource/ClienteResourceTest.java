package br.com.katia.api_bancaria.resource;

import br.com.katia.api_bancaria.testutil.TestDatabaseCleaner;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class ClienteResourceTest {

    @Inject
    TestDatabaseCleaner cleaner;

    @BeforeEach
    void setup() {
        cleaner.limpar();
    }

    @Test
    void deveCriarCliente() {
        String uuid = UUID.randomUUID().toString();

        given()
                .contentType("application/json")
                .body("{"
                        + "\"nome\":\"Cliente Teste\","
                        + "\"cpf\":\"" + uuid + "\","
                        + "\"email\":\"" + uuid + "@email.com\""
                        + "}")
                .when()
                .post("/clientes")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    void deveBuscarClientePorId() {
        Long id = criarCliente();

        given()
                .when()
                .get("/clientes/" + id)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    private Long criarCliente() {
        String uuid = UUID.randomUUID().toString();

        Response response =
                given()
                        .contentType("application/json")
                        .body("{"
                                + "\"nome\":\"Cliente Teste\","
                                + "\"cpf\":\"" + uuid + "\","
                                + "\"email\":\"" + uuid + "@email.com\""
                                + "}")
                        .when()
                        .post("/clientes")
                        .then()
                        .statusCode(200)
                        .body("id", notNullValue())
                        .extract()
                        .response();

        return ((Number) response.jsonPath().get("id")).longValue();
    }
}