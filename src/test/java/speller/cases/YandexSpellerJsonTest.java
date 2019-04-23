package speller.cases;



import beans.YandexSpellerAnswer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import speller.core.YandexSpellerApi;
import speller.utils.Options;
import speller.utils.SpellerConstants;
import speller.utils.Utils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertTrue;
import static speller.utils.SpellerConstants.*;
import static org.hamcrest.Matchers.*;
import static speller.testData.TestYandexData.*;
import static speller.core.YandexSpellerApi.succesResponse;

public class YandexSpellerJsonTest {

    private Utils utils;

    @Before
    public void setUp() {
        utils = new Utils();
    }

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
    public void tc1_wrongWordEn() {
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
    public void tc2_capitalLetterEn() {
        YandexSpellerApi.with()
                .text(WRONG_CAPITAL_EN)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_CAPITAL_EN,
                                CORRECT_CAPITAL_EN)),
                        containsString("\"code\":3")));
    }

    @Test
    public void tc3_wordWithDigitsEn() {
        YandexSpellerApi.with()
                .text(WRONG_DIGTIS_EN)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_DIGTIS_EN,
                                CORRECT_DIGTIS_EN)),
                        containsString("\"code\":1")));
    }

    @Test
    public void tc4_wordWithDigitsIgnoreEn() {
        List<YandexSpellerAnswer> answers =
                YandexSpellerApi.getYandexSpellerAnswer(
                        YandexSpellerApi.with().text(WRONG_DIGTIS_IGNORE_EN)
                                .language(Language.EN.toString())
                                .option(Options.IGNORE_DIGITS.getCode())
                                .callApi());
        assertTrue(utils.verifyJsonResponse(answers,"s",CORRECT_DIGTIS_IGNORE_EN));
    }

    @Test
    public void tc5_capitalWordEn() {
        YandexSpellerApi.with()
                .text(WRONG_CAPITAL_WORD_EN)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_CAPITAL_WORD_EN,
                                CORRECT_CAPITAL_WORD_EN)),
                        containsString("\"code\":1")));
    }

}
