package main.java.ru.geekbrains.java2.client.controller.fxview;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.java.ru.geekbrains.java2.client.controller.ClientController;

public class FxAuthDialog implements Window {

    private FxRegWindow fxRegWindow;
    private ClientController clientController;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button buttonOk;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonRegister;

    @FXML
    void initialize() {

        buttonOk.setOnAction(e -> onOK());
        buttonCancel.setOnAction(e -> onCancel());

        passwordText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                onOK();
            }
        });

        buttonRegister.setOnAction(e -> registration());

    }

    private void registration() {
        clientController.getPrimaryStage().hide();
        openRegWindow();

    }

    private void openRegWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FxRegWindow.fxml"));
        Stage secondaryStage = new Stage();
        Parent root;

        try {
            root = loader.load();
            Scene scene = new Scene(root, 350, 250);
            fxRegWindow = loader.getController();
            secondaryStage.setTitle("Registration");
            secondaryStage.setScene(scene);
            secondaryStage.setResizable(false);
            fxRegWindow.setClientController(clientController);
            fxRegWindow.setStage(secondaryStage);
            secondaryStage.setOnCloseRequest(e-> fxRegWindow.closeRegWindow());
            secondaryStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fxRegWindow = null;
            clientController.getPrimaryStage().show();
        }
    }

    private void onOK() {

        String login = loginText.getText().trim();
        String pass = passwordText.getText().trim();

        if (!loginText.getText().trim().isEmpty() && !passwordText.getText().trim().isEmpty()) {
            try {
                clientController.sendAuthMessage(login, pass);
            } catch (IOException e) {
                showErrorMessage("Ошибка аутентификации. Проверьте данные");
            }

            clientController.setUsername(login);
        }
    }

    private void onCancel() {
        System.exit(0);
    }

    public void showErrorMessage(String errorMessage) {
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
        });
    }

    public void showInfoMessage(String infoMessage) {
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText(null);
            alert.setContentText(infoMessage);
            alert.showAndWait();
        });
    }

}