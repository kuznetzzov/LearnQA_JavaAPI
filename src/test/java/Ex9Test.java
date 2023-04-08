import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.path.xml.XmlPath.CompatibilityMode.HTML;

public class Ex9Test {

    @Test
    void passwordSelection() throws IOException {

        ArrayList<String> pws = nonRecurringPasswords(passwords());
        Map<String, String> params = new HashMap<>();
        params.put("login", "super_admin");

        for (String pw : pws) {
            params.put("password", pw);

            Response response = RestAssured
                    .given()
                    .body(params)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String responseCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();

            Assertions.assertNotEquals(responseCookie, null, "Cookie should not be NULL. Current password: " + pw);

            cookies.put("auth_cookie", responseCookie);

            Response response2 = RestAssured
                    .given()
                    .body(params)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            System.out.println(response2.asString());
            if (!response2.asString().equals("You are NOT authorized")) {
                System.out.println("password = " + pw + ", message - " + response2.asString());
                break;
            }
        }
    }

    public ArrayList<String> passwords() throws IOException {
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords").get();
        Element table = doc.select("table").get(1);

        ArrayList<String> pws = new ArrayList<>();

        Elements rows = table.select("tr");
        Elements cols;

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            cols = row.select("td");
            pws.add(cols.get(1).text());
            pws.add(cols.get(2).text());
            pws.add(cols.get(3).text());
            pws.add(cols.get(4).text());
            pws.add(cols.get(5).text());
            pws.add(cols.get(6).text());
            pws.add(cols.get(7).text());
            pws.add(cols.get(8).text());
            pws.add(cols.get(9).text());
        }

        System.out.println("Список повторяющихся паролей из таблицы Top 25 most common passwords by year according to SplashData:  " + pws);

        return pws;
    }

    public ArrayList<String> nonRecurringPasswords(ArrayList<String> passwords){
        ArrayList<String> pws = new ArrayList<>();

        for (String password : passwords) {
            if(!pws.contains(password)){
                pws.add(password);
            }
        }
        System.out.println("Список неповторяющихся паролей из таблицы Top 25 most common passwords by year according to SplashData:  " + pws);
        return pws;
    }

//    @Test
//    void checkArray() throws IOException {
//        nonRecurringPasswords(passwords());
//    }
}
