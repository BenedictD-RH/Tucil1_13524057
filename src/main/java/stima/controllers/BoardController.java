package stima.controllers;

import java.util.Arrays;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import stima.modules.*;
import stima.helper.*;

public class BoardController {

    public Board board = new Board(9);
    private ColorPicker colorBank = new ColorPicker();
    private double gridSize = 520;
    public char selectedSign = 'A';
    private Image crownImage = new Image(getClass().getResourceAsStream("/crown_icon.png"));
    private boolean colorblindMode = false;
    private SolverTask solverTask;

    @FXML
    public Label iterationLabel, processStateLabel, timeElapsedLabel, fileInvalidLabel;

    @FXML
    public Button incDimButton, decDimButton, colorblindSwitch, saveButton, solveButton, resetButton, openFileButton;

    @FXML
    public TextField dimTextField, filenameTextField;

    public void setupBoardView(AnchorPane root) {
        GridPane grid = new GridPane();
        grid.setId("grid");
        for (int i = 0; i < this.board.boardDimension; i++) {
            for (int j = 0; j < this.board.boardDimension; j++) {
                StackPane stack = new StackPane();
                stack.setId("tile" + j + "-" + i);
                stack.getChildren().add(getTileColor(this.board.tileAt(j,i).sign));
                stack.getChildren().add(getTileBorder(j,i));
                if (this.colorblindMode) {
                    Label signLabel = new Label(Character.toString(this.board.tileAt(j,i).sign));
                    signLabel.setStyle("-fx-font-size: " + (getTileSize()/2));
                    stack.getChildren().add(signLabel);
                }
                if (processStateLabel.getText() != "Processing...") {
                    stack.getChildren().add(getTileHitbox(j, i, root));
                }
                if (this.board.isQueenAt(j,i)) {
                    stack.getChildren().add(getQueenTile());
                }
                grid.add(stack, j, i);
            }
        }
        AnchorPane.setRightAnchor(grid, 300 - (getTileSize()*this.board.boardDimension/2));
        AnchorPane.setTopAnchor(grid, 280 - (getTileSize()*this.board.boardDimension/2));
        root.getChildren().add(grid);
    }

    private void refreshScreen(AnchorPane root) {
        refreshGrid(root);
        refreshButtonGrid(root);
        updateDimensionInput();
        if (this.board.isBoardValid()) {
            processStateLabel.setText("Board Valid");
            processStateLabel.setStyle("-fx-text-fill: limegreen");
            solveButton.setDisable(false);
        }
        else {
            processStateLabel.setText("Board Invalid");
            processStateLabel.setStyle("-fx-text-fill: red");
            solveButton.setDisable(true);
        }
    }

    private void refreshGrid(AnchorPane root) {
        GridPane grid = (GridPane) root.lookup("#grid");
        root.getChildren().remove(grid);
        setupBoardView(root);
    }

    public void setupSignButtons(AnchorPane root) {
        GridPane buttonGrid = new GridPane();
        buttonGrid.setId("buttonGrid");
        String signString = getUniqueSigns();
        //System.out.println(signString);
        for (int i = 0; i < this.board.boardDimension; i++) {
            StackPane stack = new StackPane();
            Label color = new Label();
            Button signButton = new Button();

            signButton.setId("signButton");
            String colorHex = colorBank.getColor(signString.charAt(i));
            color.setStyle("-fx-background-color: #" + colorHex.substring(2));
            color.setPrefSize(50, 50);
            signButton.setPrefSize(50, 50);
            int fi = i;
            signButton.setOnAction(event ->  {
                this.selectedSign = signString.charAt(fi);
                refreshButtonGrid(root);
            });

            if(signString.charAt(i) == this.selectedSign) {
                signButton.setStyle("-fx-border-color: rgba(0,0,0,0.2)");
            }
            else if (!this.board.isSignOnBoard(signString.charAt(i))) {
                signButton.setStyle("-fx-border-color: rgba(255,0,0,1)");
            }

            

            stack.getChildren().add(color);
            stack.getChildren().add(signButton);
            if (!this.board.isSignOnBoard(signString.charAt(i))) {
                Label NA = new Label("N/A");
                NA.setId("tileLabel");
                NA.setMouseTransparent(true);
                stack.getChildren().add(NA);
            }
            else if (this.colorblindMode) {
                Label signLabel = new Label(Character.toString(signString.charAt(i)));
                signLabel.setStyle("-fx-font-size: 25");
                signLabel.setMouseTransparent(true);
                stack.getChildren().add(signLabel);
            }
            buttonGrid.add(stack, (i - (i % 9))/9, i % 9);
        }
        AnchorPane.setLeftAnchor(buttonGrid, 10D);
        AnchorPane.setTopAnchor(buttonGrid, 10D);
        buttonGrid.setVgap(10);
        buttonGrid.setHgap(10);
        root.getChildren().add(buttonGrid);
    }

