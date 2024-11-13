package ru.jackwizard88.destktopmessenger.commands.command;

import java.io.Serializable;

public class RegisterCommand implements Serializable {
    private final String login;
    private final String password;
    private String username;

    public RegisterCommand(String login, String password, String nickname) {
        this.login = login;
        this.password = password;
        this.username = nickname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
