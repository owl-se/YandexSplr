package speller.utils;

import beans.YandexSpellerAnswer;

import java.util.List;

public class Utils {

    public boolean verifyJsonResponse(List<YandexSpellerAnswer> answers, String node, String val) {
        boolean r = false;
        for (YandexSpellerAnswer answer: answers) {
            for (int i = 0; i < answer.s.size(); i++) {
                if (answer.s.get(i).equals(val)) {
                    r = true;
                    return r;
                }
            }
        }
        return r;
    }
}
