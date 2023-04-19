package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tests.UserGetTest;

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
    public Boolean isPhrase(String phrase) {
        boolean isPhrase = false;
        if (phrase.contains("The following required params are missed")) {
            isPhrase = true;
        }
        return isPhrase;
    }

    @Step("Аутентификация пользователя")
    public Response responseGetAuth(Map<String, String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
    }

    @Step("Получение данных пользователя")
    public Response responseUserData(String header, String cookie, String id) {
        return given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();
    }

    @Step("Поиск лишних значений в данных пользователя")
    public Boolean isFields(Map<String, String> fields){
        return !(fields.containsKey("firstName")
                || fields.containsKey("lastName")
                || fields.containsKey("email"));
    }

    @Step("Проверка username в данных пользователя")
    public Boolean isUsername(Map<String, String> fields){
        return fields.containsKey("username");
    }
}
