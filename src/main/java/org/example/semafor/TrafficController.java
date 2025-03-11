package org.example.semafor;

import javafx.animation.TranslateTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficController {

    @FXML
    private GridPane vehicle1;
    @FXML
    private GridPane vehicle2;

    @FXML
    private GridPane om1;
    @FXML
    private GridPane om2;

    @FXML
    private Circle greenLight;
    @FXML
    private Circle yellowLight;
    @FXML
    private Circle redLight;

    @FXML
    private Circle red1;
    @FXML
    private Circle green1;
    @FXML
    private Circle red2;
    @FXML
    private Circle green2;

    private Timer movementTimer;
    private Timer trafficLightTimer;
    private Timer pedestrianTimer; // Timer pentru pietoni

    private String currentLight = "GREEN";
    private boolean pedestriansAnimated = false;

    public void initialize() {
        startVehicleMovement();
        startTrafficLightCycle();
        startPedestrianCycle(); //  Adăugat timer-ul pietonilor
    }

    private void startVehicleMovement() {
        movementTimer = new Timer();
        movementTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if ("GREEN".equals(currentLight)) {
                        vehicle1.setLayoutY(vehicle1.getLayoutY() - 6);
                        if (vehicle1.getLayoutY() < -200) {
                            vehicle1.setLayoutY(600);
                        }
                        vehicle2.setLayoutY(vehicle2.getLayoutY() + 6);
                        if (vehicle2.getLayoutY() > 600) {
                            vehicle2.setLayoutY(-200);
                        }
                    } else if ("YELLOW".equals(currentLight)) {
                        vehicle1.setLayoutY(vehicle1.getLayoutY() - 6);
                        if (vehicle1.getLayoutY() <= 473) {
                            vehicle1.setLayoutY(473);
                        }
                        vehicle2.setLayoutY(vehicle2.getLayoutY() + 6);
                        if (vehicle2.getLayoutY() >= -150) {
                            vehicle2.setLayoutY(-150);
                        }
                    } else if ("RED".equals(currentLight)) {
                        if (vehicle1.getLayoutY() < 473) {
                            vehicle1.setLayoutY(473);
                        }
                        if (vehicle2.getLayoutY() > -150) {
                            vehicle2.setLayoutY(-150);
                        }
                    }
                });
            }
        }, 0, 50);
    }

    private void startTrafficLightCycle() {
        trafficLightTimer = new Timer();
        trafficLightTimer.scheduleAtFixedRate(new TimerTask() {
            private int elapsedSeconds = 0;

            @Override
            public void run() {
                Platform.runLater(() -> {
                    elapsedSeconds++;
                    if (elapsedSeconds <= 6) {
                        switchToGreen();
                    } else if (elapsedSeconds <= 12) {
                        switchToYellow();
                    } else if (elapsedSeconds <= 17) {
                        switchToRed();
                    } else if (elapsedSeconds <= 22) {
                        switchToYellow();
                    } else {
                        elapsedSeconds = 0;
                        switchToGreen();
                    }
                });
            }
        }, 0, 1000);
    }

    private void startPedestrianCycle() {
        pedestrianTimer = new Timer();
        pedestrianTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if ("RED".equals(currentLight) && !pedestriansAnimated) {
                        animatePedestrians();
                        pedestriansAnimated = true;
                    }
                });
            }
        }, 0, 1000);
    }

    private void switchToGreen() {
        currentLight = "GREEN";

        greenLight.setFill(Paint.valueOf("#22FF40"));
        yellowLight.setFill(Paint.valueOf("#6f731f"));
        redLight.setFill(Paint.valueOf("#842c2c"));

        red1.setFill(Paint.valueOf("#FF3333"));
        green1.setFill(Paint.valueOf("#347620"));
        red2.setFill(Paint.valueOf("#FF3333"));
        green2.setFill(Paint.valueOf("#347620"));

        pedestriansAnimated = false;
    }

    private void switchToYellow() {
        currentLight = "YELLOW";

        greenLight.setFill(Paint.valueOf("#347620"));
        yellowLight.setFill(Paint.valueOf("#FFFF00"));
        redLight.setFill(Paint.valueOf("#842c2c"));

        red1.setFill(Paint.valueOf("#FF3333"));
        green1.setFill(Paint.valueOf("#347620"));
        red2.setFill(Paint.valueOf("#FF3333"));
        green2.setFill(Paint.valueOf("#347620"));

        pedestriansAnimated = false;
    }

    private void switchToRed() {
        currentLight = "RED";

        greenLight.setFill(Paint.valueOf("#347620"));
        yellowLight.setFill(Paint.valueOf("#6f731f"));
        redLight.setFill(Paint.valueOf("#FF3333"));

        red1.setFill(Paint.valueOf("#6f731f"));
        green1.setFill(Paint.valueOf("#22FF40"));
        red2.setFill(Paint.valueOf("#6f731f"));
        green2.setFill(Paint.valueOf("#22FF40"));
    }

    private void animatePedestrians() {
        double targetDistance = 650;

        double newXOm1 = (om1.getTranslateX() == 0 ? targetDistance : 0);
        TranslateTransition transitionOm1 = new TranslateTransition(Duration.seconds(3), om1);
        transitionOm1.setToX(newXOm1);

        RotateTransition rotateOm1 = new RotateTransition(Duration.seconds(0.5), om1);
        rotateOm1.setAxis(Rotate.Y_AXIS);
        rotateOm1.setToAngle(newXOm1 > om1.getTranslateX() ? 0 : 180);

        ParallelTransition parallelOm1 = new ParallelTransition(om1, transitionOm1, rotateOm1);
        parallelOm1.play();

        double newXOm2 = (om2.getTranslateX() == 0 ? targetDistance : 0);
        TranslateTransition transitionOm2 = new TranslateTransition(Duration.seconds(3), om2);
        transitionOm2.setToX(newXOm2);

        RotateTransition rotateOm2 = new RotateTransition(Duration.seconds(0.5), om2);
        rotateOm2.setAxis(Rotate.Y_AXIS);
        rotateOm2.setToAngle(newXOm2 > om2.getTranslateX() ? 0 : 180);

        ParallelTransition parallelOm2 = new ParallelTransition(om2, transitionOm2, rotateOm2);
        parallelOm2.play();
    }
}
