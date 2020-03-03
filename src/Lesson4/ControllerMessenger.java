package Lesson4;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class ControllerMessenger {

     @FXML
     private TextArea ChatTextField;

     @FXML
     private TextField InputTextField;

     @FXML
     private Button SendButton;

     @FXML
     private ListView<String> UserListField;

     @FXML
     private MenuItem chatClearButton;

     @FXML
     void initialize() {

          UserListField.setItems(UserList.getUserList());
          UserListField.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

          UserListField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> UserList.setActiveUser(UserListField.getSelectionModel().getSelectedItem()));

          SendButton.setOnAction(event -> sendMessage());
          chatClearButton.setOnAction(event -> ChatTextField.setText(""));
          InputTextField.setOnKeyPressed(keyEvent -> {
               if (keyEvent.getCode() == KeyCode.ENTER)  {
                    sendMessage();
               }
          });
     }

     private void sendMessage() {
          if (!InputTextField.getText().equals("")) {
               ChatTextField.setText(ChatTextField.getText() + UserList.getActiveUser() + ": " + InputTextField.getText() + "\n");
               InputTextField.clear();
          }
     }
}

