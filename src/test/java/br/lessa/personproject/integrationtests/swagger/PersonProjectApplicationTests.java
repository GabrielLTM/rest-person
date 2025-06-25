package br.lessa.personproject.integrationtests.swagger;

import br.lessa.personproject.config.TestConfigs;
import br.lessa.personproject.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue; // Importação necessária

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldDisplaySwaggerUiPage() {
        String content =
                given()
                        .basePath("/swagger-ui/index.html")
                            .port(TestConfigs.SERVER_PORT)
                        .when()
                            .get()
                        .then()
                            .statusCode(200)
                        .extract()
                            .body()
                                .asString();


        assertTrue(content.contains("Swagger UI"));
    }
}
