package ru.geekbrains.java2.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.java2.client.controller.fxview.FxAuthDialog;
import ru.geekbrains.java2.client.controller.fxview.FxChatWindow;
import ru.geekbrains.java2.client.history.HistoryLogger;
import ru.geekbrains.java2.client.model.NetworkService;
import java.io.IOException;

import static ru.geekbrains.java2.commands.Command.*;

public class ClientController {

    private final NetworkService networkService;
    private Stage primaryStage;
    private Parent rootChat;
    private String nickname;
    private String username;
    private HistoryLogger history;

    public String getNickname() {
        return nickname;
    }

    public FxAuthDialog getAuthDialog() {
        return authDialog;
    }

    public FxChatWindow getClientChat() {
        return clientChat;
    }

    private FxAuthDialog authDialog;
    private FxChatWindow clientChat;

    public HistoryLogger getHistoryLogger() {
        return history;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ClientController(String serverHost, int serverPort, Stage primaryStage) {
        this.networkService = new NetworkService(serverHost, serverPort);
        this.primaryStage = primaryStage;
        this.authDialog = new FxAuthDialog();
        this.clientChat = new FxChatWindow();
    }

    public void runApplication() throws IOException {
        connectToServer();
        runAuthProcess();
    }

    private void runAuthProcess() {
        networkService.setSuccessfulAuthEvent(nickname -> {
            setUserName(nickname);
            Platform.runLater(()->{
                openChat();
            });
        });

        FXMLLoader loaderAuth = new FXMLLoader();
        try {
            rootChat = loaderAuth.load(getClass().getResourceAsStream("fxview/FxAuthDialog.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        authDialog  = loaderAuth.getController();
        authDialog.setClientController(this);


        Scene scene = new Scene(rootChat, 300, 220);
        primaryStage.setTitle("LogIn Messenger");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->{
            System.exit(0);
        });
    }

    private void openChat() {
        FXMLLoader loaderChat = new FXMLLoader();

        try {
            rootChat = loaderChat.load(getClass().getResourceAsStream("fxview/FxChatWindow.fxml"));
            clientChat  = loaderChat.getController();
            history = new HistoryLogger(username, clientChat);
            networkService.setCurentWindow(clientChat);
            clientChat.setClientController(this);
            Scene scene = new Scene(rootChat, 600, 400);
            scene.getStylesheets().add(getClass().getResource("userList.css").toExternalForm());
            primaryStage.setTitle(nickname + " via JackMessenger");
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(200);
            primaryStage.setMinWidth(400);
            primaryStage.show();
            clientChat.getClientController().getHistoryLogger().RetrieveHistory();
            authDialog = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return username;
    }

    private void setUserName(String nickname) {
        this.nickname = nickname;
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect(this);
        } catch (IOException e) {
            System.err.println("Failed to establish server connection");
            throw e;
        }
    }

    public void sendAuthMessage(String login, String pass) throws IOException {
        networkService.sendCommand(authCommand(login, pass));
    }

    public void sendMessage(String message, String acceptor) {
        try {
            networkService.sendCommand(messageCommand(nickname, message, acceptor));
        } catch (IOException e) {
            networkService.showError("Failed to send message!");
            e.printStackTrace();
        }
    }

    public void changeNick(String newNickname) {
        try {
            networkService.sendCommand(changeNicknameCommand(username, newNickname));
        } catch (IOException e) {
            networkService.showError("Failed to changeNickname!");
            e.printStackTrace();
        }
        nickname = newNickname;
    }

    public void showErrorMessage(String errorMessage) {

        if (authDialog != null) {
            authDialog.showErrorMessage(errorMessage);
        } else if (clientChat != null) {
            clientChat.showErrorMessage(errorMessage);
        }
    }
}
