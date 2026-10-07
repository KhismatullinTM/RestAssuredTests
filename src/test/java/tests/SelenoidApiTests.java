package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SelenoidApiTests extends TestBaseApi {

    @Test
    @DisplayName("Проверка ответа статус кода 200 ОК")
    public void checkStatusCode() {

        given()
                .log().all()
                .when()
                .get("/ui/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/ui_status_json_schema.json"));
    }

    @Test
    @DisplayName("Проверка состояния Selenoid")
    public void checkSelenoidState(){
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/wd_hub_satus_json_schema.json"))
                .body("value.message", containsString("Selenoid v"))
                .body("value.ready", equalTo(true));
    }

    @Test
    @DisplayName("Проверка Content-Type application/json")
    public void checkResponseContentType() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    @DisplayName("Проверка запрета доступа к статусу Selenoid без авторизации")
    public void checkSelenoidStateUnauthorized(){
        given()
                .log().all()
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("Проверка отказа в доступе при неверных учетных данных")
    public void checkSelenoidStateWithInvalidCredentials() {
        given()
                .log().all()
                .auth().basic("wrongUser", "wrongPassword")
                .when()
                .get("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("Проверка ответа 404 для несуществующего эндпоинта")
    public void shouldReturn404ForUnknownEndpoint() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .get("/invalidpath")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("Проверка ответа на POST запрос к /wd/hub/status")
    public void checkPostRequestToStatusEndpoint() {
        given()
                .log().all()
                .auth().basic("user1", "1234")
                .when()
                .post("/wd/hub/status")
                .then()
                .log().all()
                .statusCode(405);
    }

}