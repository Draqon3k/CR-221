package org.example.semafor;

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
    private Circle greenLight;
    @FXML
    private Circle yellowLight;
    @FXML
    private Circle redLight;

    private Timer movementTimer;
    private Timer trafficLightTimer;
    private String currentLight = "GREEN"; // Track current light state

    public void initialize() {
        startVehicleMovement(); // Start vehicle movement
        startTrafficLightCycle(); // Start traffic light cycle
    }

    private void startVehicleMovement() {
        movementTimer = new Timer();
        movementTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if ("GREEN".equals(currentLight)) {
                        // Vehicles move normally
                        vehicle1.setLayoutY(vehicle1.getLayoutY() - 6);
                        if (vehicle1.getLayoutY() < -200) {
                            vehicle1.setLayoutY(600); // Reset vehicle1
                        }

                        vehicle2.setLayoutY(vehicle2.getLayoutY() + 6);
                        if (vehicle2.getLayoutY() > 600) {
                            vehicle2.setLayoutY(-200); // Reset vehicle2
                        }
                    } else if ("YELLOW".equals(currentLight)) {
                        vehicle1.setLayoutY(vehicle1.getLayoutY() - 6);
                        if (vehicle1.getLayoutY() <= 473) {
                            vehicle1.setLayoutY(473); // Stop near line
                        }

                        vehicle2.setLayoutY(vehicle2.getLayoutY() + 6);
                        if (vehicle2.getLayoutY() >= -150) {
                            vehicle2.setLayoutY(-150); // Stop at line
                        }
                    } else if ("RED".equals(currentLight)) {
                        // Vehicles stop at the respective stopping lines
                        if (vehicle1.getLayoutY() < 473) {
                            vehicle1.setLayoutY(473); // Stop before stop line
                        }
                        if (vehicle2.getLayoutY() > -150) {
                            vehicle2.setLayoutY(-150); // Stop before stop line
                        }
                    }
                });
            }
        }, 0, 50); // Execute every 50 milliseconds
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
                        switchToGreen(); // Green light for 5 seconds
                    } else if (elapsedSeconds <= 12) {
                        switchToYellow(); // Yellow light for 7 seconds
                    } else if (elapsedSeconds <= 17) {
                        switchToRed(); // Red light for 5 seconds

                    } else if (elapsedSeconds <= 22.5) {
                        switchToYellow(); // Yellow light for 7 seconds
                    } else {
                        elapsedSeconds = 0; // Restart the cycle
                        switchToGreen();
                    }
                });
            }
        }, 0, 1000); // Execute every 1 second
    }

    private void switchToGreen() {
        currentLight = "GREEN";

        // Update light colors
        greenLight.setFill(Paint.valueOf("#22FF40")); // Bright green
        yellowLight.setFill(Paint.valueOf("#6f731f")); // Normal yellow
        redLight.setFill(Paint.valueOf("#842c2c")); // Normal red
    }

    private void switchToYellow() {
        currentLight = "YELLOW";

        // Update light colors
        greenLight.setFill(Paint.valueOf("#347620")); // Normal green
        yellowLight.setFill(Paint.valueOf("#FFFF00")); // Bright yellow
        redLight.setFill(Paint.valueOf("#842c2c")); // Normal red
    }

    private void switchToRed() {
        currentLight = "RED";

        // Update light colors
        greenLight.setFill(Paint.valueOf("#347620")); // Normal green
        yellowLight.setFill(Paint.valueOf("#6f731f")); // Normal yellow
        redLight.setFill(Paint.valueOf("#FF3333")); // Bright red
    }
}