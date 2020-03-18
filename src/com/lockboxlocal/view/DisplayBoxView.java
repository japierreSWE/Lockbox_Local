package com.lockboxlocal.view;

import com.lockboxlocal.entity.Lockbox;
import com.lockboxlocal.entity.Model;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class DisplayBoxView extends Scene {

    Model model;
    Lockbox currentBox;

    Label title = new Label();
    Label lockStatus = new Label("Locked");
    Label timeToLockChange = new Label();

    HBox buttonContainer = new HBox(8);
    Button unlockButton = new Button("Unlock");
    Button relockButton = new Button("Relock");

    Label contentsLabel = new Label("Contents");
    TextArea contentsArea = new TextArea();

    /**
     * Given a duration of time in milliseconds,
     * returns the largest unit of time
     * it can be converted to and the number of those
     * units.
     */
    private Pair<String, String> getTimeUnitsAndAmount(long duration) {

        String numUnits = null;
        String timeUnits = null;

        final long secLength = 1000;
        final long minLength = secLength * 60;
        final long hourLength = minLength * 60;
        final long dayLength = hourLength * 24;

        if(duration >= dayLength) {

            numUnits = String.valueOf(duration / dayLength);

            if(duration / dayLength == 1) {
                timeUnits = "day";
            } else {
                timeUnits = "days";
            }

        } else if(duration >= hourLength) {

            numUnits = String.valueOf(duration / hourLength);

            if(duration / hourLength == 1) {
                timeUnits = "hour";
            } else {
                timeUnits = "hours";
            }

        } else if(duration >= minLength) {

            numUnits = String.valueOf(duration / minLength);

            if(duration / minLength == 1) {
                timeUnits = "minute";
            } else {
                timeUnits = "minutes";
            }

        } else {

            numUnits = String.valueOf(duration / secLength);

            if(duration / secLength == 1) {
                timeUnits = "second";
            } else {
                timeUnits = "seconds";
            }

        }

        return new Pair<String, String>(timeUnits, numUnits);

    }

    /**
     * Determines how the UI should look based on the current state
     * of the box.
     */
    private void updateUI() {

        if(currentBox.getUnlockTimestamp() != null) {

            long currentTimeMillis = System.currentTimeMillis();

            if(currentTimeMillis < currentBox.getUnlockTimestamp()) {

                long diff = currentBox.getUnlockTimestamp() - currentTimeMillis;
                Pair<String, String> pair = getTimeUnitsAndAmount(diff);

                String numUnits = pair.getValue();
                String timeUnits = pair.getKey();

                timeToLockChange.setManaged(true);
                timeToLockChange.setText(numUnits + " " + timeUnits + " remaining to unlock.");

            } else if (currentTimeMillis >= currentBox.getUnlockTimestamp()
                    && currentTimeMillis < currentBox.getRelockTimestamp()) {

                currentBox.setLocked(0);
                model.updateBox(currentBox);

                long diff = currentBox.getRelockTimestamp() - currentTimeMillis;
                Pair<String, String> pair = getTimeUnitsAndAmount(diff);

                String numUnits = pair.getValue();
                String timeUnits = pair.getKey();

                timeToLockChange.setManaged(true);
                timeToLockChange.setText(numUnits + " " + timeUnits + " remaining to relock.");

            } else if(currentTimeMillis > currentBox.getRelockTimestamp()) {

                currentBox.setLocked(1);
                model.updateBox(currentBox);

                timeToLockChange.setManaged(false);
                timeToLockChange.setText("");
            }

        }

        if(currentBox.getLocked() == 1) {
            lockStatus.setText("Locked");
            lockStatus.setTextFill(Color.RED);
            contentsArea.setText("-- locked --");
            relockButton.setDisable(true);
            unlockButton.setDisable(false);
        } else {
            lockStatus.setText("Unlocked");
            lockStatus.setTextFill(Color.GREEN);
            contentsArea.setText(currentBox.getContents());
            relockButton.setDisable(false);
            unlockButton.setDisable(true);
        }
    }

    public void lockBox() {
        currentBox.setLocked(1);
        currentBox.setUnlockTimestamp(null);
        currentBox.setRelockTimestamp(0);
    }

    public DisplayBoxView(VBox root, Model model, String boxName) {

        super(root, 400, 400);
        this.model = model;

        timeToLockChange.setManaged(false);

        root.setPadding(new Insets(20, 20, 20, 20));

        title.setText(boxName);
        title.setFont(new Font(24));

        lockStatus.setPadding(new Insets(0,0,20, 0));
        buttonContainer.setPadding(new Insets(0,0,20, 0));

        root.getChildren().add(title);
        root.getChildren().add(lockStatus);
        root.getChildren().add(timeToLockChange);

        root.getChildren().add(buttonContainer);
        buttonContainer.getChildren().addAll(unlockButton, relockButton);

        root.getChildren().add(contentsLabel);
        root.getChildren().add(contentsArea);

        currentBox = model.getBox(boxName);

        unlockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                currentBox.setUnlockTimestamp(System.currentTimeMillis() + currentBox.getUnlockDelay());
                currentBox.setRelockTimestamp(currentBox.getUnlockTimestamp() + currentBox.getRelockDelay());
                model.updateBox(currentBox);
                updateUI();

            }
        });

        relockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                lockBox();
                model.updateBox(currentBox);
                timeToLockChange.setManaged(false);
                timeToLockChange.setText("");
                updateUI();

            }
        });

        updateUI();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                updateUI();

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

}
