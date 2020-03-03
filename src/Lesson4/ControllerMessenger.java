package Lesson4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class ControllerMessenger {

     @FXML
     private TextArea ChatTextField;

     @FXML
     private TextField InputTextField;

     @FXML
     private Button SendButton;

     @FXML
     private ListView<?> UserListField;

     @FXML
     private MenuItem chatClearButton;

     @FXML
     void initialize() {

          UserListField.setItems(UserList.getUserList());
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
               ChatTextField.setText(ChatTextField.getText() + "Я: " + InputTextField.getText() + "\n");
               InputTextField.clear();
          }
     }
}

