package ru.geekbrains.java2.commands.command;

import java.io.Serializable;

public class ChangeNicknameCommand implements Serializable {

    private final String login;
    private final String username;

    public ChangeNicknameCommand(String login, String username) {
        this.login = login;
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public String getUsername() {
        return username;
    }

}
