package ru.geekbrains.java2.commands;

import ru.geekbrains.java2.commands.command.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

        private CTypeEnum type;
        private Object data;

        public Object getData() {
            return data;
        };

        public CTypeEnum getType() {
            return type;
        }

        public static Command authCommand(String login, String password) {
            Command command = new Command();
            command.type = CTypeEnum.AUTH;
            command.data = new AuthCommand(login, password);
            return command;
        }

        public static Command registerCommand(String login, String password, String nickname) {
            Command command = new Command();
            command.type = CTypeEnum.REGISTER;
            command.data = new RegisterCommand(login, password, nickname);
            return command;
        }

        public static Command changeNicknameCommand(String login, String nickname) {
            Command command = new Command();
            command.type = CTypeEnum.CHANGE_NICKNAME;
            command.data = new ChangeNicknameCommand(login, nickname);
            return command;
        }

        public static Command errorCommand(String errorMessage) {
            Command command = new Command();
            command.type = CTypeEnum.ERROR;
            command.data = new ErrorCommand(errorMessage);
            return command;
        }

        public static Command messageCommand(String username, String message, String acceptor) {
            Command command = new Command();
            command.type = CTypeEnum.MESSAGE;
            command.data = new MessageCommand(username, message, acceptor);
            return command;
        }

        public static Command updateUsersListCommand(List<String> users) {
            Command command = new Command();
            command.type = CTypeEnum.UPDATE_USERS_LIST;
            command.data = new UpdateUsersListCommand(users);
            return command;
        }
}

