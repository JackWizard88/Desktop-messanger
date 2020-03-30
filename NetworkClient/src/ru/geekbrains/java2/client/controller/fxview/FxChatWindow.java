package ru.geekbrains.java2.client.controller.fxview;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import ru.geekbrains.java2.client.controller.ClientController;
import javax.swing.*;
import java.util.List;

public class FxChatWindow {
    
    private ClientController clientController;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    @FXML
    private TextArea chatTextField;

    @FXML
    private TextField inputTextField;

    @FXML
    private Button sendButton;

    @FXML
    private ListView<String> userListField;

    @FXML
    private MenuItem chatClearButton;

    @FXML
    private MenuItem chatChangeNickButton;
    
    @FXML
    void initialize() {

        userListField.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //перенос строк
        chatTextField.setWrapText(true);
        //отправка сообщений и очистка чата
        sendButton.setOnAction(e -> sendMessage(inputTextField.getText()));
        chatClearButton.setOnAction(e -> chatTextField.setText(""));
        chatChangeNickButton.setOnAction(e -> changeNickname());
        inputTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                sendMessage(inputTextField.getText());
            }
        });
    }

    private void changeNickname() {
        String newNick = JOptionPane.showInputDialog(null, "Введите новый ник: ");
        if (newNick != null) {
            clientController.sendMessage("/newNick " + clientController.getUsername() + " " + newNick);
            clientController.getPrimaryStage().setTitle(newNick + " via JackMessenger");
        }
    }

    //метод отправки сообщений
    private void sendMessage(String msg) {
        if (!msg.trim().isEmpty()) {
            chatTextField.appendText("Я: " + msg + "\n");
            clientController.sendMessage(msg);
        }
        inputTextField.clear();
    }

    public void appendMessage(String msg) {
        chatTextField.appendText(msg + "\n");
    }

    public void updateUserListField(List<String> userlist) {
        userListField.setItems(FXCollections.observableList(userlist));
    }

}