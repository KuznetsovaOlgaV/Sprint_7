import api.CourierApiClient;
import org.junit.After;
import org.junit.Before;
import pojo.CourierPojo;

public class BaseCourierTest {

    protected Integer createdCourierId;

    @Before
    public void setUp() {
        createdCourierId = null;
    }

    @After
    public void tearDown() {
        if (createdCourierId != null) {
            CourierApiClient.deleteCourierById(createdCourierId);
            createdCourierId = null;
        }
    }

    protected void createTestCourier(String login, String password, String firstName) {
        CourierPojo body = new CourierPojo(login, password, firstName);
        CourierApiClient.createCourier(body);

        createdCourierId = CourierApiClient.getCourierIdByLoginCredentials(login, password);
    }
}