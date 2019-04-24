package speller.testData;

public class TestYandexData {
    public static String WRONG_WORD = "moother";
    public static String CORRECT_WORD = "mother";

    public static String WRONG_CAPITAL = "london";
    public static String CORRECT_CAPITAL = "London";
    public static String CORRECT_CAPITAL_IGNORE = "london";

    public static String WRONG_DIGTIS = "London77";
    public static String CORRECT_DIGTIS = "London 77";

    public static String WRONG_DIGTIS_IGNORE = "fatherr77\nmotherr";
    public static String CORRECT_DIGTIS_IGNORE = "mother";

    public static String WRONG_CAPITAL_WORD = "MOSCOWW";
    public static String CORRECT_CAPITAL_WORD = "MOSCOW";

    public static String WRONG_WORD_WITH_SYMBOLS = "beeg()";
    public static String CORRECT_WORD_WITH_SYMBOLS = "big";

    public static String WRONG_WORD_WITH_CHAR = "fo0od";
    public static String CORRECT_WORD_WITH_CHAR = "food";

    public static String WRONG_MULTIPLE_WORDS = "food fod food fod";
    public static String WRONG_MULTIPLE_WORD = "fod";
    public static String CORRECT_MULTIPLE_WORD = "food";

    public static String WRONG_WORDS_WITH_CAPITALS = "motherr london moscoww";
}
