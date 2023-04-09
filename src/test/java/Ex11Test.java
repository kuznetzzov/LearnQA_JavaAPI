import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.apache.http.cookie.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Ex11Test {
    @Test
    public void checkCookie(){
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Assertions.assertEquals("hw_value", response.getCookie("HomeWork"),
                "Значение cookie HomeWork не соответствует значению hw_value " + response.getCookie("HomeWork"));
    }
}
