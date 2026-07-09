import pojo.OrderCreatePojo;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateParametrizedTest {

    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String PATH = "/api/v1/orders";

    private final List<String> colors;

    public OrderCreateParametrizedTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null},
                {List.of("BLACK")}, //BLACK или GREY
                {List.of("BLACK", "GREY")}
        });
    }

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создание заказа с цветами: {colors}")
    @Test
    public void createOrderWithColors() {
        OrderCreatePojo body = new OrderCreatePojo(
                "Анна",
                "Сахно",
                "Бульвар Рокоссовского, 15",
                "Сокольники",
                "79981212357",
                11,
                "2020-07-12",
                "Оставить у двери",
                colors
        );

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(PATH)
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("track", notNullValue());
    }
}