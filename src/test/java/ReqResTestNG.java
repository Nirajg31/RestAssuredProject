import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqResTestNG
{
    private static final Logger logger = LogManager.getLogger(ReqResTestNG.class);
    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
        requestSpec = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .log().all();
        responseSpec = RestAssured
                .expect()
                .statusCode(200)
                .logDetail(io.restassured.filter.log.LogDetail.ALL);
    }

    @Test
    public void GetUser()
    {
        logger.info("Starting GET API Test...");
        given()
                .spec(requestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .body("page", equalTo(2)); // Validate the response
        logger.info("GET API Test Completed Successfully.");
    }

    @Test
    public void CreateUser() {
        logger.info("Starting POST API Test...");
        String payload = """
                {
                    "name": "morpheus",
                    "job": "leader"
                }
                """;

        given()
                .spec(requestSpec)
                .body(payload)
                .when()
                .post("/users")
                .then()
                .statusCode(201) // Specific to POST
                .log().all()
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader")); // Validate response data
        logger.info("POST API Test Completed Successfully.");
    }
}
