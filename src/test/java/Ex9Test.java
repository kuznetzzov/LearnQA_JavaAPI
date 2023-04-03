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

        ArrayList<String> pws = passwords();
        Map<String, String> params = new HashMap<>();

        for (String pw : pws) {
            params.put("login", "super_admin");
            params.put("password ", pw);

            Response response = RestAssured
                    .given()
                    .params(params)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .then()
                    .extract()
                    .response();

            Response response2 = RestAssured
                    .given()
                    .params(params)
                    .cookie("auth_cookie", response.getCookies().get("auth_cookie"))
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .then()
                    .extract()
                    .response();

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
}