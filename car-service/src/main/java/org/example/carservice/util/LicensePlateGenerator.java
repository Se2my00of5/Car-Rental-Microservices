package org.example.carservice.util;

import java.util.Random;

public class LicensePlateGenerator {

    private static final String LETTERS = "ABCEHKMOPTXY"; // Похожие на русские
    private static final String REGION_CODE = "77"; // Можно сделать параметром
    private static final Random RANDOM = new Random();

    public static String generateLicensePlateNumber() {
        char firstLetter = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
        int digits = 100 + RANDOM.nextInt(900); // от 100 до 999
        char secondLetter = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
        char thirdLetter = LETTERS.charAt(RANDOM.nextInt(LETTERS.length()));
        return String.format("%c%d%c%c%s", firstLetter, digits, secondLetter, thirdLetter, REGION_CODE);
    }
}