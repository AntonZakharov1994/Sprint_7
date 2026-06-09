import io.restassured.response.Response;
import model.OrderModel;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseApi {
    private Integer currentTrack;

    private final List<String> color;
    private final String testDescription;

    public CreateOrderTest(List<String> color, String testDescription) {
        this.color = color;
        this.testDescription = testDescription;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> provideOrderColors() {
        return Arrays.asList(new Object[][] {
                { null, "Цвет не указан" },
                { Arrays.asList("BLACK"), "Указан цвет BLACK" },
                { Arrays.asList("GREY"), "Указан цвет GREY" },
                { Arrays.asList("BLACK", "GREY"), "Указаны оба цвета" }
        });
    }

    @Test
    public void shouldCreateOrderWithDifferentColorOptions() {
        OrderModel orderModel = new OrderModel(
                "Naruto", "Uchiha", "Konoha, 142 apt.", "4",
                "+7 800 355 35 35", "5", "2020-06-06", "Saske, come back to Konoha",
                color
        );

        Response response = OrderApi.createOrder(orderModel);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());


        currentTrack = response.jsonPath().get("track");
    }

    @After
    public void cancelOrder() {
        if (currentTrack != null) {
            Response cancelResponse = OrderApi.cancelOrder(currentTrack);

        }
    }
}
