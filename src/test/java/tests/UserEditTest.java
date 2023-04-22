package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User cases")
@Feature("Edit")
public class UserEditTest {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Попытаемся изменить данные пользователя, будучи неавторизованными")
    @DisplayName("Редактирование без авторизации")
    public void checkEditWithoutAuth() {
        // Генерация данных для создания пользователя
        Map<String, String> userData = apiCoreRequests.userDataGenerator();
        // Запрос на создание пользователя
        Response response = apiCoreRequests.createUser(userData);
        Map userDataDTO = response.getBody().as(Map.class);
        String id = String.valueOf(userDataDTO.get("id"));

        // Изменяемое значение у созданного пользователя
        Map<String, String> userDataPut = new HashMap<>();
        userData.put("firstName", "Changed name");
        // Запрос на редактирование пользователя
        Response editResponse = apiCoreRequests.responsePutEdit(userDataPut, "", "", id);

        Assertions.assertEquals(400, editResponse.getStatusCode(), "Пользователь c id = " + id + " редактируется без авторизации");
    }

    @Test
    @Description("Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем")
    @DisplayName("Редактирование под другой авторизацией")
    public void checkEditWithAnotherUser() {
        // Генерация данных для создания пользователя 1
        Map<String, String> userData1 = apiCoreRequests.userDataGenerator();
        // Генерация данных для создания пользователя 2
        Map<String, String> userData2 = apiCoreRequests.userDataGenerator();

        // Запрос на создание пользователя 1
        Response response1 = apiCoreRequests.createUser(userData1);
        Map userDataDTO1 = response1.getBody().as(Map.class);
        String id1 = String.valueOf(userDataDTO1.get("id"));
        // Запрос на создание пользователя 2
        Response response2 = apiCoreRequests.createUser(userData2);
        Map userDataDTO2 = response2.getBody().as(Map.class);
        String id2 = String.valueOf(userDataDTO2.get("id"));

        // Авторизация пользователя 1
        String email = String.valueOf(userData1.get("email"));
        String password = String.valueOf(userData1.get("password"));

        Map<String, String> authData = new HashMap<>();
        authData.put("email", email);
        authData.put("password", password);

        Response authResponse = apiCoreRequests.responseGetAuth(authData);
        String header = authResponse.headers().get("x-csrf-token").getValue();
        String cookie = authResponse.cookies().get("auth_sid");

        // Авторизация пользователя 2
        String email2 = String.valueOf(userData2.get("email"));
        String password2 = String.valueOf(userData2.get("password"));

        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", email2);
        authData2.put("password", password2);

        Response authResponse2 = apiCoreRequests.responseGetAuth(authData2);
        String header2 = authResponse2.headers().get("x-csrf-token").getValue();
        String cookie2 = authResponse2.cookies().get("auth_sid");

        // Изменяемое значение у созданного пользователя 1
        String newName = "Changed name";
        Map<String, String> userDataEdit = new HashMap<>();
        userDataEdit.put("firstName", newName);

        // Запрос на редактирование пользователя
        Response editResponse = apiCoreRequests.responsePutEdit(userDataEdit, header, cookie, id2);

        // Получение данных отредактированного пользователя
        Response getUserAfterEdit2 = apiCoreRequests.responseUserData(header2, cookie2, id2);
        Map checkedMap = getUserAfterEdit2.getBody().as(Map.class);
        String checkedFirstName = String.valueOf(checkedMap.get("firstName"));

        Assertions.assertTrue(apiCoreRequests.isEdit(newName, checkedFirstName), "Пользователь с id = " + id2 + "редактируется без авторизации");
    }

    @Test
    @Description("Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @")
    @DisplayName("Редактирование не корректного email")
    public void checkEditEmailWithoutDogSign() {
        // Генерация данных для создания пользователя
        Map<String, String> userData = apiCoreRequests.userDataGenerator();
        // Запрос на создание пользователя
        Response response = apiCoreRequests.createUser(userData);
        Map userDataDTO = response.getBody().as(Map.class);
        String id = String.valueOf(userDataDTO.get("id"));
        // Авторизация пользователя
        String email = String.valueOf(userData.get("email"));
        String password = String.valueOf(userData.get("password"));
        Map<String, String> authData = new HashMap<>();
        authData.put("email", email);
        authData.put("password", password);
        Response authResponse = apiCoreRequests.responseGetAuth(authData);
        String header = authResponse.headers().get("x-csrf-token").getValue();
        String cookie = authResponse.cookies().get("auth_sid");

        // Изменяемое значение у созданного пользователя
        String newEmail = "maxgmail.com";
        Map<String, String> userDataEdit = new HashMap<>();
        userDataEdit.put("email", newEmail);
        // Запрос на редактирование пользователя
        Response editResponse = apiCoreRequests.responsePutEdit(userDataEdit, header, cookie, id);

        Assertions.assertEquals("Invalid email format", editResponse.asString(), "Для пользователя с id = " + id + " изменился email без @");
    }

    @Test
    @Description("Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")
    @DisplayName("Редактирование короткого firstName")
    public void checkEditWithShortFirstName() {
        // Генерация данных для создания пользователя
        Map<String, String> userData = apiCoreRequests.userDataGenerator();
        // Запрос на создание пользователя
        Response response = apiCoreRequests.createUser(userData);
        Map userDataDTO = response.getBody().as(Map.class);
        String id = String.valueOf(userDataDTO.get("id"));
        // Авторизация пользователя
        String email = String.valueOf(userData.get("email"));
        String password = String.valueOf(userData.get("password"));
        Map<String, String> authData = new HashMap<>();
        authData.put("email", email);
        authData.put("password", password);
        Response authResponse = apiCoreRequests.responseGetAuth(authData);
        String header = authResponse.headers().get("x-csrf-token").getValue();
        String cookie = authResponse.cookies().get("auth_sid");

        // Изменяемое значение у созданного пользователя
        String newFirstName = "m";
        Map<String, String> userDataEdit = new HashMap<>();
        userDataEdit.put("firstName", newFirstName);
        // Запрос на редактирование пользователя
        Response editResponse = apiCoreRequests.responsePutEdit(userDataEdit, header, cookie, id);
        Map errors = editResponse.getBody().as(Map.class);
        String error = String.valueOf(errors.get("error"));
        Assertions.assertEquals("Too short value for field firstName", error, "Для пользователя с id = " + id + " изменилось firstName на слишком короткое");
    }

}
