package speller.utils;

public class SpellerConstants {

    public static final String YANDEX_SPELLER_API_URI =
            "https://speller.yandex.net/services/spellservice.json/checkText";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTION = "options";
    public static final String PARAM_LANG = "lang";
    public static final String QUOTES = "\"";

    public enum Language {
        RU("ru"),
        UK("uk"),
        EN("en");
        public String languageCode;

        private Language(String lang) {
            this.languageCode = lang;
        }
    }
}
