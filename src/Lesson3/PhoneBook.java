package Lesson3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PhoneBook {
    private static PhoneBook INSTANCE;
    private static HashMap<String, String> phoneBook;

    private PhoneBook() {
        phoneBook = new HashMap<>();
    }

    public static PhoneBook getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhoneBook();
        }

        return INSTANCE;
    }

    public static void add(String name, String number) {
        phoneBook.put(number, name);
    }

    public static void get(String name) {
        Iterator<Map.Entry<String, String>> iterator = phoneBook.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if ((entry.getValue()).equals(name)) {
                System.out.println(entry.getValue() + " : "+ entry.getKey());
            }
        }
    }



}
