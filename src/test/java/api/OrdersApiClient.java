package api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import pojo.OrderCreatePojo;

import static org.hamcrest.Matchers.notNullValue;

public class OrdersApiClient {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDERS_PATH = "/api/v1/orders";

    static {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создаем заказ и проверяем track не null")
    public static void createOrder(OrderCreatePojo body) {
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(ORDERS_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(io.restassured.http.ContentType.JSON)
                .body("track", notNullValue());
    }

    @Step("Получаем список заказов, ожидается 200 и JSON")
    public static void getOrdersList() {
        RestAssured
                .given()
                .when()
                .get(ORDERS_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(io.restassured.http.ContentType.JSON);
    }
}