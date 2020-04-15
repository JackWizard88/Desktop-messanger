package ru.geekbrains.java2.client.model;

import javafx.application.Platform;
import ru.geekbrains.java2.client.controller.AuthEvent;
import ru.geekbrains.java2.client.controller.ClientController;
import ru.geekbrains.java2.client.controller.fxview.FxChatWindow;
import ru.geekbrains.java2.commands.Command;
import ru.geekbrains.java2.commands.InfoCommand;
import ru.geekbrains.java2.commands.command.AuthCommand;
import ru.geekbrains.java2.commands.command.ErrorCommand;
import ru.geekbrains.java2.commands.command.MessageCommand;
import ru.geekbrains.java2.commands.command.UpdateUsersListCommand;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class NetworkService {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private FxChatWindow curentWindow;
    private AuthEvent successfulAuthEvent;
    private String nickname;
    private ClientController controller;
    public NetworkService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(ClientController controller) throws IOException {
        this.controller = controller;
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        runReadThread();
    }

    private void runReadThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Command command = (Command) in.readObject();
                    switch (command.getType()) {
                        case AUTH: {
                            AuthCommand commandData = (AuthCommand) command.getData();
                            nickname = commandData.getUsername();
                            successfulAuthEvent.authIsSuccessful(nickname);
                            break;
                        }
                        case MESSAGE: {
                            MessageCommand commandData = (MessageCommand) command.getData();
                            if (curentWindow != null) {
                                String message = commandData.getMessage();
                                curentWindow.appendMessage(message);
                                curentWindow.getClientController().getHistoryLogger().SaveHistory(message + "\n");
                            }
                            break;
                        }
                        case ERROR: {
                            if (controller.getAuthDialog() != null || controller.getClientChat() != null) {
                                ErrorCommand commandData = (ErrorCommand) command.getData();
                                showError(commandData.getErrorMessage());
                            }
                            break;
                        }
                        case UPDATE_USERS_LIST: {
                            UpdateUsersListCommand commandData = (UpdateUsersListCommand) command.getData();
                            List<String> users = commandData.getUsers();

                            Platform.runLater(
                                    () -> {
                                        curentWindow.updateUsersList(users);
                                    }
                            );
                            break;
                        }
                        case INFO: {
                            if (controller.getAuthDialog() != null || controller.getClientChat() != null) {
                                InfoCommand commandData = (InfoCommand) command.getData();
                                showInfoMessage(commandData.getInfoMessage());
                            }
                            break;
                        }
                        default:
                            System.err.println("Unknown type of command: " + command.getType());
                    }
                } catch (IOException e) {
                    System.out.println("ReadThread interrupted!");
                    close();
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showInfoMessage(String infoMessage) {
        controller.showInfoMessage(infoMessage);
    }

    public void sendCommand(Command command) throws IOException {
        out.writeObject(command);
    }


    public void setCurentWindow(FxChatWindow curentWindow) {
        this.curentWindow = curentWindow;
    }

    public void setSuccessfulAuthEvent(AuthEvent successfulAuthEvent) {
        this.successfulAuthEvent = successfulAuthEvent;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showError(String errorMessage) {
        controller.showErrorMessage(errorMessage);
    }
}
