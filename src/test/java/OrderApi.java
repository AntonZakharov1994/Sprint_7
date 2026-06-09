import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderModel;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;


public class OrderApi {

    @Step("создаем заказ")
    public static Response createOrder(OrderModel orderModel) {
        return given()
                .header("Content-Type", "application/json")
                .body(orderModel)
                .when()
                .log().all()
                .post(CREATE_ORDER);
    }
@Step("Отменяем заказ")
    public static Response cancelOrder(Integer track) {
        return given()
                .when().log().all()
                .put(CANCEL_ORDER + "?track=" + track)
                .then()
                .log().all()
                .extract()
                .response();
    }
}