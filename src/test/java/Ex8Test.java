import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Ex8Test {

    @Test
    void checkToken(){

        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then()
                .extract().response();

        TokenDtoResponse tokenDto = responseToDto(response);

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());

        Response response2 = RestAssured
                .given()
                .params(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then()
                .extract().response();

        TokenDtoNotReady dtoNotReady = notReadyDto(response2);

        int seconds = Integer.parseInt(tokenDto.getSeconds()) * 1000;

        if (dtoNotReady.getStatus().equals("Job is NOT ready")){
            try {
                Thread.sleep(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Response response3 = RestAssured
                .given()
                .params(params)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then()
                .extract().response();

        TokenDtoReady dtoReady = readyDto(response3);

        if(response3.statusCode() == 200){
            System.out.println("result = " + dtoReady.getResult() + ", " + "status = " + dtoReady.getStatus());
        }
    }

    public TokenDtoResponse responseToDto(Response response){
        return response.as(TokenDtoResponse.class);
    }

    public TokenDtoNotReady notReadyDto(Response response){
        return response.as(TokenDtoNotReady.class);
    }

    public TokenDtoReady readyDto(Response response){
        return response.as(TokenDtoReady.class);
    }

    @Getter
    @Setter
    public static class TokenDtoResponse {
        private String token;
        private String seconds;
    }

    @Getter
    @Setter
    public static class TokenDtoNotReady {
        private String status;
    }

    @Getter
    @Setter
    public static class TokenDtoReady {
        private String result;
        private String status;
    }
}
