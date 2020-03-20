package ru.geekbrains.java2.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.java2.client.controller.fxview.FxAuthDialog;
import ru.geekbrains.java2.client.controller.fxview.FxChatWindow;
import ru.geekbrains.java2.client.model.NetworkService;
import javax.swing.*;
import java.io.IOException;

public class ClientController {

    private final NetworkService networkService;
    private Stage primaryStage;
    private Parent rootChat;
    private String nickname;


    public ClientController(String serverHost, int serverPort, Stage primaryStage) {
        this.networkService = new NetworkService(serverHost, serverPort);
        this.primaryStage = primaryStage;
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
        FxAuthDialog authDialog  = loaderAuth.getController();
        authDialog.setClientController(this);

        Scene scene = new Scene(rootChat, 300, 200);
        primaryStage.setTitle("LogIn Messenger");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->{
            System.exit(0);
        });
    }

    private void openChat() {
        FXMLLoader loaderChat = new FXMLLoader();
        try {
            rootChat = loaderChat.load(getClass().getResourceAsStream("fxview/FxChatWindow.fxml"));
            FxChatWindow clientChat  = loaderChat.getController();
            clientChat.setClientController(this);
            Scene scene = new Scene(rootChat, 600, 400);
            scene.getStylesheets().add(getClass().getResource("userList.css").toExternalForm());
            primaryStage.setTitle(nickname + " via JackMessenger");
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(200);
            primaryStage.setMinWidth(400);
            primaryStage.show();
            networkService.setCurentWindow(clientChat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUserName(String nickname) {
        this.nickname = nickname;
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect();
        } catch (IOException e) {
            System.err.println("Failed to establish server connection");
            throw e;
        }
    }

    public void sendAuthMessage(String login, String pass) throws IOException {
        networkService.sendAuthMessage(login, pass);
    }

    public void sendMessage(String message) {
        try {
            networkService.sendMessage(message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to send message!");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        networkService.close();
    }

    public String getUsername() {
        return nickname;
    }
}
