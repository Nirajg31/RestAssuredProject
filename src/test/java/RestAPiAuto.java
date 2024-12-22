import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;

public class RestAPiAuto {
    private static final Logger logger = LogManager.getLogger(RestAPiAuto.class);

    private static RequestSpecification requestSpecification;
    private static ResponseSpecification responseSpecification;
    private int empid;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://gorest.co.in";
        requestSpecification = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer b453c077ef7e6bd4d416df483369237e4c4ec649bd2a5d58c465b38c4db7ab93");
        responseSpecification = expect()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    public void createUser()
    {
        logger.info("Create user api execution started");
        Map<String, Object> createUserReq = new HashMap<>();
        createUserReq.put("name", "Niraj2 Gupta");
        createUserReq.put("gender", "Male");
        createUserReq.put("email", "niraj.gupt2a" + UUID.randomUUID() + "@abfc.com");
        createUserReq.put("status", "active");

        empid = given()
                .spec(requestSpecification)
                .body(createUserReq)
                .log().all()
                .when()
                .post("/public/v2/users/")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        System.out.println("Created User ID: " + empid);
        logger.info("Create user api execution Completed");

    }

    @Test(dependsOnMethods = {"createUser"})
    public void getAllEmployeeDetails()
    {
        logger.info("Get user details api execution started");

        given()
                .spec(requestSpecification)
                .when()
                .get("/public/v2/users")
                .then()
                .spec(responseSpecification)
                .log().all();
        logger.info("Get user details api execution Completed");

    }

    @Test(dependsOnMethods = {"getAllEmployeeDetails"})
    public void getEmployeeDetailsByID() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/public/v2/users/" + empid)
                .then()
                .spec(responseSpecification)
                .log().all();
    }

    @Test(dependsOnMethods = {"getEmployeeDetailsByID"})
    public void updateEmployeeDetails() {
        String randomName = "User_" + UUID.randomUUID().toString().substring(0, 8);
        String randomEmail = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", randomName);
        requestBody.put("email", randomEmail);
        requestBody.put("status", "active");

        // Send PUT request
        given()
                .spec(requestSpecification)
                .body(requestBody)
                .log().all()
                .when()
                .put("/public/v2/users/" + empid)
                .then()
                .spec(responseSpecification)
                .log().all();
    }

    @Test(dependsOnMethods = {"updateEmployeeDetails"})
    public void deleteEmpyloyee() {
        given()
                .spec(requestSpecification)
                .when()
                .delete("/public/v2/users/" + empid)
                .then()
                .statusCode(204)
                .log().all();
    }
}
