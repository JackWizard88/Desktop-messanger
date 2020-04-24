package ru.geekbrains.java2.server;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.java2.server.controller.fxview.FxServerApp;
import java.io.IOException;

public class ServerController {

    private NetworkServer networkServer;
    private Stage primaryStage;
    private Parent parent;
    private FxServerApp fxServerApp;


    public ServerController(NetworkServer networkServer, Stage primaryStage) {
        this.networkServer = networkServer;
        this.primaryStage = primaryStage;
    }

    public void startController() {

        Platform.runLater(() -> {

            FXMLLoader loader = new FXMLLoader();

            try {
                parent = loader.load(getClass().getResourceAsStream("/FxServerApp.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            fxServerApp = loader.getController();
            fxServerApp.setNetworkServer(networkServer);
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e-> System.exit(0));
            primaryStage.setTitle("Server");
            primaryStage.setResizable(false);
            primaryStage.show();

        });
    }
}
