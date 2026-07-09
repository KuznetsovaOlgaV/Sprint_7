import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import org.junit.Test;

public class OrdersListTest {

    @Test
    public void getOrdersList() {
        RestAssured.given()
                .when()
                .get("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
}
