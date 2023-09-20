package ch.hftm;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.quarkus.test.keycloak.server.KeycloakTestResourceLifecycleManager.getAccessToken;

@QuarkusTest
public class BlogResourceTest
{
    KeycloakTestClient keycloakCient = new KeycloakTestClient();

    @Test
    public void testInvalidToken() {
        /* RestAssured.given().auth().oauth2("TOKEN").body("{ \"title\": \"Test\", \"content\": \"Test\" }")
                .when().post( "/blog" )
                .then()
                .statusCode( 401 ); */
    }
}
