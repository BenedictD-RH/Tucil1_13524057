package stima;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import stima.controllers.*;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainscreen.fxml"));
        AnchorPane root = new AnchorPane();
        root.getChildren().add(loader.load());
        root.setId("root");
        BoardController c = loader.getController();
        c.setupSignButtons(root);
        c.setupBoardView(root);
        c.updateDimensionInput();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Queen Solver");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(App.class, new String[0]);
    }
}
