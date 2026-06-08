import io.restassured.response.Response;
import model.CourierModel;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static data.CourierData.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends BaseApi {

    private CourierApiClient courierApiClient = new CourierApiClient();
    private Integer createdCourierId;


    @Test
    @DisplayName("Создание курьера, проверка статуса")
    @Description("Проверяем успешное создание курьера с корректными данными")
    public void createCourierTest() {
        CourierModel courierModel = new CourierModel(uniqueLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);

        Response response = courierApiClient.createCourier(courierModel);

        response.then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .and()
                .body("ok", is(true));

        createdCourierId = extractCourierId(new CourierModel(uniqueLogin, COURIER_PASSWORD, COURIER_FIRST_NAME));
    }

    @Test
    @DisplayName("Создание курьера с тем же логином")
    @Description("Проверяем обработку дублирующего логина — должен вернуть 409")
    public void createCourierWithDuplicateLoginTest() {
        // Сначала создаём курьера (успешно)
        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);
        courierApiClient.createCourier(courierModel).then().statusCode(201);

        // Пытаемся создать с тем же логином
        Response response = courierApiClient.createCourier(courierModel);

        response.then()
                .log().all()
                .assertThat()
                .statusCode(409);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверяем обработку отсутствия логина — должен вернуть 400")
    public void createCourierWithoutLoginTest() {
        CourierModel courierModel = new CourierModel("", COURIER_PASSWORD, COURIER_FIRST_NAME);

        Response response = courierApiClient.createCourier(courierModel);

        response.then()
                .log().all()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверяем обработку отсутствия пароля — должен вернуть 400")
    public void createCourierWithoutPasswordTest() {
        CourierModel courierModel = new CourierModel(COURIER_LOGIN, "", COURIER_FIRST_NAME);

        Response response = courierApiClient.createCourier(courierModel);

        response.then()
                .log().all()
                .assertThat()
                .statusCode(400);
    }

    @After
    public void deleteCreatedCourier() {
        if (createdCourierId != null) {
            Response loginResponse = courierApiClient.loginCourier(
                    new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME)
            );

            if (loginResponse.statusCode() == 200) {
                courierApiClient.deleteCourier(createdCourierId)
                        .then().statusCode(200);
            } else {
                System.out.println("Курьер не найден — пропуск удаления");
            }
        }
    }

    private int extractCourierId(CourierModel courierModel) {
        Response loginResponse = courierApiClient.loginCourier(courierModel);
        return loginResponse.jsonPath().getInt("id");
    }
}







