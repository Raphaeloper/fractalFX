package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {
    ImageView imageView = new ImageView();
    Draw draw = new Draw();
    Stage pStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        pStage = primaryStage;
        pStage.getIcons().add(new Image("file:icon.png"));
        setUI();
        setTask();
    }

    //Creates and launches calculation threads
    //Updates the screen afterwards
    public void setTask() {
        Calculator task;
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        //Calculating row by row
        for (double y = draw.yLimL; y <= draw.yLimU; y += draw.diff) {
            //Creating a different thread for each row
            task = new Calculator(draw, y);
            //Workaround for updating the image (needed to be final)
            double finalY = y;
            //When the row has finished loading
            task.setOnSucceeded((succeededEvent) -> {
                updateImage(finalY);
            });
            //Launching each thread
            executorService.execute(task);
        }
        executorService.shutdown();
    }

    //Outputs the row to an image
    public void updateImage(double y) {
        int graphY;
        graphY = draw.originY + (int) (y * draw.magSize);
        imageView.setImage(draw.paint(graphY));
    }

    //Sets up the general overlay
    public void setUI() throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.setCenter(imageView);

        MenuBar menuBar = setMenus();
        root.setTop(menuBar);

        pStage.setTitle("Fractal Demo");
        pStage.setScene(new Scene(root, 200, 300));
        pStage.setMaximized(true);
        pStage.show();
    }

    //Sets up the menus
    public MenuBar setMenus() {
        Menu file = new Menu("File");
        MenuItem zoom = new MenuItem("Zoom");
        file.getItems().add(zoom);
        //Handles the zoom in/out dialog
        zoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Zoom control");
                dialog.setHeaderText("Choose your range");
                ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(confirm, ButtonType.CANCEL);

                GridPane gridPane = new GridPane();
                TextField upperLimit = new TextField();
                upperLimit.setPromptText("Upper Limit");
                TextField lowerLimit = new TextField();
                lowerLimit.setPromptText("Lower Limit");

                upperLimit.setText(draw.xLimU + "");
                lowerLimit.setText(draw.xLimL + "");
                gridPane.add(upperLimit, 0, 0);
                gridPane.add(lowerLimit, 0, 1);

                dialog.getDialogPane().setContent(gridPane);
                dialog.setResultConverter(buttonType -> {
                    if (buttonType == confirm)
                        return new Pair<>(upperLimit.getText(), lowerLimit.getText());
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();
                result.ifPresent(limits -> {
                    draw.updateRange(Double.parseDouble(limits.getKey()), Double.parseDouble(limits.getValue()));
                    setTask();
                });
            }
        });

        Menu view = new Menu("View");
        return new MenuBar(file, view);
    }
}
