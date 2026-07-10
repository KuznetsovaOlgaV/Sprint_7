import api.CourierApiClient;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import pojo.CourierPojo;

public class CourierCreateTest {

    private String currentTestLogin;
    private String currentTestPassword;
    private Integer currentCourierId;

    @After
    public void tearDown() {
        if (currentCourierId != null) {
            CourierApiClient.deleteCourierById(currentCourierId);
        }
    }

    @Description("Курьер создаётся с валидными данными, возвращает 201 и ok=true")
    @Test
    public void createCourierSuccess() {
        currentTestLogin = "courierLoginSuccess" + System.currentTimeMillis();
        currentTestPassword = "password1234";
        CourierPojo body = new CourierPojo(currentTestLogin, currentTestPassword, "courierNameSuccess");

        CourierApiClient.createCourier(body);

        currentCourierId = CourierApiClient.getCourierIdByLoginCredentials(currentTestLogin, currentTestPassword);
    }

    @Description("При отсутствии login в теле запроса возвращает 400")
    @Test
    public void createCourierMissingLogin() {
        CourierPojo body = new CourierPojo(null, "password1234", "courierNameMissingLogin");
        CourierApiClient.createCourierExpectBadRequest(
                body,
                "Недостаточно данных для создания учетной записи"
        );
    }

    @Description("При отсутствии password в теле запроса ожидается 400")
    @Test
    public void createCourierMissingPassword() {
        currentTestLogin = "courierLoginMissingPassword" + System.currentTimeMillis();
        currentTestPassword = "password1234";
        CourierPojo body = new CourierPojo(currentTestLogin, null, "courierNameMissingPassword");

        CourierApiClient.createCourierExpectBadRequest(
                body,
                "Недостаточно данных для создания учетной записи"
        );
    }

    @Description("Попытка создать второго курьера с тем же логином: ожидается 409")
    @Test
    public void createCourierDuplicateLogin() {
        currentTestLogin = "twoCourierSameLogin" + System.currentTimeMillis();
        currentTestPassword = "password1234";

        CourierPojo firstBody = new CourierPojo(currentTestLogin, currentTestPassword, "courierFirstName");
        CourierApiClient.createCourier(firstBody);

        currentCourierId = CourierApiClient.getCourierIdByLoginCredentials(currentTestLogin, currentTestPassword);

        CourierPojo secondBody = new CourierPojo(currentTestLogin, "password5678", "courierSecondName");
        CourierApiClient.createCourierExpectConflict(
                secondBody,
                "Этот логин уже используется"
        );
    }
}