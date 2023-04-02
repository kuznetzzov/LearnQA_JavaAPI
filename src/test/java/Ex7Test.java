import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7Test {

    @Test
    void longRedirect() {

        String url = "https://playground.learnqa.ru/api/long_redirect";

        while (url != null) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();

            if (response.getHeader("Location") != null) {
                url = response.getHeader("Location");
                System.out.println(response.getHeader("Location"));
            } else {
                break;
            }
        }
    }
}
