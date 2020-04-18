package ru.geekbrains.java2.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import ru.geekbrains.java2.client.controller.ClientController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class NetworkClient extends Application {

    private String DEFAULT_ADDR = "localhost";
    private final int DEFAULT_PORT= 8189;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        TextInputDialog dialog = new TextInputDialog("localhost");
        dialog.setHeaderText("Введите адрес сервера:");
        dialog.setTitle("");
        dialog.setResizable(false);
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();


        result.ifPresent(serverIP -> DEFAULT_ADDR = serverIP);

        try {
            ClientController clientController = new ClientController(DEFAULT_ADDR, DEFAULT_PORT, primaryStage);
            clientController.runApplication();
        } catch (IOException e) {

            String errorMessage = "Failed to connect to server! Please, check you network settings";
            System.err.println(errorMessage);

            Platform.runLater(() ->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText(errorMessage);
                alert.showAndWait();
            });
        }

    }
}