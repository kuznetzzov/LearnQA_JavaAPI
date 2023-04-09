import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Ex12Test {
    @Test
    public void checkHeader(){
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Assertions.assertEquals("Some secret value", response.getHeader("x-secret-homework-header"),
                "Значение header - x-secret-homework-header, не соответствует значению Some secret value " + response.getHeader("x-secret-homework-header"));
    }
}
