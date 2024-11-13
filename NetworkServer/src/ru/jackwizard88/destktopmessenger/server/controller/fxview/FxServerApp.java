package ru.jackwizard88.destktopmessenger.server.controller.fxview;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import ru.jackwizard88.destktopmessenger.server.NetworkServer;

import java.io.IOException;
import java.util.List;

public class FxServerApp {

    private NetworkServer networkServer;
    private Thread networkServerThread;
    private boolean isRunning = false;


    public void setNetworkServer(NetworkServer networkServer) {
        this.networkServer = networkServer;
    }

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonStop;

    @FXML
    private Label labelServerStatus;

    @FXML
    private ListView<String> onlineClientsList;

    @FXML
    void initialize() {

        buttonStop.setDisable(true);
        onlineClientsList.autosize();

        buttonStart.setOnAction(e -> {
            if (!isRunning) {
                networkServerThread = new Thread(() -> networkServer.start(), "NetworkServerThread");
                networkServerThread.start();
                networkServer.setFXServerApp(this);
                labelServerStatus.setText("Server status: ONLINE");
                labelServerStatus.setTextFill(Color.GREEN);
                isRunning = true;
                buttonStart.setDisable(true);
                buttonStop.setDisable(false);
            }
        });

        buttonStop.setOnAction(e -> {
            if (isRunning) {
                try {
                    if (networkServer.stop() == 1) {
                        labelServerStatus.setText("Server status: OFFLINE");
                        labelServerStatus.setTextFill(Color.RED);
                        isRunning = false;
                        buttonStart.setDisable(false);
                        buttonStop.setDisable(true);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void setOnlineClientsList(List<String> users) {

        Platform.runLater(() -> onlineClientsList.setItems(FXCollections.observableList(users)));

    }

}
