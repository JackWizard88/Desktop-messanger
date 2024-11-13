package ru.jackwizard88.destktopmessenger.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.jackwizard88.destktopmessenger.client.controller.fxview.FxAuthDialog;
import ru.jackwizard88.destktopmessenger.client.controller.fxview.FxChatWindow;
import ru.jackwizard88.destktopmessenger.client.history.HistoryLogger;
import ru.jackwizard88.destktopmessenger.client.model.NetworkService;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static ru.jackwizard88.destktopmessenger.commands.Command.*;


public class ClientController {

    private final NetworkService networkService;
    private final Stage primaryStage;
    private Parent rootChat;
    private String nickname;
    private String username;
    private HistoryLogger history;
    private FxAuthDialog authDialog;
    private FxChatWindow clientChat;

    public String getNickname() {
        return nickname;
    }

    public FxAuthDialog getAuthDialog() {
        return authDialog;
    }

    public FxChatWindow getClientChat() {
        return clientChat;
    }

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
//        this.authDialog = new FxAuthDialog();
//        this.clientChat = new FxChatWindow();
    }

    public void runApplication() throws IOException {
        connectToServer();
        runAuthProcess();
    }

    private void runAuthProcess() {
        networkService.setSuccessfulAuthEvent(nickname -> {
            setUserName(nickname);
            Platform.runLater(this::openChat);
        });

        FXMLLoader loaderAuth = new FXMLLoader();
        try {
            rootChat = loaderAuth.load(getClass().getResourceAsStream("/FxAuthDialog.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        authDialog = loaderAuth.getController();
        authDialog.setClientController(this);


        Scene scene = new Scene(rootChat, 300, 250);
        primaryStage.setTitle("LogIn Messenger");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e-> System.exit(0));
    }

    private void openChat() {
        FXMLLoader loaderChat = new FXMLLoader();

        try {
            rootChat = loaderChat.load(getClass().getResourceAsStream("/FxChatWindow.fxml"));
            clientChat  = loaderChat.getController();
            history = new HistoryLogger(username, clientChat);
            networkService.setCurrentWindow(clientChat);
            clientChat.setClientController(this);
            Scene scene = new Scene(rootChat, 600, 400);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/userList.css")).toExternalForm());
            primaryStage.setTitle(nickname + " via JackMessenger");
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(200);
            primaryStage.setMinWidth(400);
            clientChat.getClientController().getHistoryLogger().RetrieveHistory();
            primaryStage.show();
            authDialog = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void showInfoMessage(String infoMessage) {

        if (authDialog != null) {
            authDialog.showInfoMessage(infoMessage);
        } else if (clientChat != null) {
            clientChat.showInfoMessage(infoMessage);
        }
    }

    public void sendRegMessage(String login, String password, String nickname) throws IOException {
        networkService.sendCommand(registerCommand(login, password, nickname));
    }

    public String saltPassword(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}