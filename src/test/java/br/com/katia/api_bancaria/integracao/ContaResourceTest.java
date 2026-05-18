package br.com.katia.api_bancaria.integracao;

import br.com.katia.api_bancaria.testutil.TestDatabaseCleaner;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ContaResourceTest {

    @Inject
    TestDatabaseCleaner cleaner;

    private Long clienteId;

    @BeforeEach
    void setup() {
        cleaner.limpar();
        clienteId = criarCliente();
    }

    private Long criarCliente() {
        String uuid = UUID.randomUUID().toString();

        String json = "{"
                + "\"nome\":\"Cliente Teste\","
                + "\"cpf\":\"" + uuid + "\","
                + "\"email\":\"" + uuid + "@email.com\""
                + "}";

        Response response =
                given()
                        .contentType("application/json")
                        .body(json)
                        .when()
                        .post("/clientes")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        return ((Number) response.jsonPath().get("id")).longValue();
    }

    private Long criarConta() {
        String json = "{"
                + "\"clienteId\":" + clienteId
                + "}";

        Response response =
                given()
                        .contentType("application/json")
                        .body(json)
                        .when()
                        .post("/contas")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        return ((Number) response.jsonPath().get("id")).longValue();
    }

    @Test
    void deveExecutarFluxoCompletoConta() {
        Long contaId = criarConta();

        given()
                .contentType("application/json")
                .body("{\"valor\":100.00}")
                .when()
                .post("/contas/" + contaId + "/deposito")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/contas/" + contaId + "/saldo")
                .then()
                .statusCode(200);
    }

    @Test
    void deveSacarViaApi() {
        Long contaId = criarConta();

        given()
                .contentType("application/json")
                .body("{\"valor\":200.00}")
                .when()
                .post("/contas/" + contaId + "/deposito")
                .then()
                .statusCode(200);

        given()
                .contentType("application/json")
                .body("{\"valor\":50.00}")
                .when()
                .post("/contas/" + contaId + "/saque")
                .then()
                .statusCode(200);
    }

    @Test
    void deveTransferirEntreContas() {
        Long origem = criarConta();
        Long destino = criarConta();

        given()
                .contentType("application/json")
                .body("{\"valor\":300.00}")
                .when()
                .post("/contas/" + origem + "/deposito")
                .then()
                .statusCode(200);

        String json = "{"
                + "\"contaOrigemId\":" + origem + ","
                + "\"contaDestinoId\":" + destino + ","
                + "\"valor\":100.00"
                + "}";

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/contas/transferencia")
                .then()
                .statusCode(200);
    }
}