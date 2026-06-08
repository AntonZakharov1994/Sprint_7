import io.restassured.RestAssured;
import org.junit.BeforeClass;
import static data.Endpoints.*;

import java.util.UUID;

public class BaseApi {

    protected static String uniqueLogin;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
        uniqueLogin = "test_courier_" + UUID.randomUUID().toString();
    }

    protected String getUniqueLogin() {
        return uniqueLogin;
    }
}
