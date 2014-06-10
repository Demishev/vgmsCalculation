package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Calculator;
import model.Point;
import model.ResultSet;
import model.physicalModels.Function;
import model.physicalModels.ParallelFunction;
import model.physicalModels.RoundFunction;

/**
 *  Created by Demishev on 29.04.14.
 */
public class Main extends Application {
    public static final int SIZE = 400;
    public final ComboBox ACTIVE_MODEL_COMBO_BOX = new ComboBox(FXCollections.observableArrayList("parallel model", "round model"));
    private final SmallNumberHolder hXHolder = new SmallNumberHolder(0);
    private final SmallNumberHolder hYHolder = new SmallNumberHolder(0);
    private final SmallNumberHolder hZHolder = new SmallNumberHolder(3000);
    private final SmallNumberHolder χHolder = new SmallNumberHolder(10E-4);
    private final SmallNumberHolder aHolder = new SmallNumberHolder(0.1);
    private final SmallNumberHolder bHolder = new SmallNumberHolder(0.02);
    private final SmallNumberHolder particleVolumeHolder = new SmallNumberHolder(0.001);
    private final SmallNumberHolder ηHolder = new SmallNumberHolder(0.01);
    private final SmallNumberHolder liquidVelocityHolder = new SmallNumberHolder(1);
    private final SmallNumberHolder zHolder = new SmallNumberHolder(-1);
    private final SmallNumberHolder particleXHolder = new SmallNumberHolder(5);
    private final SmallNumberHolder particleYHolder = new SmallNumberHolder(10);
    private final SmallNumberHolder particleZHolder = new SmallNumberHolder(-8);
    private SmallNumberHolder mXHolder = new SmallNumberHolder(0);
    private SmallNumberHolder mYHolder = new SmallNumberHolder(0);
    private SmallNumberHolder mZHolder = new SmallNumberHolder(1000);
    private GraphicsContext capturedAreaGraphicsContext;
    private GraphicsContext singleParticleGraphicsContext;
    private Label resultLabel;
    private SmallNumberHolder radiusHolder = new SmallNumberHolder(10);
    private VBox systemParamsHolder = new VBox(parallelPhysModelSystemParams());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VGMS calculation");
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
        mainPane.setBottom(displayResults());
        root.getChildren().add(mainPane);

        drawFigure(result);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Node getSingleParticleField(Canvas singleParticleCanvas) {
        HBox startPosition = new HBox(new Label("Start: ("), particleXHolder, new Label(","), particleYHolder, new Label(","), particleZHolder, new Label(")"));

        Button calculateButton = new Button("Calculate");

        calculateButton.setOnMouseClicked(event -> {

            final Function activeFunction = getActiveFunction();
            Calculator calculator = new Calculator(activeFunction);

            ResultSet result = calculator.getTrajectory(particleXHolder.getValue(), particleYHolder.getValue(), particleZHolder.getValue());
            final double ballRadius = activeFunction.getBallRadius();

            final double horizontalScale = SIZE / (4 * ballRadius);
            final double verticalScale = SIZE / (4 * ballRadius);

            final double horizontalDelta = 200;
            final double verticalDelta = 50;


            singleParticleGraphicsContext.setFill(Color.WHITE);
            singleParticleGraphicsContext.fillRect(0, 0, SIZE, SIZE);

            singleParticleGraphicsContext.setFill(Color.GREEN);
            singleParticleGraphicsContext.fillOval(horizontalDelta - ballRadius * horizontalScale, verticalDelta - ballRadius * verticalScale, horizontalScale * ballRadius * 2, verticalScale * ballRadius * 2);

            singleParticleGraphicsContext.setFill(Color.BLACK);
            singleParticleGraphicsContext.strokeLine(0, 50, SIZE, 50);
            singleParticleGraphicsContext.strokeLine(200, 0, 200, 400);

            result.getPoints().stream().forEach(point -> {
                System.out.println("point x,z: " + point.getX() + " " + point.getZ());
                final double newX = horizontalScale * point.getX() + horizontalDelta;
                final double newZ = -verticalScale * point.getZ() + verticalDelta;
                System.out.println("point new x,z: " + newX + " " + newZ);
                singleParticleGraphicsContext.fillRect(newX, newZ, 1, 1);
            });

            singleParticleGraphicsContext.setFill(Color.AQUA);
            System.out.println("hor" + horizontalScale + "ver: " + verticalScale + "hor: " + horizontalDelta + "ver: " + verticalDelta);
        });

        return new VBox(singleParticleCanvas, startPosition, calculateButton);
    }

