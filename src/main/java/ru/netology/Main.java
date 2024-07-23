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

        Thread thread3 = new Thread(() -> checkBeautifulWords(texts, 3, beautifulCount3));
        Thread thread4 = new Thread(() -> checkBeautifulWords(texts, 4, beautifulCount4));
        Thread thread5 = new Thread(() -> checkBeautifulWords(texts, 5, beautifulCount5));

        thread3.start();
        thread4.start();
        thread5.start();

        try {
            thread3.join();
            thread4.join();
            thread5.join();
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

    public static void checkBeautifulWords(String[] texts, int length, AtomicInteger counter) {
        for (String word : texts) {
            if (word.length() == length && (isPalindrome(word) || isSingleLetter(word) || isSorted(word))) {
                counter.incrementAndGet();
            }
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