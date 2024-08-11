package br.gov.ma.feedback;

import org.junit.jupiter.api.Test;

import br.gov.ma.feedback.mongo.Usuario;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class UsuarioResourceTest {

    @Test
    public void testUsuario() {

        Usuario usuario = new Usuario();
        usuario.cpf = "12345678910";

        given() 
            .header("Content-Type", "application/json")
            .and()
            .body(usuario)
            .when()
            .post("/usuario/novo")
            .then()
            .statusCode(400);
    }

}
