import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;

public class RestAPiAuto {

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
    public void createUser() {
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
    }

    @Test(dependsOnMethods = {"createUser"})
    public void getAllEmployeeDetails() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/public/v2/users")
                .then()
                .spec(responseSpecification)
                .log().all();
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
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Allasani Peddana1");
        requestBody.put("email", "allasani.peddana@15ce.com");
        requestBody.put("status", "active");

        given()
                .spec(requestSpecification)
                .body(requestBody)
                .log().all()
                .when()
                .patch("/public/v2/users/" + empid)
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
