import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Calculator;
import model.CapturedArea;
import model.Point;
import model.RoundFunction;

import java.util.ArrayList;
import java.util.List;

/**
 *  Created by Demishev on 29.04.14.
 */
public class Main extends Application {
    public static final int SIZE = 200;
    private GraphicsContext gc;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas figureCanvas = new Canvas(SIZE, SIZE);
        gc = figureCanvas.getGraphicsContext2D();

        Calculator calculator = new Calculator(SIZE / 2, new RoundFunction(8, 12, -48));

        CapturedArea result = calculator.calculate();

        BorderPane mainPane = new BorderPane();
        mainPane.setRight(figureCanvas);
        mainPane.setCenter(displaySettings(result));
        mainPane.setBottom(displayResults(result));
        root.getChildren().add(mainPane);

        drawFigure(result);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Node displayResults(CapturedArea result) {
        FlowPane flowPane = new FlowPane();

        List<Node> results = new ArrayList<>();
        results.add(new Label("I am the best VGMS application!"));
        results.add(new Label("Center is: (" + result.getxCenter() + "," + result.getyCenter() + ")"));
        results.add(new Label("Scale is: " + result.getScale()));

        flowPane.getChildren().addAll(results);

        return flowPane;
    }

    private VBox displaySettings(CapturedArea result) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        vbox.getChildren().add(new Button("Hello"));

        return vbox;
    }

    private void drawFigure(CapturedArea result) {
        this.gc.setFill(Color.GREEN);

        final double scale = result.getScale();
        final double xCenter = result.getxCenter();
        final double yCenter = result.getyCenter();

        result.getPoints().stream().filter(Point::isCaptured).forEach(p -> {
            int x = (int) ((p.getX() - xCenter + scale) * SIZE / scale / 2);
            int y = (int) ((p.getY() - yCenter + scale) * SIZE / scale / 2);
            this.gc.fillOval(x, y, 1, 1);
        });

        this.gc.setFill(Color.BLUE);
    }
}
