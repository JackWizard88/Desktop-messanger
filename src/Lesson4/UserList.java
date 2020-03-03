package Lesson4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class UserList {
    private static ArrayList<String> users = new ArrayList<>();

    public static void add(String name) {
        users.add(name);
    }

    public static ObservableList getUserList() {
        ObservableList<String> userList = FXCollections.observableList(users);
        return userList;
    }
}
