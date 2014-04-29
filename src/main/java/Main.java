import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Calculator;
import model.CapturedArea;
import model.Point;
import model.RoundFunction;

/**
 * Created by konstantin on 29.04.14.
 */
public class Main extends Application {

    public static final int SIZE = 200;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(SIZE, SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        Calculator calculator = new Calculator(SIZE / 2, new RoundFunction(8, 12, -48));

        CapturedArea result = calculator.calculate();


        drawShapes(gc, result);


        BorderPane border = new BorderPane();
        border.setRight(canvas);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        border.setCenter(vbox);
        vbox.getChildren().add(new Label("I am the best VGMS application!"));
        vbox.getChildren().add(new Label("Center is: (" + result.getxCenter() + "," + result.getyCenter() + ")"));
        vbox.getChildren().add(new Label("Scale is: " + result.getScale()));
        vbox.getChildren().add(new Button("Hello"));

        root.getChildren().add(border);

        root.minWidth(400);
        root.minHeight(250);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc, CapturedArea result) {


        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

        final double scale = result.getScale();
        final double xCenter = result.getxCenter();
        final double yCenter = result.getyCenter();

        result.getPoints().stream().filter(Point::isCaptured).forEach(p -> {
            int x = (int) ((p.getX() - xCenter + scale) * SIZE / scale / 2);
            int y = (int) ((p.getY() - yCenter + scale) * SIZE / scale / 2);
            gc.fillOval(x, y, 1, 1);
        });

        gc.setFill(Color.BLUE);


        gc.fillOval(0, 0, 10, 10);


    }
}
