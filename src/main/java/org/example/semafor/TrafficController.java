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

    @FXML
    private Circle red1; // Primul semafor pietoni - roșu
    @FXML
    private Circle green1; // Primul semafor pietoni - verde

    @FXML
    private Circle red2; // Al doilea semafor pietoni - roșu
    @FXML
    private Circle green2; // Al doilea semafor pietoni - verde

    private Timer movementTimer;
    private Timer trafficLightTimer;
    private String currentLight = "GREEN"; // Stare actuală a luminii semaforului

    public void initialize() {
        startVehicleMovement(); // Pornește mișcarea vehiculelor
        startTrafficLightCycle(); // Pornește ciclul semaforului
    }

    private void startVehicleMovement() {
        movementTimer = new Timer();
        movementTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if ("GREEN".equals(currentLight)) {
                        // Vehiculele se mișcă normal
                        vehicle1.setLayoutY(vehicle1.getLayoutY() - 6);
                        if (vehicle1.getLayoutY() < -200) {
                            vehicle1.setLayoutY(600); // Resetare pentru vehiculul 1
                        }

                        vehicle2.setLayoutY(vehicle2.getLayoutY() + 6);
                        if (vehicle2.getLayoutY() > 600) {
                            vehicle2.setLayoutY(-200); // Resetare pentru vehiculul 2
                        }
                    } else if ("YELLOW".equals(currentLight)) {
                        vehicle1.setLayoutY(vehicle1.getLayoutY() - 6);
                        if (vehicle1.getLayoutY() <= 473) {
                            vehicle1.setLayoutY(473); // Oprire lângă linie
                        }

                        vehicle2.setLayoutY(vehicle2.getLayoutY() + 6);
                        if (vehicle2.getLayoutY() >= -150) {
                            vehicle2.setLayoutY(-150); // Oprire lângă linie
                        }
                    } else if ("RED".equals(currentLight)) {
                        // Vehiculele se opresc la liniile respective
                        if (vehicle1.getLayoutY() < 473) {
                            vehicle1.setLayoutY(473); // Oprire înainte de linia de oprire
                        }
                        if (vehicle2.getLayoutY() > -150) {
                            vehicle2.setLayoutY(-150); // Oprire înainte de linia de oprire
                        }
                    }
                });
            }
        }, 0, 50); // Executare la fiecare 50 milisecunde
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
                        switchToGreen(); // Lumina verde timp de 6 secunde
                    } else if (elapsedSeconds <= 12) {
                        switchToYellow(); // Lumina galbenă timp de 6 secunde
                    } else if (elapsedSeconds <= 17) {
                        switchToRed(); // Lumina roșie timp de 5 secunde
                    } else if (elapsedSeconds <= 22) {
                        switchToYellow(); // Lumina galbenă timp de 5 secunde
                    } else {
                        elapsedSeconds = 0; // Reînceperea ciclului
                        switchToGreen();
                    }
                });
            }
        }, 0, 1000); // Executare la fiecare secundă
    }

    private void switchToGreen() {
        currentLight = "GREEN";

        // Actualizează culorile semaforului pentru vehicule
        greenLight.setFill(Paint.valueOf("#22FF40")); // Verde aprins
        yellowLight.setFill(Paint.valueOf("#6f731f")); // Galben normal
        redLight.setFill(Paint.valueOf("#842c2c")); // Roșu normal

        // Actualizează culorile primului semafor pietonal
        red1.setFill(Paint.valueOf("#FF3333")); // Roșu aprins (oprire pentru pietoni)
        green1.setFill(Paint.valueOf("#347620")); // Verde întunecat (neactiv)

        // Actualizează culorile celui de-al doilea semafor pietonal
        red2.setFill(Paint.valueOf("#FF3333")); // Roșu aprins (oprire pentru pietoni)
        green2.setFill(Paint.valueOf("#347620")); // Verde întunecat (neactiv)
    }

    private void switchToYellow() {
        currentLight = "YELLOW";

        // Actualizează culorile semaforului pentru vehicule
        greenLight.setFill(Paint.valueOf("#347620")); // Verde normal
        yellowLight.setFill(Paint.valueOf("#FFFF00")); // Galben aprins
        redLight.setFill(Paint.valueOf("#842c2c")); // Roșu normal

        // Actualizează culorile primului semafor pietonal
        red1.setFill(Paint.valueOf("#FF3333")); // Roșu aprins (oprire pentru pietoni)
        green1.setFill(Paint.valueOf("#347620")); // Verde întunecat (neactiv)

        // Actualizează culorile celui de-al doilea semafor pietonal
        red2.setFill(Paint.valueOf("#FF3333")); // Roșu aprins (oprire pentru pietoni)
        green2.setFill(Paint.valueOf("#347620")); // Verde întunecat (neactiv)
    }

    private void switchToRed() {
        currentLight = "RED";

        // Actualizează culorile semaforului pentru vehicule
        greenLight.setFill(Paint.valueOf("#347620")); // Verde normal
        yellowLight.setFill(Paint.valueOf("#6f731f")); // Galben normal
        redLight.setFill(Paint.valueOf("#FF3333")); // Roșu aprins

        // Actualizează culorile primului semafor pietonal
        red1.setFill(Paint.valueOf("#6f731f")); // Roșu întunecat (neactiv)
        green1.setFill(Paint.valueOf("#22FF40")); // Verde aprins (traversare pentru pietoni)

        // Actualizează culorile celui de-al doilea semafor pietonal
        red2.setFill(Paint.valueOf("#6f731f")); // Roșu întunecat (neactiv)
        green2.setFill(Paint.valueOf("#22FF40")); // Verde aprins (traversare pentru pietoni)
    }
}