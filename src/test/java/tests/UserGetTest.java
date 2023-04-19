package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserGetTest {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void checkAuthByOneAndGetTwo() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = apiCoreRequests.responseGetAuth(authData);

        String header = authResponse.headers().get("x-csrf-token").getValue();
        String cookie = authResponse.cookies().get("auth_sid");
        String id = "1";

        Response dateResponse = apiCoreRequests.responseUserData(header, cookie, id);
        Map userDataDTO = dateResponse.as(Map.class);

        Assertions.assertTrue(apiCoreRequests.isUsername(userDataDTO), "В ответе отсутствует username");
        Assertions.assertTrue(apiCoreRequests.isFields(userDataDTO), "В ответе присутствуют лишние поля");
    }

}
