import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CourierModel;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;

public class CourierApiClient {

@Step("Создаем курьера")
    public Response createCourier(CourierModel courierModel) {
        return given()
                .header("Content-type", "application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(CREATE_COURIER);
    }

@Step("Логин курьера в системе")
    public Response loginCourier(CourierModel courierModel) {
        return given()
                .header("Content-type", "application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(LOGIN_COURIER);
    }

@Step("Удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .pathParam("id", courierId)
                .log().all()
                .when()
                .delete(DELETE_COURIER);
    }
}