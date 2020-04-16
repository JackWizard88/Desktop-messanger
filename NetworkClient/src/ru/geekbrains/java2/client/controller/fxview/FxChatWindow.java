package ru.geekbrains.java2.client.controller.fxview;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import ru.geekbrains.java2.client.controller.ClientController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FxChatWindow implements Window {
    
    private ClientController clientController;
    private String receiver = "all";

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public ClientController getClientController() {
        return clientController;
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
    private Label receiverLabel;

    @FXML
    private MenuItem chatClearButton;

    @FXML
    private MenuItem chatChangeNickButton;

    @FXML
    private MenuItem chatClearHistoryButton;

    @FXML
    private CheckMenuItem censoredCheckbox;

    @FXML
    private MenuItem exitButton;
    
    @FXML
    void initialize() {

        userListField.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setReceiver();

        userListField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selected = userListField.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.equals(clientController.getNickname())) {
                    receiver = "all";
                } else receiver = selected;
                setReceiver();
            }
        });

        chatTextField.setWrapText(true);
        chatClearHistoryButton.setOnAction(e -> {
            clientController.getHistoryLogger().clearHistory();
            chatTextField.clear();
        });
        sendButton.setOnAction(e -> sendMessage(inputTextField.getText()));

        chatClearButton.setOnAction(e -> chatTextField.clear());
        chatChangeNickButton.setOnAction(e -> changeNickname());

        exitButton.setOnAction(e -> System.exit(0));

        inputTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                sendMessage(inputTextField.getText());
            }
        });

    }

    private void changeNickname() {

        TextInputDialog dialog = new TextInputDialog(clientController.getNickname());
        dialog.setHeaderText("Введите новый никнейм:");
        dialog.setTitle("");
        dialog.setResizable(false);
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();


        result.ifPresent(name -> {
            clientController.changeNick(name);
            clientController.getPrimaryStage().setTitle(name + " via JackMessenger");
        });

    }

    //метод отправки сообщений
    private void sendMessage(String msg) {
        if (!msg.trim().isEmpty()) {
            String pr = "Я:";
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss");
            String time = "[" + formatForDateNow.format(dateNow) + "] ";
            if (!receiver.equals("all")) {
                pr = String.format("(private) %s:", receiver);
            }
            String message = String.format("%s %s %s \n", time, pr, msg);
            chatTextField.appendText(message);
            clientController.getHistoryLogger().SaveHistory(message);
            clientController.sendMessage(msg, receiver);
        }
        inputTextField.clear();
    }

    public void appendMessage(String msg) {
        if (censoredCheckbox.isSelected()) {
            msg = censorMessage(msg);
        }
        chatTextField.appendText(msg + "\n");
    }

    public void updateUsersList(List<String> users) {
        userListField.setItems(FXCollections.observableList(users));
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

    private void setReceiver() {
        receiverLabel.setText(String.format("to %s:", receiver.toUpperCase()));
    }

    private String censorMessage(String message) {
        final String[] forbidden = { "блядь",
                "бля",
                "выеб",
                "гомо",
                "долбо",
                "ебло",
                "ебли",
                "ебать",
                "ебич",
                "ебуч",
                "ебун",
                "ебла",
                "ебну",
                "ебол",
                "ебош",
                " лох",
                "лошар",
                "муда",
                "мудо",
                "ебал",
                "ебат",
                "ебуч",
                "заёб",
                "залуп",
                "залупо",
                "ебин",
                "манда",
                "мандо",
                "ъеби",
                "хуе",
                "пизда",
                "пидар",
                "пидор",
                "залуп",
                "пизд",
                "сука",
                "сучка",
                "трах",
                "уебок",
                "уебать",
                "гондо",
                "гандо",
                "уебан",
                "хуй",
                "хуи",
                "членосос",
                "член",
                "шлюх"};

        for (String s : forbidden) {
            Pattern pattern = Pattern.compile("(\\w*)" + s + "(\\w*)");
            Matcher matcher = pattern.matcher(message);
            message = matcher.replaceAll("***");
        }
        return message;


    }
}