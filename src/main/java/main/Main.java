package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;

/**
 *  Created by Demishev on 29.04.14.
 */
public class Main extends Application {
    public static final int SIZE = 200;
    private final SmallNumberHolder hXHolder = new SmallNumberHolder(0);
    private final SmallNumberHolder hYHolder = new SmallNumberHolder(0);
    private final SmallNumberHolder hZHolder = new SmallNumberHolder(1);
    private final SmallNumberHolder χHolder = new SmallNumberHolder(1);
    private final SmallNumberHolder aHolder = new SmallNumberHolder(1);
    private final SmallNumberHolder bHolder = new SmallNumberHolder(0.1);
    private final SmallNumberHolder particleVolumeHolder = new SmallNumberHolder(0.01);
    private final SmallNumberHolder ηHolder = new SmallNumberHolder(1);
    private final SmallNumberHolder liquidVelocityHolder = new SmallNumberHolder(1);
    private final SmallNumberHolder zHolder = new SmallNumberHolder(-8);
    private final SmallNumberHolder particleXHolder = new SmallNumberHolder(5);
    private final SmallNumberHolder particleYHolder = new SmallNumberHolder(10);
    private final SmallNumberHolder particleZHolder = new SmallNumberHolder(-8);
    private SmallNumberHolder mXHolder = new SmallNumberHolder(1);
    private SmallNumberHolder mYHolder = new SmallNumberHolder(1);
    private SmallNumberHolder mZHolder = new SmallNumberHolder(1);
    private GraphicsContext capturedAreaGraphicsContext;
    private GraphicsContext singleParticleGraphicsContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas figureCanvas = new Canvas(SIZE, SIZE);
        capturedAreaGraphicsContext = figureCanvas.getGraphicsContext2D();

        Canvas singleParticleCanvas = new Canvas(SIZE, SIZE);
        singleParticleGraphicsContext = singleParticleCanvas.getGraphicsContext2D();

        Calculator calculator = new Calculator(SIZE / 2, new RoundFunction(8, 12, -48));

        ResultSet result = calculator.calculate(0);

        BorderPane mainPane = new BorderPane();

        HBox paintBox = new HBox();
        paintBox.getChildren().add(figureCanvas);

        paintBox.getChildren().add(getSingleParticleField(singleParticleCanvas));

        mainPane.setRight(paintBox);
        mainPane.setCenter(displaySettings());
        mainPane.setBottom(displayResults(result));
        root.getChildren().add(mainPane);

        drawFigure(result);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Node getSingleParticleField(Canvas singleParticleCanvas) {
        HBox startPosition = new HBox(new Label("Start: ("), particleXHolder, new Label(","), particleYHolder, new Label(","), particleZHolder, new Label(")"));

        Button calculateButton = new Button("Calculate");

        calculateButton.setOnMouseClicked(event -> {
            final double ballRadius = bHolder.getValue();

            Calculator calculator = new Calculator(new RoundFunction(10));

            ResultSet result = calculator.getTrajectory(particleXHolder.getValue(), particleYHolder.getValue(), particleZHolder.getValue());

            final double horizontalScale = SIZE / (result.getMaxX() - result.getMinX()) / 1.5;
            final double verticalScale = SIZE / (result.getMaxZ() - result.getMinZ()) / 1.5;

            final double horizontalDelta = -result.getMinX() * horizontalScale + 100 * 1.5;
            final double verticalDelta = -result.getMaxZ() * verticalScale + 15 * 1.5;

            singleParticleGraphicsContext.setFill(Color.GREEN);
            singleParticleGraphicsContext.strokeOval(horizontalDelta, -5 * verticalScale - verticalDelta, horizontalScale * ballRadius, verticalScale * ballRadius);

            System.out.println("Bla");
        });

        return new VBox(singleParticleCanvas, startPosition, calculateButton);
    }

    private Node displayResults(ResultSet result) {
        FlowPane flowPane = new FlowPane();

        flowPane.getChildren().addAll(new Label("Center is: (" + result.getxCenter() + "," + result.getyCenter() + ")"),
                new Label("Scale is: " + result.getScale()));

        return flowPane;
    }

