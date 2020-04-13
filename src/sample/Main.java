package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {
    ImageView imageView = new ImageView();
    Draw draw;
    Stage pStage;
    int threadNum;
    boolean stopJulia = false;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        threadNum = Thread.activeCount();
        pStage = primaryStage;
        pStage.getIcons().add(new Image("file:icon.png"));
        setUI();
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
        zoom.setDisable(false);
        //Handles the zoom in/out dialog
        zoom.setOnAction(actionEvent -> {
            stopJulia = true;
            Dialog<Pair<String, String>> dialog = createZoomDialog();
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(limits -> {
                draw.updateRange(Double.parseDouble(limits.getKey()), Double.parseDouble(limits.getValue()));
                setMandelbrotTask();
            });
        });

        Menu view = new Menu("View");
        MenuItem mandelbrot = new MenuItem("Mandelbrot");
        MenuItem julia = new MenuItem("Julia's set");
        view.getItems().addAll(mandelbrot, julia);
        mandelbrot.setOnAction(actionEvent -> {
            initDraw(Calculator.M_SET);
            setMandelbrotTask();
        });
        julia.setOnAction(actionEvent -> {
            initDraw(Calculator.J_SET);
            setJuliaTask(new Complex(0.788, 0));
            stopJulia = false;
        });
        return new MenuBar(file, view);
    }

    public Dialog<Pair<String, String>> createZoomDialog() {
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
        return dialog;
    }

    //Creates and launches calculation threads
    public void setJuliaTask(Complex c) {
        Calculator task;
        ExecutorService executorService = Executors.newFixedThreadPool(25);
        task = new Calculator(draw, new Complex(c));
        task.setOnSucceeded((succeededEvent) -> {
            for (double y = draw.yLimL; y <= draw.yLimU; y += draw.diff) {
                updateImage(y);
            }
            c.update(c.rad + 0.1);
            System.out.println(c);
            if (c.rad <= 2 * Math.PI && !stopJulia)
                setJuliaTask(c);
        });

        executorService.execute(task);
        executorService.shutdown();
    }

    //Creates and launches calculation threads
    public void setMandelbrotTask() {
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

    public void initDraw(int set) {
        draw = new Draw();
        if (set == Calculator.M_SET) {
            draw.updateRange(4, -4);
        } else if (set == Calculator.J_SET) {
            draw.updateRange(2.5, -2.5);
        }
    }

    //Outputs the row to an image
    public void updateImage(double y) {
        int graphY;
        graphY = draw.originY + (int) (y * draw.magSize);
        imageView.setImage(draw.paint(graphY));
    }
}
