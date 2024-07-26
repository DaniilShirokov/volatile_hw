package ru.netology;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
public class Main {
    private static final int WORD_COUNT = 100_000;
    private static final String LETTERS = "abc";

    private static final AtomicInteger beautifulCount3 = new AtomicInteger();
    private static final AtomicInteger beautifulCount4 = new AtomicInteger();
    private static final AtomicInteger beautifulCount5 = new AtomicInteger();

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[WORD_COUNT];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(LETTERS, 3 + random.nextInt(3));
        }

        Thread palindromeThread = new Thread(() -> checkPalindromes(texts));
        Thread singleLetterThread = new Thread(() -> checkSingleLetters(texts));
        Thread sortedThread = new Thread(() -> checkSorted(texts));

        palindromeThread.start();
        singleLetterThread.start();
        sortedThread.start();

        try {
            palindromeThread.join();
            singleLetterThread.join();
            sortedThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.printf("Красивых слов с длиной 3: %d шт%n", beautifulCount3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт%n", beautifulCount4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт%n", beautifulCount5.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void checkPalindromes(String[] texts) {
        for (String word : texts) {
            if (isPalindrome(word)) {
                incrementCountByLength(word);
            }
        }
    }

    public static void checkSingleLetters(String[] texts) {
        for (String word : texts) {
            if (isSingleLetter(word)) {
                incrementCountByLength(word);
            }
        }
    }

    public static void checkSorted(String[] texts) {
        for (String word : texts) {
            if (isSorted(word)) {
                incrementCountByLength(word);
            }
        }
    }

    private static void incrementCountByLength(String word) {
        switch (word.length()) {
            case 3:
                beautifulCount3.incrementAndGet();
                break;
            case 4:
                beautifulCount4.incrementAndGet();
                break;
            case 5:
                beautifulCount5.incrementAndGet();
                break;
        }
    }

    private static boolean isPalindrome(String word) {
        int left = 0, right = word.length() - 1;
        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    private static boolean isSingleLetter(String word) {
        char firstChar = word.charAt(0);
        for (char c : word.toCharArray()) {
            if (c != firstChar) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSorted(String word) {
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i - 1) > word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}