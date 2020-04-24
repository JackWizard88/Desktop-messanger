package ru.geekbrains.java2.server.controller.fxview;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import ru.geekbrains.java2.server.NetworkServer;

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
    void initialize() {

        buttonStart.setOnAction(e -> {
            if (!isRunning) {
                networkServerThread = new Thread(() -> networkServer.start());
                networkServerThread.start();
                labelServerStatus.setText("Server status: ONLINE");
                labelServerStatus.setTextFill(Color.GREEN);
                isRunning = true;
            }
        });

        buttonStop.setOnAction(e -> {
            if (isRunning) {
                if (networkServer.stop() == 1) {
                    labelServerStatus.setText("Server status: OFFLINE");
                    labelServerStatus.setTextFill(Color.RED);
                    isRunning = false;
                    networkServerThread.interrupt();
                }
            }
        });
    }
}
