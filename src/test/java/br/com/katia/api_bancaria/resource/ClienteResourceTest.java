package br.com.katia.api_bancaria.resource;

import br.com.katia.api_bancaria.entity.Cliente;
import br.com.katia.api_bancaria.entity.Conta;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class ClienteResourceTest {

    @BeforeEach
    @Transactional
    void setup() {
        Conta.deleteAll();
        Cliente.deleteAll();
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

        Number id = response.jsonPath().get("id");
        return id.longValue();
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
}
