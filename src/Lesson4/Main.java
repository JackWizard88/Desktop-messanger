package Lesson4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ClientWindow.fxml"));
        primaryStage.setTitle("JackMessenger");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("userList.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {


        //создание пользователей и добавление в список контактов
        ClientContext.add(new User("Ivanov"));
        ClientContext.add(new User("Krylov"));
        ClientContext.add(new User("Petrov"));
        ClientContext.add(new User("Sidorov"));

        //запуск интерфейса
        launch(args);
    }
}
