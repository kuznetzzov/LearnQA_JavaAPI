package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Получение ответа сервера на создание пользователя")
    public Response createUser(Map<String, String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }

    @Step("Проверка метода на наличие фразы The following required params are missed")
    public Boolean isPhrase(String phrase){
        boolean isPhrase = false;
        if(phrase.contains("The following required params are missed")){
            isPhrase = true;
        }
        return isPhrase;
    }
}
