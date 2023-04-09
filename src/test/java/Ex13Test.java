import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ex13Test {


    @ParameterizedTest
    @ValueSource(strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"
    })
    public void checkUserAgent(String userAgent) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("User-Agent", userAgent);

        JsonPath response = RestAssured
                .given()
                .headers(queryParams)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        final String ua1 = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        final String ua2 = "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1";
        final String ua3 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        final String ua4 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0";
        final String ua5 = "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

        String platform = response.getString("platform");
        String browser = response.getString("browser");
        String device = response.getString("device");

        String totalResponse = "";
        // Список неправилньных параметров
        Map<String, String> falseValues = new HashMap<>();

        switch (userAgent) {
            case (ua1):
                if (!platform.equals("Mobile")) {
                    falseValues.put("platform", platform);
                }
                if (!browser.equals("No")) {
                    falseValues.put("browser", browser);
                }
                if (!device.equals("Android")) {
                    falseValues.put("device", device);
                }
                if (falseValues.size() > 0) {
                    totalResponse = (userAgent + " - неправильный параметр / параметры: " + falseValues);
                }
                break;
            case (ua2):
                if (!platform.equals("Mobile")) {
                    falseValues.put("platform", platform);
                }
                if (!browser.equals("Chrome")) {
                    falseValues.put("browser", browser);
                }
                if (!device.equals("iOS")) {
                    falseValues.put("device", device);
                }
                if (falseValues.size() > 0) {
                    totalResponse = (userAgent + " - неправильный параметр / параметры: " + falseValues);
                }
                break;
            case (ua3):
                if (!platform.equals("Googlebot")) {
                    falseValues.put("platform", platform);
                }
                if (!browser.equals("Unknown")) {
                    falseValues.put("browser", browser);
                }
                if (!device.equals("Unknown")) {
                    falseValues.put("device", device);
                }
                if (falseValues.size() > 0) {
                    totalResponse = (userAgent + " - неправильный параметр / параметры: " + falseValues);
                }
                break;
            case (ua4):
                if (!platform.equals("Web")) {
                    falseValues.put("platform", platform);
                }
                if (!browser.equals("Chrome")) {
                    falseValues.put("browser", browser);
                }
                if (!device.equals("No")) {
                    falseValues.put("device", device);
                }
                if (falseValues.size() > 0) {
                    totalResponse = (userAgent + " - неправильный параметр / параметры: " + falseValues);
                }
                break;
            case (ua5):
                if (!platform.equals("Mobile")) {
                    falseValues.put("platform", platform);
                }
                if (!browser.equals("No")) {
                    falseValues.put("browser", browser);
                }
                if (!device.equals("iPhone")) {
                    falseValues.put("device", device);
                }
                if (falseValues.size() > 0) {
                    totalResponse = (userAgent + " - неправильный параметр / параметры: " + falseValues);
                }
                break;
            default:
                System.out.println("Заполните поле User-Agent");
                break;
        }

        System.out.println(totalResponse);
    }


}
