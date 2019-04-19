package speller.cases;



import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.apache.http.HttpStatus;
import org.junit.Test;

import speller.utils.SpellerConstants;

import java.util.Arrays;

import static org.hamcrest.Matchers.lessThan;
import static speller.utils.SpellerConstants.*;
import static org.hamcrest.Matchers.*;

public class YandexSpellerJsonTest {

    @Test
    public void simpleSpellerApiCall() {
        RestAssured
                .given()
                .queryParam(SpellerConstants.PARAM_TEXT, "moother")
                .params(SpellerConstants.PARAM_LANG, Language.EN, "CustomParameter", "valueOfParam")
                .accept(ContentType.JSON)
                .auth().basic("any", "any")
                .header("custom header1", "header1.value")
                .and()
                .body("some body lol")
                .log().everything()
                .get(SpellerConstants.YANDEX_SPELLER_API_URI)
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(allOf(
                        stringContainsInOrder(Arrays.asList("moother",
                                "other")),
                        containsString("\"code\":1")))
                .contentType(ContentType.JSON)
                .time(lessThan(20000L)); // Milliseconds
    }
}
