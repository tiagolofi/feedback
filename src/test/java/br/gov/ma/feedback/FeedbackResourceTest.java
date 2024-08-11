package br.gov.ma.feedback;

import org.junit.jupiter.api.Test;
import br.gov.ma.feedback.mongo.Feedback;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class FeedbackResourceTest {
    
    @Test
    public void testFeedback() {

        Feedback feedback = new Feedback();
        feedback.cpfDestino = "12345678910";
        feedback.cpfRemetente = "12345678910";

        given() 
            .header("Content-Type", "application/json")
            .and()
            .body(feedback)
            .when()
            .post("/feedback/novo")
            .then()
            .statusCode(400);
    }
}
