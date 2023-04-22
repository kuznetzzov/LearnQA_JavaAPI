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
@Feature("Delete")
public class UserDeleteTest {

    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Попытку удалить пользователя по ID, после авторизации под другим. Его данные для авторизации: (vinkotov@example.com / 1234)")
    @DisplayName("Удаление пользователя по ID 2")
    public void checkDeleteById() {

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = apiCoreRequests.responseGetAuth(authData);

        String header = authResponse.headers().get("x-csrf-token").getValue();
        String cookie = authResponse.cookies().get("auth_sid");
        String id = "2";

        Response response = apiCoreRequests.responseDelete(authData, header, cookie, id);

        String template = "Please, do not delete test users with ID 1, 2, 3, 4 or 5.";
        Assertions.assertEquals(template, response.asString(), "Ошибка при проверке удаления пользователя по ID 2");
    }

    @Test
    @Description("Создать пользователя, авторизоваться из-под него, удалить, затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален.")
    @DisplayName("Создание, авторизация, удаление, проверка удаления")
    public void checkDeleteUser() {
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

        // Запрос на удаление пользователя
        Response deleteResponse = apiCoreRequests.responseDelete(authData, header, cookie, id);
        // Проверка удаления
        Response getUserAfterEdit = apiCoreRequests.responseUserData(header, cookie, id);

        Assertions.assertEquals("User not found", getUserAfterEdit.asString(), "Пользователь id = " + id + " не удалился");
    }

    @Test
    @Description("Негативный кейс, попробовать удалить пользователя, будучи авторизованными другим пользователем.")
    @DisplayName("Проверка удаления пользователя")
    public void checkDeleteAnotherUser() {
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
        String assertId = id2;

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

        // Запрос на удаление пользователя
        Response deleteResponse = apiCoreRequests.responseDelete(userDataEdit, header, cookie, id2);

        // Получение данных пользователя, которого пытались удалить
        Response getUserAfterEdit2 = apiCoreRequests.responseUserData(header2, cookie2, id2);
        Map checkedMap = getUserAfterEdit2.getBody().as(Map.class);
        String checkedId = String.valueOf(checkedMap.get("id"));

        System.out.println(checkedId);

        Assertions.assertNotNull(checkedId, "Пользователь id = " + assertId + " удалился без авторизации");
    }
}
