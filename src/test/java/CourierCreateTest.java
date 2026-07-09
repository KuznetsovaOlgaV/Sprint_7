import io.qameta.allure.junit4.AllureJunit4;
import io.restassured.RestAssured;
import org.junit.runner.RunWith;
import pojo.CourierPojo;
import io.qameta.allure.Step;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;


public class CourierCreateTest extends BaseCourierTest {

    @Step("Успешно курьер создан")
    @Test
    public void createCourierSuccess() {
        String login = "courierLoginSuccess" + System.currentTimeMillis();
        createTestCourier(login, "password1234", "courierNameSuccess");
    }

    @Step("Нельзя создать курьера без логина")
    @Test
    public void createCourierMissingLogin() {
        CourierPojo body = new CourierPojo(null, "password1234", "courierNameMissingLogin");

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Step("Нельзя создать курьера без пароля")
    @Test //этот тест падает - баг, в задании нет инфо
    public void createCourierMissingPassword() {
        String login = "courierLoginMissingPassword" + System.currentTimeMillis();
        CourierPojo body = new CourierPojo(login, null, "courierNameMissingPassword");

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Step("Нельзя создать двух курьеров с одинаковым логином")
    @Test
    public void createCourierDuplicateLogin() {
        String login = "twoCourierSameLogin" + System.currentTimeMillis();

        createTestCourier(login, "password1234", "courierFirstName");

        CourierPojo body = new CourierPojo(login, "password5678", "courierSecondName");

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(CREATE_PATH)
                .then()
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }
}
