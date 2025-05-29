package br.com.rest.integration.swagger;

import br.com.rest.config.TestConfigs;
import br.com.rest.integrationstests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Test Should Display SwaggerUIPage")
    void testShouldDisplaySwaggerUIPage() {
        String content = given()
                .basePath("/swagger-ui.html")
                .port(TestConfigs.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        assertTrue(content.contains("<title>Swagger UI</title>"));
    }

}
