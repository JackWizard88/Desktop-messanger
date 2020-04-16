package ru.geekbrains.java2.commands;

import java.io.Serializable;

public class InfoCommand implements Serializable {

    private final String infoMessage;

    public InfoCommand(String infoMessage) {
            this.infoMessage = infoMessage;
        }

        public String getInfoMessage() {
            return infoMessage;
        }
}