    private void refreshButtonGrid(AnchorPane root) {
        GridPane grid = (GridPane) root.lookup("#buttonGrid");
        root.getChildren().remove(grid);
        setupSignButtons(root);
    }

    private String getUniqueSigns() {
        String signString = this.board.uniqueSigns;
        char c = 'A';
        while (signString.length() < this.board.boardDimension) {
            while(signString.contains(Character.toString(c))) {
                c++;
            }
            signString += c;
        }
        char[] cArray = signString.toCharArray();
        Arrays.sort(cArray);
        signString = new String(cArray);
        return signString;
    }

    private double getTileSize() {
        double size = this.gridSize/this.board.boardDimension;
        if (size > 80) {
            size = 80;
        }
        return size;
    }

    private Label getTileColor(char sign) {
        Label tileColor = new Label();
        // tileColor.setText(String.valueOf(sign));
        tileColor.setTextAlignment(TextAlignment.CENTER);
        tileColor.setId("tileColor");
        if (sign == ' ') {
            tileColor.setStyle("-fx-background-color: #AAAAAA");
        }
        else {
            String colorHex = colorBank.getColor(sign);
            tileColor.setStyle("-fx-background-color: #" + colorHex.substring(2));
        }
        tileColor.setPrefSize(getTileSize(),getTileSize());

        return tileColor;
    }

    private Label getTileBorder(int x, int y) {
        Label tileBorder = new Label();
        char tileSign = this.board.tileAt(x,y).sign;
        double bLeft = 0.02*getTileSize();
        double bRight = 0.02*getTileSize();
        double bUp = 0.02*getTileSize();
        double bDown = 0.02*getTileSize();

        if (x == 0) {
            bLeft *= 4;
        }
        else if (tileSign != this.board.tileAt(x - 1, y).sign) {
            bLeft *= 2;
        }
        else if (tileSign == ' ') {
            bLeft *= 0;
        }

        if (x == this.board.boardDimension - 1) {
            bRight *= 4;
        }
        else if (tileSign != this.board.tileAt(x + 1, y).sign) {
            bRight *= 2;
        }
        else if (tileSign == ' ') {
            bRight *= 0;
        }

        if (y == 0) {
            bUp *= 4;
        }
        else if (tileSign != this.board.tileAt(x, y - 1).sign) {
            bUp *= 2;
        }
        else if (tileSign == ' ') {
            bUp *= 0;
        }

        if (y == this.board.boardDimension - 1) {
            bDown *= 4;
        }
        else if (tileSign != this.board.tileAt(x, y + 1).sign) {
            bDown *= 2;
        }
        else if (tileSign == ' ') {
            bDown *= 0;
        }
        
        //System.out.println("(" + x + ", " + y + ") " + "-fx-border-width: " + bUp + "px " + bRight + "px " + bDown + "px " + bLeft + "px");
        tileBorder.setStyle("-fx-border-width: " + bUp + "px " + bRight + "px " + bDown + "px " + bLeft + "px");
        tileBorder.setId("tileBorder");
        tileBorder.setPrefSize(getTileSize(),getTileSize());

        return tileBorder;
    }

