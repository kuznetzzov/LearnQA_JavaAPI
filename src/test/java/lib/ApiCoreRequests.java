package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public Boolean isFields(Map<String, String> fields) {
        return !(fields.containsKey("firstName")
                || fields.containsKey("lastName")
                || fields.containsKey("email"));
    }

    @Step("Проверка username в данных пользователя")
    public Boolean isUsername(Map<String, String> fields) {
        return fields.containsKey("username");
    }

    @Step("Редактирование пользователя")
    public Response responsePutEdit(Map<String, String> userData, String header, String cookie, String id) {
        return given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .body(userData)
                .put("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();
    }

    @Step("Генератор данных пользователя")
    public Map<String, String> userDataGenerator() {
        String code = String.valueOf(getRandomNumberUsingInts(100, 999));
        String email = "max-" + code + "@gmail.com";
        String username = "max-kuznetsov-" + code;
        String firstName = "max-" + code;
        String lastName = "kuznetsov-" + code;

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", code);
        userData.put("username", username);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);
        return userData;
    }

    @Step("Генератор числа в заданном пределе")
    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    @Step("Получение всех данных пользователя")
    public Response responseAllUserData(String header, String cookie, String id) {
        return given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();
    }

    @Step("Проверка правок")
    public Boolean isEdit(String response, String check) {
        boolean isEdit = false;
        if (!response.equals(check)) {
            isEdit = true;
        }
        return isEdit;
    }
}
