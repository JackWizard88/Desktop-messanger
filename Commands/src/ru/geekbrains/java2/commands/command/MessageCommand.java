package ru.geekbrains.java2.commands.command;

import java.io.Serializable;

public class MessageCommand implements Serializable {

    private final String username;
    private final String message;
    private final String receiver;


    public MessageCommand(String username, String message, String receiver) {
        this.username = username;
        this.message = message;
        this.receiver = receiver;

    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getReceiver() {
        return receiver;
    }
}
