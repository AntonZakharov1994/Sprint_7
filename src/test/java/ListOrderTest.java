import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;
import model.OrderModel;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;


public class ListOrderTest extends BaseApi {
    private Integer currentTrack;

    @Test
    public void getListOrderTest() {
        OrderModel orderModel = new OrderModel(
                "Naruto", "Uchiha", "Konoha, 142 apt.", "4",
                "+7 800 355 35 35", "5", "2020-06-06", "Saske, come back to Konoha",
                null
        );

        Response createResponse = given()
                .body(orderModel)
                .contentType("application/json")
                .when().log().all()
                .post(CREATE_ORDER)
                .then()
                .statusCode(201)
                .extract().response();

        currentTrack = createResponse.jsonPath().getInt("track");

        Response listResponse = given()
                .when().log().all()
                .get(GET_ORDER_LIST)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
    }


        @After
        public void cancelOrder ( ){

            Response cancelResponse1 = given()
                    .when().log().all()
                    .put(CANCEL_ORDER + "?track=" + currentTrack)
                    .then()
                    .log().all()
                    .extract()
                    .response();

        }

}








