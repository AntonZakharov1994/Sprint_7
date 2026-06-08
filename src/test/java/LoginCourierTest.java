import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static data.CourierData.*;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;

public class LoginCourierTest extends BaseApi {
    private Integer courierId;
    private String uniqueLogin;
    private String password;

    @Before
    public void setUpLogin() {
        uniqueLogin = "courier_" + System.currentTimeMillis();
        password = COURIER_PASSWORD;

        CourierModel courierModel = new CourierModel(uniqueLogin, password, COURIER_FIRST_NAME);

        given()
                .header("Content-type", "application/json")
                .body(courierModel)
                .when()
                .post(CREATE_COURIER)
                .then()
                .statusCode(201);
    }

    private void authorizeAndGetCourierId() {
        CourierModel loginModel = new CourierModel(uniqueLogin, password, null);

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(200)
                .extract().response();

        String idString = loginResponse.jsonPath().getString("id");
        if (idString != null && !idString.isEmpty()) {
            try {
                courierId = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                System.out.println("Не удалось преобразовать ID в число: " + idString);
                courierId = null;
            }
        } else {
            System.out.println("ID курьера не получен из ответа авторизации");
            courierId = null;
        }
    }

    @Test
    public void loginCourierTest() {
        CourierModel loginModel = new CourierModel(uniqueLogin, password, null);

        Response response = given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(200)
                .extract().response();

        // Сохраняем ID после успешной авторизации
        String idString = response.jsonPath().getString("id");
        if (idString != null && !idString.isEmpty()) {
            courierId = Integer.parseInt(idString);
        }
    }

    @Test
    public void loginWithEmptyLoginFieldTest() {
        CourierModel loginModel = new CourierModel("", password, null);

        given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(400);
    }

    @Test
    public void loginWithEmptyPasswordFieldTest() {
        CourierModel loginModel = new CourierModel(uniqueLogin, "", null);

        given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(400);
    }

    @Test
    public void loginCourierWithIncorrectPasswordTest() {
        CourierModel loginModel = new CourierModel(uniqueLogin, password + "1", null);

        given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(404);
    }

    @Test
    public void loginCourierWithIncorrectLoginTest() {
        CourierModel loginModel = new CourierModel(uniqueLogin + "1", password, null);

        given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(404);
    }

    @Test
    public void loginWithNonExistentCourierCredentialsTest() {
        CourierModel loginModel = new CourierModel("nonexistent_login", "wrong_password", null);

        given()
                .header("Content-type", "application/json")
                .body(loginModel)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(404);
    }

    @After
    public void deleteCreatedCourier() {
        // Получаем ID только если ещё не получили (например, в тесте с валидными данными)
        if (courierId == null) {
            authorizeAndGetCourierId();
        }

        if (courierId != null) {
            given()
                    .pathParam("id", courierId)
                    .when()
                    .delete(DELETE_COURIER)
                    .then()
                    .statusCode(200);
        } else {
            System.out.println("Курьер не был создан или не авторизован — пропуск удаления");
        }
    }
}
