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
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        ;
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
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
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
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCreatedCourier() {
        CourierModel courierForLogin = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);
        Response loginResponse = courierApiClient.loginCourier(courierForLogin);

        if (loginResponse.statusCode() == 200) {
            Integer courierIdToDelete = loginResponse.jsonPath().getInt("id");

            if (courierIdToDelete != null) {
                courierApiClient.deleteCourier(courierIdToDelete)
                        .then().statusCode(200);
                System.out.println("Курьер с ID " + courierIdToDelete + " успешно удалён");
            } else {
                System.out.println("Не удалось извлечь ID курьера из ответа логина");
            }
        } else {
            System.out.println("Логин курьера не удался (статус: " +
                    loginResponse.statusCode() + ") — пропуск удаления");
        }
    }
}







