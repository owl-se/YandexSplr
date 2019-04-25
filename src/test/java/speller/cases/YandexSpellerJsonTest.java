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
    public void tc1_wrongWord() {
        YandexSpellerApi.with()
                .text(WRONG_WORD)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_WORD,
                                "other","mother","smoother")),
                        containsString("\"code\":1"),
                        containsString("\"len\":7")));
    }

    @Test
    public void tc2_capitalLetterEn() {
        YandexSpellerApi.with()
                .text(WRONG_CAPITAL)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_CAPITAL,
                                CORRECT_CAPITAL)),
                        containsString("\"code\":3")));
    }

    @Test
    public void tc3_wordWithDigitsEn() {
        YandexSpellerApi.with()
                .text(WRONG_DIGTIS)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_DIGTIS,
                                CORRECT_DIGTIS)),
                        containsString("\"code\":1")));
    }

    @Test
    public void tc4_wordWithDigitsIgnoreEn() {
        List<YandexSpellerAnswer> answers =
                YandexSpellerApi.getYandexSpellerAnswer(
                        YandexSpellerApi.with().text(WRONG_DIGTIS_IGNORE)
                                .language(Language.EN.toString())
                                .option(Options.IGNORE_DIGITS.getCode())
                                .callApi());
        assertTrue(utils.verifyJsonResponse(answers,"s",CORRECT_DIGTIS_IGNORE));
    }

    @Test
    public void tc5_capitalWordEn() {
        YandexSpellerApi.with()
                .text(WRONG_CAPITAL_WORD)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_CAPITAL_WORD,
                                CORRECT_CAPITAL_WORD)),
                        containsString("\"code\":1")));
    }

    @Test
    public void tc6_wordWithSymbols() {
        YandexSpellerApi.with()
                .text(WRONG_WORD_WITH_SYMBOLS)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_WORD_WITH_SYMBOLS,
                                CORRECT_WORD_WITH_SYMBOLS)),
                        containsString("\"code\":1")));
    }

    @Test
    public void tc7_wordWithReplacedChar() {
        YandexSpellerApi.with()
                .text(WRONG_WORD_WITH_CHAR)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_WORD_WITH_CHAR,
                                CORRECT_WORD_WITH_CHAR)),
                        containsString("\"code\":1")));
    }

    @Test
    public void tc8_capitalLetterIgnore() {
        YandexSpellerApi.with()
                .text(WRONG_CAPITAL)
                .language(Language.EN.toString())
                .option(Options.IGNORE_CAPITALIZATION.getCode())
                .callApi()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void tc9_multipleWords() {
        YandexSpellerApi.with()
                .text(WRONG_MULTIPLE_WORDS)
                .language(Language.RU.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList(WRONG_MULTIPLE_WORD,
                                CORRECT_MULTIPLE_WORD)),
                        containsString("\"code\":1"),
                        containsString("\"pos\":5")),
                        stringContainsInOrder(Arrays.asList(WRONG_MULTIPLE_WORD,
                                CORRECT_MULTIPLE_WORD)),
                        containsString("\"code\":1"),
                        containsString("\"pos\":14"));
    }

    @Test
    public void tc_10_multipleWordsWithCapital() {
        YandexSpellerApi.with()
                .text(WRONG_WORDS_WITH_CAPITALS)
                .language(Language.EN.toString())
                .callApi()
                .then()
                .assertThat()
                .body(allOf(
                        stringContainsInOrder(Arrays.asList("motherr",
                                CORRECT_WORD)),
                        containsString("\"code\":1"),
                        stringContainsInOrder(Arrays.asList("london",
                                CORRECT_CAPITAL)),
                        containsString("\"code\":3"),
                        stringContainsInOrder(Arrays.asList("moscoww",
                                "moscow")),
                        containsString("\"code\":1")));
    }

}
