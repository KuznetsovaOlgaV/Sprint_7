import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.BeforeClass;

import static org.hamcrest.Matchers.is;

public class BaseCourierTest {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    protected static final String CREATE_PATH = "/api/v1/courier";
    protected static final String LOGIN_PATH = "/api/v1/courier/login";
    protected static final String DELETE_PATH_TEMPLATE = "/api/v1/courier/%d";

    protected Integer createdCourierId;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создать курьера: login={login}")
    protected void createTestCourier(String login, String password, String firstName) {
        RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(new pojo.CourierPojo(login, password, firstName))
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(201)
                .body("ok", is(true));

        createdCourierId = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(new pojo.LoginRequestPojo(login, password))
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @After
    @Step("Удалить курьера id={courierId}")
    public void deleteTestCourier() {
        if (createdCourierId != null) {
            RestAssured
                    .given()
                    .when()
                    .delete(String.format(DELETE_PATH_TEMPLATE, createdCourierId))
                    .then()
                    .statusCode(200)
                    .body("ok", is(true));

            createdCourierId = null;
        }
    }
}
