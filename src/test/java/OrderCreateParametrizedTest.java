import api.OrdersApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class OrderCreateParametrizedTest {

    private String getUniqueFirstName() {
        return "FirstName_" + UUID.randomUUID().toString().substring(0, 9);
    }

    private String getUniqueLastName() {
        return "LastName_" + UUID.randomUUID().toString().substring(0, 9);
    }

    private String getUniqueAddress() {
        return "Address_" + UUID.randomUUID().toString().substring(0, 9);
    }

    private String getUniqueMetro() {
        return "Metro_" + UUID.randomUUID().toString().substring(0, 9);
    }

    private String getUniquePhone() {
        return "+7999" + (int) (Math.random() * 9000000 + 1000000);
    }

    private String getUniqueComment() {
        return "Оставить у двери_" + UUID.randomUUID().toString().substring(0, 9);
    }

    private String getDeliveryDate() {
        return LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Description("Заказ с null")
    @Test
    public void createOrderWithNullColors() {
        createOrderWithColors(null);
    }

    @Description("Заказ с BLACK")
    @Test
    public void createOrderWithBlackColor() {
        createOrderWithColors(List.of("BLACK"));
    }

    @Description("Заказ с GREY")
    @Test
    public void createOrderWithGreyColor() {
        createOrderWithColors(List.of("GREY"));
    }

    @Description("Заказ с цветами BLACK и GREY")
    @Test
    public void createOrderWithBothColors() {
        createOrderWithColors(List.of("BLACK", "GREY"));
    }

    @Step("Создаём заказ (rentTime=11)")
    private void createOrderWithColors(List<String> colors) {
        var body = new pojo.OrderCreatePojo(
                getUniqueFirstName(),
                getUniqueLastName(),
                getUniqueAddress(),
                getUniqueMetro(),
                getUniquePhone(),
                11,  //в условиях нет инфо, потому условно 11, остальное по п.14 В тестах нет хардкода. вроде по максимуму
                getDeliveryDate(),
                getUniqueComment(),
                colors
        );
        OrdersApiClient.createOrder(body);
    }
}