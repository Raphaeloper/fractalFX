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
        setTask();
        setUI();
    }

    public void setTask() {
        Calculator task = new Calculator(draw);
        task.setOnSucceeded((succeededEvent) -> {
            draw.screen = task.getValue();
            imageView.setImage(draw.paint());
        });
        task.setOnFailed((workerStateEvent -> {
            System.out.println(task);
        }));

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();
    }

    public Alert setDialog() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Loading...");
        alert.setGraphic(null);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.setOnCloseRequest(dialogEvent ->
                alert.close());
        alert.setResizable(false);
        alert.getButtonTypes().clear();
        alert.getDialogPane().setPrefSize(480, 170);
        return alert;
    }

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

    public MenuBar setMenus() {
        Menu file = new Menu("File");
        MenuItem zoom = new MenuItem("Zoom");
        file.getItems().add(zoom);
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
