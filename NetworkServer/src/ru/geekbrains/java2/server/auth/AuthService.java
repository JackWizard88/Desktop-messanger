package ru.geekbrains.java2.server.auth;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password);

    void start();

    void stop();

    void changeNickName(String login, String newNickname);

    void logOut(String login);

    void registerNewUser(String login, String password, String nickname);
}
