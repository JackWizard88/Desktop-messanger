package ru.jackwizard88.destktopmessenger.commands.command;

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
