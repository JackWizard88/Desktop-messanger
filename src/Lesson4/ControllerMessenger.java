package Lesson4;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class ControllerMessenger {

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
     void initialize() {
          //перенос строк
          chatTextField.setWrapText(true);

          //наполнение таблицы пользователей
          userListField.setItems(ClientContext.getUserList());

          //модель селектора единичная
          userListField.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

          //слушатель выбранного пользователя
          userListField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> ClientContext.setActiveUser(userListField.getSelectionModel().getSelectedItem()));

          //отправка сообщений и очистка чата
          sendButton.setOnAction(event -> sendMessage());
          chatClearButton.setOnAction(event -> chatTextField.setText(""));
          inputTextField.setOnKeyPressed(keyEvent -> {
               if (keyEvent.getCode() == KeyCode.ENTER)  {
                    sendMessage();
               }
          });
     }

     //метод отправки сообщений
     private void sendMessage() {
          if (!inputTextField.getText().trim().isEmpty()) {
               chatTextField.appendText(ClientContext.getActiveUser() + ": " + inputTextField.getText() + "\n");
          }
          inputTextField.clear();
     }
}

