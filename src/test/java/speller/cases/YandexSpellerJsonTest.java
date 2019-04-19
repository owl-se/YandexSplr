package speller.cases;



import beans.YandexSpellerAnswer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.apache.http.HttpStatus;
import org.junit.Test;

import speller.core.YandexSpellerApi;
import speller.utils.SpellerConstants;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.lessThan;
import static speller.utils.SpellerConstants.*;
import static org.hamcrest.Matchers.*;
import static speller.testData.TestYandexData.*;
import static speller.core.YandexSpellerApi.succesResponse;

public class YandexSpellerJsonTest {

    @Test()
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

    @Test
    public void wrongWordEn() {
        YandexSpellerApi.with()
                .text(WRONG_WORD_EN)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_WORD_EN,
                                "other","mother","smoother")),
                        containsString("\"code\":1"),
                        containsString("\"len\":7")));
    }

    @Test
    public void wrongWordRu() {

        RestAssured
                .given((YandexSpellerApi.baseRequestConf()))
                        .param(SpellerConstants.PARAM_TEXT, WRONG_WORD_RU)
                        .get().prettyPeek()
                        .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(allOf(
                        containsString("\"code\":1"),
                        containsString(CORRECT_WORD_RU)
                ));



/*        YandexSpellerApi.with()
                .text(WRONG_WORD_RU)
                .language(Language.RU.toString())
                .callApi()
                .then().specification(succesResponse())
                .assertThat()
                .contentType(ContentType.JSON)
                .body(allOf(
                        containsString("\"code\":1"),
                        containsString(CORRECT_WORD_RU)
                        ));*/
    }
}
