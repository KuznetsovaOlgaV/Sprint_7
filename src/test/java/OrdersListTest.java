import api.OrdersApiClient;
import io.qameta.allure.Description;
import org.junit.Test;

public class OrdersListTest {

    @Description("Запрос GET /api/v1/orders возвращает список заказов с 200 и JSON-ответом")
    @Test
    public void getOrdersList() {
        OrdersApiClient.getOrdersList();
    }
}