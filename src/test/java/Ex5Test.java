import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class Ex5Test {
    @Test
    void getTextFromSecondMessage(){
        JsonPath response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        ArrayList allMessages = response.get("messages");
        HashMap secondMessage = (HashMap) allMessages.get(1);
        String secondMessageTxt = (String) secondMessage.get("message");

        System.out.println(secondMessageTxt);
    }

}
