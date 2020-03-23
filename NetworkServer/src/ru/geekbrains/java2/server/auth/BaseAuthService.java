package ru.geekbrains.java2.server.auth;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    private static class UserData {
        private String login;
        private String password;
        private String username;

        public UserData(String login, String password, String username) {
            this.login = login;
            this.password = password;
            this.username = username;
        }
    }

    private static List<UserData> USER_DATA = new ArrayList<>();
    {
        USER_DATA.add(new UserData("login1", "pass1", "username1"));
        USER_DATA.add(new UserData("login2", "pass2", "username2"));
        USER_DATA.add(new UserData("login3", "pass3", "username3"));
        USER_DATA.add(new UserData("root", "admin", "Admin"));
        USER_DATA.add(new UserData("belka", "belkapass", "Belka-Skret"));
        USER_DATA.add(new UserData("alex", "alexpass", "Revoc"));
        USER_DATA.add(new UserData("jackwizard", "jackwizardpass", "JackWizard"));
    }

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        for (UserData userDatum : USER_DATA) {
            if (userDatum.login.equals(login) && userDatum.password.equals(password)) {
                return userDatum.username;
            }
        }
        return null;
    }

    @Override
    public void start() {
        System.out.println("Auth service started");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");
    }
}
