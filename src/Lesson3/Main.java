package Lesson3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        wordCounter();

        System.out.println();

        PhoneBook.getInstance();
        PhoneBook.add("Ivanov",  "890999911111");
        PhoneBook.add("Petrov",  "890999922222");
        PhoneBook.add("Sidorov",  "8909993333");
        PhoneBook.add("Petrov",  "890999944449");
        PhoneBook.add("Petrov",  "8909999555559");
        PhoneBook.add("Sidorov",  "8909999666669");
        PhoneBook.add("Sidorov",  "890999977779");
        PhoneBook.add("Sidorov",  "89099999777669");
        PhoneBook.add("Ivanov",  "8909999565699");
        PhoneBook.add("Ivanov",  "890999934569");
        PhoneBook.add("Ivanov",  "8909999345639");

        PhoneBook.get("Petrov");

    }


    //метод по 1 пункту ТЗ
    public static void wordCounter() {
        List<String> wordList = new ArrayList<>();
        wordList.add("Арбуз");
        wordList.add("Абрикос");
        wordList.add("Персик");
        wordList.add("Дыня");
        wordList.add("Арбуз");
        wordList.add("Киви");
        wordList.add("Дыня");
        wordList.add("Дыня");
        wordList.add("Персик");
        wordList.add("Арбуз");
        wordList.add("Манго");
        wordList.add("Виноград");
        wordList.add("Киви");
        wordList.add("Персик");
        wordList.add("Киви");
        wordList.add("Киви");
        wordList.add("Персик");
        wordList.add("Киви");
        wordList.add("Виноград");
        wordList.add("Абрикос");
        System.out.println(wordList);

        Map<String, Integer> wordMap = new HashMap<>();
        for (String word: wordList) {
            if (wordMap.containsKey(word)) {
                wordMap.put(word, wordMap.get(word) + 1);
            } else {
                wordMap.put(word, 1);
            }
        }
        System.out.println(wordMap);
    }
}
