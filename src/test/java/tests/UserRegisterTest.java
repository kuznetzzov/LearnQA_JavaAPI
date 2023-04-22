package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Создание пользователя с некорректным email - без символа @")
    public void checkCreateUserWithIncorrectEmail() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "nosobakagmail.com");
        userData.put("password", "qwerty123");
        userData.put("username", "ivan");
        userData.put("firstName", "Ivan");
        userData.put("lastName", "Vanin");

        Response response = apiCoreRequests.createUser(userData);

        Assertions.assertEquals(400, response.getStatusCode(),
                "Код ответа сервера не соотвествует ожидаемому" + response.getStatusCode());
        Assertions.assertEquals("Invalid email format", response.asString(),
                "Ошибка тестирования создания пользователя с некорректным email" + response.asString());
    }

    @ParameterizedTest
    @CsvSource({
            "email, , password, 123, username, maxKuznetsov, firstName, max, lastName, kuznetsov",
            "email, max@gmail.com, password, , username, maxKuznetsov, firstName, max, lastName, kuznetsov",
            "email, max@gmail.com, password, 123, username, , firstName, max, lastName, kuznetsov",
            "email, max@gmail.com, password, 123, username, maxKuznetsov, firstName, , lastName, kuznetsov",
            "email, max@gmail.com, password, 123, username, maxKuznetsov, firstName, max, lastName, "
    })
    @DisplayName("Создание пользователя без указания одного из полей")
    public void checkCreateUserWithoutOneFieldValue(String key, String value,
                                                    String key2, String value2,
                                                    String key3, String value3,
                                                    String key4, String value4,
                                                    String key5, String value5) {
        Map<String, String> userData = new HashMap<>();
        userData.put(key, value);
        userData.put(key2, value2);
        userData.put(key3, value3);
        userData.put(key4, value4);
        userData.put(key5, value5);

        Response response = apiCoreRequests.createUser(userData);

        Assertions.assertEquals(400, response.getStatusCode(),
                "Код ответа сервера не соотвествует ожидаемому" + response.getStatusCode());
        Assertions.assertTrue(apiCoreRequests.isPhrase(response.asString()),
                "Пользователь создается с одним отсутствующим полем" + response.asString());
    }

    @ParameterizedTest
    @CsvSource({
            "email, max@gmail.com, password, 123, username, maxKuznetsov, firstName, a, lastName, kuznetsov"
    })
    @DisplayName("Создание пользователя с очень коротким именем в один символ")
    public void checkCreateUserWithShortFirstName(String key, String value,
                                                  String key2, String value2,
                                                  String key3, String value3,
                                                  String key4, String value4,
                                                  String key5, String value5) {
        Map<String, String> userData = new HashMap<>();
        userData.put(key, value);
        userData.put(key2, value2);
        userData.put(key3, value3);
        userData.put(key4, value4);
        userData.put(key5, value5);

        Response response = apiCoreRequests.createUser(userData);

        Assertions.assertEquals(400, response.getStatusCode(),
                "Код ответа сервера не соотвествует ожидаемому" + response.getStatusCode());
        Assertions.assertEquals("The value of 'firstName' field is too short", response.asString(),
                "Ошибка создания пользователя с очень коротким именем в один символ" + response.asString());
    }

    @Test
    @DisplayName("Создание пользователя с очень длинным именем - длиннее 250 символов")
    public void checkCreateUserWithLongFirstName() {

        String firstName = "FirstNamefffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "ffffffffffffffffffffffffffffffnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", "nosobakagmail.com");
        userData.put("password", "qwerty123");
        userData.put("username", "ivan");
        userData.put("firstName", firstName);
        userData.put("lastName", "Vanin");

        Response response = apiCoreRequests.createUser(userData);

        Assertions.assertEquals(400, response.getStatusCode(),
                "Код ответа сервера не соотвествует ожидаемому" + response.getStatusCode());
        Assertions.assertEquals("The value of 'firstName' field is too long", response.asString(),
                "Ошибка создания пользователя с очень длинным именем в один символ" + response.asString());
    }
}
