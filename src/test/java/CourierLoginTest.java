import api.CourierApiClient;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.LoginRequestPojo;
import org.apache.http.HttpStatus;

public class CourierLoginTest {

    private String testLogin;
    private String testPassword;
    private Integer testCourierId;

    @Before
    public void setUp() {
        testLogin = "login_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
        testPassword = "password1234";

        var courier = new pojo.CourierPojo(testLogin, testPassword, "CourierName");
        CourierApiClient.createCourier(courier);

        testCourierId = CourierApiClient.getCourierIdByLoginCredentials(testLogin, testPassword);
    }

    @After
    public void tearDown() {
        if (testCourierId != null) {
            CourierApiClient.deleteCourierById(testCourierId);
        }
    }

    @Description("Курьер с валидными учётными данными успешно авторизуется, возвращается 200 и id курьера")
    @Test
    public void loginSuccess() {
        Integer id = CourierApiClient.getCourierIdByLoginCredentials(testLogin, testPassword);
        Assert.assertNotNull("Ожидался id курьера не null", id);
    }


    @Description("При отсутствии login в запросе возвращается 400")
    @Test
    public void loginMissingLogin() {
        LoginRequestPojo body = new LoginRequestPojo(null, testPassword);
        CourierApiClient.loginExpectError(
                body,
                HttpStatus.SC_BAD_REQUEST,
                "Недостаточно данных для входа"
        );
    }

 //тест по докам 400, падает с 504 - баг в задании не указано
    @Description("При отсутствии password в запросе ожидается 400")
    @Test
    public void loginMissingPassword() {
        LoginRequestPojo body = new LoginRequestPojo(testLogin, null);
        CourierApiClient.loginExpectError(
                body,
                HttpStatus.SC_BAD_REQUEST,
                "Недостаточно данных для входа"
        );
    }

    @Description("Сервер при несуществующем логине возвращает 404")
    @Test
    public void wrongLogin() {
        String nonExistingLogin = "wrongLogin_" + System.currentTimeMillis();
        LoginRequestPojo body = new LoginRequestPojo(nonExistingLogin, "anyPassword");
        CourierApiClient.loginExpectError(
                body,
                HttpStatus.SC_NOT_FOUND,
                "Учетная запись не найдена"
        );
    }

    @Description("Сервер при верном логине, но неверном пароле возвращает 404")
    @Test
    public void wrongPassword() {
        LoginRequestPojo body = new LoginRequestPojo(testLogin, "wrongPassword123");
        CourierApiClient.loginExpectError(
                body,
                HttpStatus.SC_NOT_FOUND,
                "Учетная запись не найдена"
        );
    }
}