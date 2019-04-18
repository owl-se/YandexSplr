package speller.core;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import speller.utils.SpellerConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerApi {

    private YandexSpellerApi() {}

    private HashMap<String, String> params = new HashMap<String, String>();

    public static class ApiBuilder {
        YandexSpellerApi yandexSpellerApi;

        private ApiBuilder(YandexSpellerApi api) {
            yandexSpellerApi = api;
        }

        public ApiBuilder text(String text) {
            yandexSpellerApi.params.put(SpellerConstants.PARAM_TEXT, text);
            return this;
        }

        public ApiBuilder option(String option) {
            yandexSpellerApi.params.put(SpellerConstants.PARAM_OPTION, option);
            return this;
        }

        public ApiBuilder language(String lang) {
            yandexSpellerApi.params.put(SpellerConstants.PARAM_LANG, lang);
            return this;
        }

        public Response callApi() {
            return RestAssured.with()
                    .queryParam(yandexSpellerApi.params.toString())
                    .log().all()
                    .get(SpellerConstants.YANDEX_SPELLER_API_URI).prettyPeek();
        }
    }

    public static ApiBuilder with() {
        YandexSpellerApi api = new YandexSpellerApi();
        return new ApiBuilder(api);
    }

   public static List<YandexSpellerAnswer> getYandexSpellerAnswer(Response response) {
        return new Gson().fromJson(response.asString().trim(),
                new TypeToken<List<YandexSpellerAnswer>>(){}.getType());
   }

   public static ResponseSpecification succesResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
   }

   public static RequestSpecification baseRequestConf() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .setRelaxedHTTPSValidation()
                .addHeader("custom header2", "header2.value")
                .addQueryParam("requestID", new Random().nextLong())
                .setBaseUri(SpellerConstants.YANDEX_SPELLER_API_URI)
                .build();
   }
}
