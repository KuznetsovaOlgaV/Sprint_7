package api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import pojo.CourierPojo;
import pojo.LoginRequestPojo;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.containsString;

public class CourierApiClient {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";
    private static final String DELETE_PATH_TEMPLATE = "/api/v1/courier/%d";

    static {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создаём курьера: login={body.login}")
    public static void createCourier(CourierPojo body) {
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));
    }

    @Step("получаем id курьера")
    public static Integer getCourierIdByLoginCredentials(String login, String password) {
        LoginRequestPojo body = new LoginRequestPojo(login, password);

        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("id");
    }

    @Step("Удаляем курьера по id={courierId}")
    public static void deleteCourierById(int courierId) {
        RestAssured
                .given()
                .when()
                .delete(String.format(DELETE_PATH_TEMPLATE, courierId))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("ok", is(true));
    }

    @Step("Пытаемся создать курьера с некорректными данными: ожидается BAD_REQUEST")
    public static void createCourierExpectBadRequest(CourierPojo body, String expectedMessagePart) {
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", containsString(expectedMessagePart));
    }

    @Step("Пытаемся создать курьера с дублирующимся логином: ожидается CONFLICT")
    public static void createCourierExpectConflict(CourierPojo body, String expectedMessagePart) {
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", containsString(expectedMessagePart));
    }

    @Step("Проверяем ошибочный логин: status={expectedStatusCode}")
    public static void loginExpectError(LoginRequestPojo body, int expectedStatusCode, String expectedMessageContains) {
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(expectedStatusCode)
                .body("message", containsString(expectedMessageContains));
    }
}