    private VBox displaySettings() {
        VBox vbox = new VBox();

        Button startButton = new Button("Start");

        startButton.setOnMouseClicked(event -> {
            try {
                final double χ = χHolder.getValue();
                final double η = ηHolder.getValue();
                final double a = aHolder.getValue();
                final double b = bHolder.getValue();

                final double mX = mXHolder.getValue();
                final double mY = mYHolder.getValue();
                final double mZ = mZHolder.getValue();

                final double hX = hXHolder.getValue();
                final double hY = hYHolder.getValue();
                final double hZ = hZHolder.getValue();

                final double particleVolume = particleVolumeHolder.getValue();
                final double liquidVelocity = liquidVelocityHolder.getValue();


                Calculator calculator = new Calculator(20, new ParallelFunction(χ, η, a, b, mX, mY, mZ, hX, hY, hZ, particleVolume, liquidVelocity));
                ResultSet result = calculator.calculate(zHolder.getValue());

                capturedAreaGraphicsContext.restore();

                drawFigure(result);

            } catch (Exception ignored) {

            }
        });

        vbox.getChildren().addAll(systemParams(), startButton);

        return vbox;
    }

    private void drawFigure(ResultSet result) {
        this.capturedAreaGraphicsContext.setFill(Color.GREEN);

        final double scale = result.getScale();
        final double xCenter = result.getxCenter();
        final double yCenter = result.getyCenter();

        result.getPoints().stream().filter(Point::isCaptured).forEach(p -> {
            int x = (int) ((p.getX() - xCenter + scale) * SIZE / scale / 2);
            int y = (int) (SIZE - (p.getY() - yCenter + scale) * SIZE / scale / 2);
            this.capturedAreaGraphicsContext.fillOval(x, y, 1, 1);
        });

        drawXScale(result);
    }

    private void drawXScale(ResultSet result) {
        capturedAreaGraphicsContext.setFill(Color.BLACK);

        int xScaleTop = SIZE - 15;
        capturedAreaGraphicsContext.strokeLine(0, xScaleTop, SIZE - 5, xScaleTop);

        capturedAreaGraphicsContext.strokeLine(SIZE - 5, xScaleTop, SIZE - 10, xScaleTop - 5);
        capturedAreaGraphicsContext.strokeLine(SIZE - 5, xScaleTop, SIZE - 10, xScaleTop + 5);

        capturedAreaGraphicsContext.fillText(String.format("%.2f", result.getxCenter() - result.getScale()), 10, xScaleTop + 13);
        capturedAreaGraphicsContext.fillText(String.format("%.2f", result.getxCenter() + result.getScale()), SIZE - 30, xScaleTop + 13);


        capturedAreaGraphicsContext.strokeLine(5, 0, 5, SIZE);
        capturedAreaGraphicsContext.strokeLine(5, 0, 0, 5);
        capturedAreaGraphicsContext.strokeLine(5, 0, 10, 5);

        capturedAreaGraphicsContext.fillText(String.format("%.2f", result.getyCenter() - result.getScale()), 5, SIZE - 20);
        capturedAreaGraphicsContext.fillText(String.format("%.2f", result.getyCenter() + result.getScale()), 5, 15);
    }

    private Node systemParams() {
        HBox magnetisationBox = new HBox(new Label("M: ("), mXHolder, new Label(","), mYHolder, new Label(","), mZHolder, new Label(")"));
        HBox extFieldBox = new HBox(new Label("H: ("), hXHolder, new Label(","), hYHolder, new Label(","), hZHolder, new Label(")"));
        HBox χBox = new HBox(new Label("χ: "), χHolder);
        HBox ballRadiusBox = new HBox(new Label("a: "), aHolder);
        HBox particleRadiusBox = new HBox(new Label("b: "), bHolder);
        HBox particleVolumeBox = new HBox(new Label("Vнф: "), particleVolumeHolder);
        HBox ηBox = new HBox(new Label("η: "), ηHolder);
        HBox liquidVelocityBox = new HBox(new Label("V0: ", liquidVelocityHolder));
        HBox defaultZBox = new HBox(new Label("z: "), zHolder);

        return new VBox(magnetisationBox, extFieldBox, χBox, ballRadiusBox, particleRadiusBox, particleVolumeBox, ηBox, liquidVelocityBox, defaultZBox);
    }

    class SmallNumberHolder extends TextField {
        SmallNumberHolder(double defaultValue) {
            super(String.format("%.2f", defaultValue));

            setMaxWidth(55);

            setOnKeyReleased(event -> {
                try {
                    Double.valueOf(getText());
                    setStyle("-fx-background-color: beige");
                } catch (NumberFormatException e) {
                    setStyle("-fx-background-color: red");
                }
            });
        }

        double getValue() {
            return Double.valueOf(getText());
        }
    }
}
