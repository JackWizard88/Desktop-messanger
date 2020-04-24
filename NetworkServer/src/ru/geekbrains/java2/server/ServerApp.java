package ru.geekbrains.java2.server;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {

    private static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        NetworkServer networkServer = new NetworkServer(DEFAULT_PORT);
        ServerController controller = new ServerController(networkServer, primaryStage);
        controller.startController();
    }
}
