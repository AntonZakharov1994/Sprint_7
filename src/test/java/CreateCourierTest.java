import io.restassured.response.Response;
import model.CourierModel;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;
import static data.CourierData.*;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest extends BaseApi {




@Test
@Step("Создание курьера, проверка статуса")
    public void createCourierTest(){
    CourierModel courierModel = new CourierModel(COURIER_LOGIN,COURIER_PASSWORD,COURIER_FIRST_NAME);

    given()
            .header("Content-type","application/json")
            .body(courierModel)
            .log().all()
            .when()
            .post(CREATE_COURIER)
            .then().log().all()
            .assertThat().body("ok", is(true))
            .and()
            .statusCode(201);

}
@Test
    @Step("Создание курьера с тем же логином")
    public void createCourierWithDuplicateLoginTest(){
    CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);
    given()
            .header("Content-type","application/json")
            .body(courierModel)
            .log().all()
            .when()
            .post(CREATE_COURIER)
            .then().log().all()
            .assertThat().body("ok", is(true))
            .and()
            .statusCode(201);

    given()
            .header("Content-type", "application/json")
            .body(courierModel)
            .log().all()
            .when()
            .post(CREATE_COURIER)
            .then().log().all()
            .assertThat()
            .statusCode(409);

}
    @Test
    @Step("Создание курьера без логина")
    public void createCourierWithoutLoginTest(){
        CourierModel courierModel = new CourierModel("",COURIER_PASSWORD,COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(CREATE_COURIER)
                .then().log().all()
                .assertThat().statusCode(400);

    }
    @Test
    @Step("Создание курьера без пароля")
    public void createCourierWithoutPasswordTest(){
        CourierModel courierModel = new CourierModel(COURIER_LOGIN,"",COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(CREATE_COURIER)
                .then().log().all()
                .assertThat().statusCode(400);

    }
    @After
    public void deleteCreatedCourier() {
        // Создаём модель курьера с теми же данными
        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        // Логинимся, чтобы получить id курьера
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .extract().response();

        // Проверяем, что авторизация прошла успешно (статус 200)
        if (loginResponse.statusCode() == 200) {
            // Извлекаем id курьера из ответа
            int courierId = loginResponse.jsonPath().getInt("id");

            // Удаляем курьера по id
            given()
                    .pathParam("id", courierId)
                    .when()
                    .delete(DELETE_COURIER)
                    .then()
                    .statusCode(200);
        } else {
            System.out.println("Курьер не найден — пропуск удаления");
        }
    }
    }







