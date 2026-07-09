import io.restassured.RestAssured;
import io.qameta.allure.junit4.AllureJunit4;
import org.junit.runner.RunWith;
import pojo.LoginRequestPojo;
import io.qameta.allure.Step;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;


public class CourierLoginTest extends BaseCourierTest {

    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Успешно курьер авторизуется")
    @Test
    public void loginSuccess() {
        String login = "loginSuccess" + System.currentTimeMillis();
        createTestCourier(login, "password1234", "courierName");

        LoginRequestPojo body = new LoginRequestPojo(login, "password1234");

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Step("Ошибка без логина")
    @Test
    public void loginMissingLogin() {
        LoginRequestPojo body = new LoginRequestPojo(null, "password1234");

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Step("Ошибка без пароля")
    // тест по докам 400, падает с 504 - баг в задании не указано, потому показываю тест был, но скрыла, т.к. иначе не формируется отчет
    @Test
    public void loginMissingPassword() {
        String login = "loginMissingPasswordMistake" + System.currentTimeMillis();
        LoginRequestPojo body = new LoginRequestPojo(login, null);

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Step("Ошибка неверная пара логин/пароль")
    @Test
    public void wrongCredentials() {
        String login = "wrongLogin" + System.currentTimeMillis();
        String password = "wrongPassword";
        LoginRequestPojo body = new LoginRequestPojo(login, password);

        RestAssured
                .given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }
}
