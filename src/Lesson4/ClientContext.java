package Lesson4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ClientContext {
    private static ArrayList<String> users = new ArrayList<>();
    private static String activeUser;

    public static void add(User user) {
        users.add(user.toString());
    }

    public static ObservableList<String> getUserList() {
        return FXCollections.observableList(users);
    }

    public static void setActiveUser(String user) {
        activeUser = user;
    }

    public static String getActiveUser() {
        return activeUser;
    }
}
