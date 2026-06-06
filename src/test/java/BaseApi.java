
import io.restassured.RestAssured;
import model.CourierModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static data.CourierData.*;
import static data.Endpoints.*;
import static io.restassured.RestAssured.given;


public class BaseApi {
    @BeforeClass
    public static void setUp(){
                RestAssured.baseURI = BASE_URL;


    }

}