    private ImageView getQueenTile() {
        ImageView imageView = new ImageView();
        imageView.setImage(crownImage);
        imageView.setId("queen");
        imageView.setFitWidth(getTileSize()*0.6);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private Label getTileHitbox(int x, int y, AnchorPane root) {
        Label tileHitbox = new Label();
        
        tileHitbox.setPrefSize(getTileSize(),getTileSize());
        tileHitbox.setOnMousePressed(event -> {
            this.board.updateTileSign(x, y, selectedSign);
            refreshScreen(root);
        });
        tileHitbox.setOnMouseEntered(event -> {
            if (event.isControlDown() && (this.board.tileAt(x, y).sign != this.selectedSign)) {
                this.board.updateTileSign(x, y, selectedSign);
                refreshScreen(root);
            }
        });
        return tileHitbox;
    }

    private AnchorPane getRoot(ActionEvent event) {
        return (AnchorPane) ((Node)event.getSource()).getScene().getRoot();
    }

    public void solveBoard(ActionEvent event) {
        if (this.board.isBoardValid()) {
            solveButton.setDisable(true);
            disableInputs();
            invokeSolverTask(getRoot(event));
        }
        else {
            System.out.println("Board still invalid!");
        }
    }

    private void invokeSolverTask(AnchorPane root) {
        if (solverTask != null && solverTask.isRunning()) {
            solverTask.cancel();
        }
        Solver S = new Solver(this.board);
        SolverTask solverTask = new SolverTask(S);
        solverTask.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                iterationLabel.setText("Iterations : " + Integer.toString(newValue));
                timeElapsedLabel.setText("Time Elapsed : " + S.timeElapsed + "ms");
                refreshGrid(root);
                if (processStateLabel.getText() != S.processState) {
                    processStateLabel.setText(S.processState);
                    if (S.processState == "Processing...") {
                        processStateLabel.setStyle("-fx-text-fill: black");
                    }
                    else if (S.processState == "Answer Found") {
                        processStateLabel.setStyle("-fx-text-fill: limegreen");
                        saveButton.setDisable(false);
                        int n = 1;
                        File f = new File("data/results/txt/result_" + n);
                        while(f.exists()) {
                            n++;
                            f = new File("data/results/txt/result_" + n);
                        }
                        S.writeResult("data/results/txt/result_" + n);
                        reenableInputs();
                    }
                    else if (S.processState == "Answer not Found"){
                        processStateLabel.setStyle("-fx-text-fill: red");
                        reenableInputs();
                    }
                }
            }
        });

        Thread th = new Thread(solverTask);
        th.setDaemon(true);
        th.start();
    }

    public void setBoardDimension(ActionEvent event) {
        TextField dimInput = (TextField) event.getSource();
        boolean invalid = false;
        int newDim = 0;
        try {
            newDim = Integer.parseInt(dimInput.getText());
            if ((newDim < 1) || (newDim > 26)) {
                invalid = true;
            }
        } catch (NumberFormatException e) {
            invalid = true;
        }
        if (invalid) {
            dimInput.setText(Integer.toString(this.board.boardDimension));
        }
        else {
            this.board.boardDimension = newDim;
            refreshScreen(getRoot(event));
        }
        updateDimensionInput();
    }

    public void increaseBoardDimension(ActionEvent event) {
        this.board.boardDimension++;
        refreshScreen(getRoot(event));
    }

    public void decreaseBoardDimension(ActionEvent event) {
        this.board.boardDimension--;
        refreshScreen(getRoot(event));
    }

    public void updateDimensionInput() {
        incDimButton.setDisable(false);
        decDimButton.setDisable(false);
        dimTextField.setText(Integer.toString(this.board.boardDimension));
        if (this.board.boardDimension == 1) {
            decDimButton.setDisable(true);
        }
        else if (this.board.boardDimension == 26) {
            incDimButton.setDisable(true);
        }
    }

    private Stage getStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setInputFile(ActionEvent event) {
        String directory = "data/input/" + filenameTextField.getText();
        File f = new File(directory);
        if (f.exists()) {
            setBoardFromFile(new File(directory), event);
        }
    
    }

    public void openFileDialog(ActionEvent event) {
        FileChooser f = new FileChooser();
        f.setInitialDirectory(new File("data/input"));
        File selectedFile = f.showOpenDialog(getStage(event));
        if (selectedFile != null) {
            setBoardFromFile(selectedFile, event);
            filenameTextField.setText(selectedFile.getName());
        }
    }

    private void setBoardFromFile(File f, ActionEvent event) {
        resetBoard(event);
        fileInvalidLabel.setText("");
        this.board = Board.readBoard(f);
        if (this.board.isBoardEmpty()) {
            fileInvalidLabel.setText("File Invalid!");
        }
        refreshScreen(getRoot(event));
    }

    public void resetBoard(ActionEvent event) {
        this.board = new Board(9);
        refreshScreen(getRoot(event));
        iterationLabel.setText("Iterations : ");
        timeElapsedLabel.setText("Time Elapsed : ");
        saveButton.setDisable(true);
    }

    public void switchColorblindMode(ActionEvent event) {
        this.colorblindMode = !this.colorblindMode;
        if (this.colorblindMode) {
            colorblindSwitch.setText("Colorblind Mode: ON");
        }
        else {
            colorblindSwitch.setText("Colorblind Mode: OFF");
        }
        refreshScreen(getRoot(event));
    }

    public void saveResultAs(ActionEvent event) {
        FileChooser f = new FileChooser();
        f.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
        f.setInitialDirectory(new File("data/results/png"));
        File file = f.showSaveDialog(getStage(event));

        if (file != null) {
            try {
                Node node = getRoot(event).lookup("#grid");
                WritableImage writableImage = node.snapshot(null, null);
                
                java.awt.image.RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void disableInputs() {
        openFileButton.setDisable(true);
        incDimButton.setDisable(true);
        decDimButton.setDisable(true);
        dimTextField.setDisable(true);
        resetButton.setDisable(true);
        colorblindSwitch.setDisable(true);
        filenameTextField.setDisable(true);
    }

    private void reenableInputs() {
        openFileButton.setDisable(false);
        incDimButton.setDisable(false);
        decDimButton.setDisable(false);
        dimTextField.setDisable(false);
        resetButton.setDisable(false);
        colorblindSwitch.setDisable(false);
        filenameTextField.setDisable(false);
    }
}