    private Node displayResults() {
        FlowPane flowPane = new FlowPane();

        resultLabel = new Label();

        flowPane.getChildren().addAll(resultLabel);

        return flowPane;
    }

    private void setResultText(ResultSet result) {
        resultLabel.setText("Center is: (" + result.getxCenter() + "," + result.getyCenter() + ")Scale is: " + result.getScale());
    }

    private VBox displaySettings() {
        VBox vbox = new VBox();

        Button startButton = new Button("Start");

        startButton.setOnMouseClicked(event -> {
            try {
                final Function function = getActiveFunction();
                Calculator calculator = new Calculator(20, function);
                ResultSet result = calculator.calculate(zHolder.getValue());

                capturedAreaGraphicsContext.setFill(Color.WHITE);
                capturedAreaGraphicsContext.fillRect(0, 0, SIZE, SIZE);

                drawFigure(result);
                displayResults();

            } catch (Exception ignored) {

            }
        });

        vbox.getChildren().addAll(systemParams(), startButton);

        return vbox;
    }

    private Function getActiveFunction() {
        return (ACTIVE_MODEL_COMBO_BOX.getValue() == "parallel model") ? getParallelFunction() : getRoundFunction();
    }

    private Function getRoundFunction() {
        return new RoundFunction(radiusHolder.getValue());
    }

    private Function getParallelFunction() {
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

        return new ParallelFunction(χ, η, a, b, mX, mY, mZ, hX, hY, hZ, particleVolume, liquidVelocity);
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

        setResultText(result);

        drawScales(result);
    }

    private void drawScales(ResultSet result) {
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
        ACTIVE_MODEL_COMBO_BOX.setValue("parallel model");

        ACTIVE_MODEL_COMBO_BOX.setOnAction(event -> {
            systemParamsHolder.getChildren().remove(0);
            if (ACTIVE_MODEL_COMBO_BOX.getValue() == "parallel model") {
                systemParamsHolder.getChildren().add(parallelPhysModelSystemParams());
            } else {
                systemParamsHolder.getChildren().add(roundPhysModelSystemParams());
            }
        });

        return new VBox(ACTIVE_MODEL_COMBO_BOX,
                systemParamsHolder);
    }


    private Node roundPhysModelSystemParams() {
        return new VBox(new HBox(new Label("a: "), radiusHolder));
    }

    private Node parallelPhysModelSystemParams() {
        return new VBox(
                new HBox(new Label("M: ("), mXHolder, new Label(","), mYHolder, new Label(","), mZHolder, new Label(")")),
                new HBox(new Label("H: ("), hXHolder, new Label(","), hYHolder, new Label(","), hZHolder, new Label(")")),
                new HBox(new Label("χ: "), χHolder),
                new HBox(new Label("a: "), aHolder),
                new HBox(new Label("b: "), bHolder),
                new HBox(new Label("Vнф: "), particleVolumeHolder),
                new HBox(new Label("η: "), ηHolder),
                new HBox(new Label("V0: "), liquidVelocityHolder),
                new HBox(new Label("z: "), zHolder)
        );
    }

    class SmallNumberHolder extends TextField {
        private double value;

        SmallNumberHolder(double defaultValue) {
            super();
            this.value = defaultValue;

            updateValue();

            setMaxWidth(55);

            setOnKeyReleased(event -> {
                try {
                    final Double newValue = Double.valueOf(getText());

                    if (newValue != value) {
                        this.value = newValue;
                        setStyle("-fx-background-color: beige");
                    }
                } catch (NumberFormatException e) {
                    setStyle("-fx-background-color: red");
                }
            });
        }

        private void updateValue() {
            setText(String.format("%.3f", value));
        }

        double getValue() {
            return value;
        }
    }
}
