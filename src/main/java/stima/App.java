package stima;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import stima.modules.*;
import stima.controllers.*;

// public class App {
//     public static void main(String[] args) {
//         Board B = new Board(9);
//         B = Board.readBoard("test2.txt");
//         Solver S = new Solver(B);
//         S.solveBoard();
//     }
// }

public class App extends Application {
    // public static void main(String[] args) {
    //     Board B = new Board(9);
    //     B = Board.readBoard("test2.txt");
    //     Solver S = new Solver(B);
    //     S.solveBoard();
    // }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainscreen.fxml"));
        Group root = new Group();
        root.getChildren().add(loader.load());
        Scene scene = new Scene(root);
        stage.setTitle("Queen Solver");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(App.class, new String[0]);
    }
}
