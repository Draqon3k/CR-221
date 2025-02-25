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
import javafx.util.Duration;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficController {

    @FXML
    private GridPane vehicle1;
    @FXML
    private GridPane vehicle2;

    // Elementele pentru pietoni
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

    // Semafor pietonal
    @FXML
    private Circle red1; // Semafor pietonal – roșu
    @FXML
    private Circle green1; // Semafor pietonal – verde
    @FXML
    private Circle red2; // Al doilea semafor pietonal – roșu
    @FXML
    private Circle green2; // Al doilea semafor pietonal – verde

    private Timer movementTimer;
    private Timer trafficLightTimer;
    private String currentLight = "GREEN"; // Stare curentă pentru semaforul vehiculelor

    // Flag pentru a porni animația pietonilor o singură dată pe ciclul verde pentru pietoni
    private boolean pedestriansAnimated = false;

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
                        switchToRed(); // Lumina roșie timp de 5 secunde (pietonii au verde)
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
        redLight.setFill(Paint.valueOf("#842c2c"));   // Roșu normal

        // Pentru semaforul pietonal, se afișează roșu (pietonii nu pot traversa)
        red1.setFill(Paint.valueOf("#FF3333"));
        green1.setFill(Paint.valueOf("#347620"));
        red2.setFill(Paint.valueOf("#FF3333"));
        green2.setFill(Paint.valueOf("#347620"));

        // Resetează flag-ul pentru animația pietonilor
        pedestriansAnimated = false;
    }

    private void switchToYellow() {
        currentLight = "YELLOW";

        // Actualizează culorile semaforului pentru vehicule
        greenLight.setFill(Paint.valueOf("#347620")); // Verde normal
        yellowLight.setFill(Paint.valueOf("#FFFF00")); // Galben aprins
        redLight.setFill(Paint.valueOf("#842c2c"));     // Roșu normal

        // Semaforul pietonal rămâne roșu
        red1.setFill(Paint.valueOf("#FF3333"));
        green1.setFill(Paint.valueOf("#347620"));
        red2.setFill(Paint.valueOf("#FF3333"));
        green2.setFill(Paint.valueOf("#347620"));

        // Resetează flag-ul pentru animația pietonilor
        pedestriansAnimated = false;
    }

    private void switchToRed() {
        currentLight = "RED";

        // Actualizează culorile semaforului pentru vehicule
        greenLight.setFill(Paint.valueOf("#347620")); // Verde normal
        yellowLight.setFill(Paint.valueOf("#6f731f")); // Galben normal
        redLight.setFill(Paint.valueOf("#FF3333"));    // Roșu aprins

        // Actualizează culorile semaforului pentru pietoni (devin verde, deci se pot traversa)
        red1.setFill(Paint.valueOf("#6f731f")); // Roșu întunecat (neactiv)
        green1.setFill(Paint.valueOf("#22FF40")); // Verde aprins (activ pentru traversare)
        red2.setFill(Paint.valueOf("#6f731f"));
        green2.setFill(Paint.valueOf("#22FF40"));

        // Pornim animația pietonilor o singură dată pe ciclul în care semaforul pietonal e verde
        if (!pedestriansAnimated) {
            animatePedestrians();
            pedestriansAnimated = true;
        }
    }

    // Metoda care animează pietonii (om1 și om2)
    private void animatePedestrians() {
        // Stabilim distanța de traversare (ajustează după necesități)
        double targetDistance = 650;

        // Pentru om1:
        double newXOm1 = (om1.getTranslateX() == 0 ? targetDistance : 0);
        // Tranziția de deplasare orizontală
        TranslateTransition transitionOm1 = new TranslateTransition(Duration.seconds(3), om1);
        transitionOm1.setToX(newXOm1);

        // Tranziția de rotație în jurul axei Y (flip orizontal)
        RotateTransition rotateOm1 = new RotateTransition(Duration.seconds(0.5), om1);
        rotateOm1.setAxis(Rotate.Y_AXIS);
        // Dacă se deplasează spre dreapta (din stânga spre dreapta), orientarea va fi 0°;
        // dacă se deplasează spre stânga (de la dreapta spre stânga), setăm 180°.
        if (newXOm1 > om1.getTranslateX()) {
            rotateOm1.setToAngle(0);
        } else {
            rotateOm1.setToAngle(180);
        }

        // Se rulează în paralel deplasarea și rotația
        ParallelTransition parallelOm1 = new ParallelTransition(om1, transitionOm1, rotateOm1);
        parallelOm1.play();

        // Pentru om2:
        double newXOm2 = (om2.getTranslateX() == 0 ? targetDistance : 0);
        TranslateTransition transitionOm2 = new TranslateTransition(Duration.seconds(3), om2);
        transitionOm2.setToX(newXOm2);

        RotateTransition rotateOm2 = new RotateTransition(Duration.seconds(0.5), om2);
        rotateOm2.setAxis(Rotate.Y_AXIS);
        if (newXOm2 > om2.getTranslateX()) {
            rotateOm2.setToAngle(0);
        } else {
            rotateOm2.setToAngle(180);
        }

        ParallelTransition parallelOm2 = new ParallelTransition(om2, transitionOm2, rotateOm2);
        parallelOm2.play();
    }

}
