package ru.geekbrains.java2.client.controller.fxview;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import ru.geekbrains.java2.client.controller.ClientController;

import javax.swing.*;

public class FxAuthDialog {

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
    void initialize() {

        buttonOk.setOnAction(e -> onOK());
        buttonCancel.setOnAction(e -> onCancel());

        passwordText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                onOK();
            }
        });

    }

    private void onOK() {

        String login = loginText.getText().trim();
        String pass = passwordText.getText().trim();

        if (!loginText.getText().trim().isEmpty() && !passwordText.getText().trim().isEmpty()) {
            try {
                clientController.sendAuthMessage(login, pass);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка аутентификации. Проверьте данные");
            }

            clientController.setUsername(login);
        }
    }

    private void onCancel() {
        System.exit(0);
    }

}