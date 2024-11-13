package ru.jackwizard88.destktopmessenger.client.controller.fxview;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.jackwizard88.destktopmessenger.client.controller.ClientController;

import java.io.IOException;

public class FxRegWindow implements Window {

    private ClientController clientController;
    private Stage stage;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passTextField1;

    @FXML
    private PasswordField passTextField2;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {

        registerButton.setOnAction(e -> checkRegDataAndSend());

        cancelButton.setOnAction(e -> closeRegWindow());
    }

    private void checkRegDataAndSend() {

        String login = loginTextField.getText().trim();
        String password = passTextField1.getText().trim();
        String password2 = passTextField2.getText().trim();
        String nickname = nicknameTextField.getText().trim();
        String saltedPassword = clientController.saltPassword(password);

        if (!login.isEmpty() && !nickname.isEmpty() && !password.isEmpty()) {
            if (password.equals(password2)) {
                try {
                    clientController.sendRegMessage(login, saltedPassword, nickname);
                } catch (IOException e) {
                    e.printStackTrace();
                    showErrorMessage("Ошибка");
                } finally {
                    System.out.println("registration data successfully sent to server");
                    closeRegWindow();
                }

            }
            else showInfoMessage("Пароли не совпадают");
        } else showInfoMessage("Необходимо заполнить все поля");

    }

    public void closeRegWindow() {
        stage.close();
    }

    public void showErrorMessage(String errorMessage) {
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            System.exit(1);
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

