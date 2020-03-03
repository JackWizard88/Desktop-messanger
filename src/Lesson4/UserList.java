package Lesson4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class UserList {
    private static ArrayList<String> users = new ArrayList<>();
    private static String activeUser;

    public static void add(User name) {
        users.add(name.toString());
    }

    public static ObservableList getUserList() {
        ObservableList<String> userList = FXCollections.observableList(users);
        return userList;
    }

    public static void setActiveUser(String user) {
        activeUser = user.toString();
    }

    public static String getActiveUser() {
        return activeUser;
    }
}
