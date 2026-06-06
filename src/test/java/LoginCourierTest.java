import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CourierModel;
import org.junit.After;
import org.junit.Test;
import static data.CourierData.*;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;


public class LoginCourierTest extends BaseApi {

    @Test
    @Step("Логин с валидными данными")
    public void loginCourierTest(){

        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .when()
                .post(CREATE_COURIER)
                .then().log().all();
        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .and()
                .statusCode(200);

    }
    @Test
    @Step("Логин без ввода login")
    public void loginWithEmptyLoginFieldTest(){

        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .when()
                .post(CREATE_COURIER)
                .then().log().all();
        CourierModel courierModelForLogin = new CourierModel("",COURIER_PASSWORD, null     );
        given()
                .header("Content-type","application/json")
                .body(courierModelForLogin)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .and()
                .statusCode(400);

    }
    @Test
    @Step("Логин без ввода password")
    public void loginWithEmptyPasswordFieldTest(){

        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .when()
                .post(CREATE_COURIER)
                .then().log().all();
        CourierModel courierModelForLogin = new CourierModel(COURIER_LOGIN,"", null     );
        given()
                .header("Content-type","application/json")
                .body(courierModelForLogin)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .and()
                .statusCode(400);

    }
    @Test
    @Step("Логин с неправильным password")
    public void loginCourierWithIncorrectPasswordTest(){

        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .when()
                .post(CREATE_COURIER)
                .then().log().all();
        CourierModel courierModelForLogin = new CourierModel(COURIER_LOGIN,COURIER_PASSWORD+"1", null     );
        given()
                .header("Content-type","application/json")
                .body(courierModelForLogin)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .and()
                .statusCode(404);

    }
    @Test
    @Step("Логин с неправильным login")
    public void loginCourierWithIncorrectLoginTest(){

        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .when()
                .post(CREATE_COURIER)
                .then().log().all();
        CourierModel courierModelForLogin = new CourierModel(COURIER_LOGIN+"1",COURIER_PASSWORD, null     );
        given()
                .header("Content-type","application/json")
                .body(courierModelForLogin)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .and()
                .statusCode(404);

    }
    @Test
    @Step("Логин с данными несуществующего пользователя")
    public void loginWithNonExistentCourierCredentialsTest(){

        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);
        given()
                .header("Content-type","application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .and()
                .statusCode(404);

    }

    @After
    public void deleteCreatedCourier() {
        CourierModel courierModel = new CourierModel(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRST_NAME);

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(courierModel)
                .log().all()
                .when()
                .post(LOGIN_COURIER)
                .then().log().all()
                .extract().response();

        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.jsonPath().getInt("id");

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
