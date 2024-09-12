package com.gym.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final int START_NUMERALS_INTERVAL = 48;
    private static final int END_NUMERALS_INTERVAL = 57;
    private static final int START_BIG_LETTERS_INTERVAL = 65;
    private static final int END_BIG_LETTERS_INTERVAL = 90;
    private static final int START_SMALL_LETTERS_INTERVAL = 97;
    private static final int END_SMALL_LETTERS_INTERVAL = 122;

    public static String generateRandomString(int length){
        Random random = new Random();

        return random.ints(START_NUMERALS_INTERVAL, END_SMALL_LETTERS_INTERVAL + 1)
                .filter(i -> (i <= END_NUMERALS_INTERVAL || i >= START_BIG_LETTERS_INTERVAL) &&
                        (i <= END_BIG_LETTERS_INTERVAL || i >= START_SMALL_LETTERS_INTERVAL))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static boolean isMatch(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
