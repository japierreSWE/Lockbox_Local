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

    //Whether the box was locked last time we tried to
    //update the UI.
    Integer lastStatus;

    //whether we've already received a request to unlock the box.
    boolean currentlyUnlocking = false;

    /**
     * From a duration of time, returns it in the format
     * "x days, x hours, x minutes and x seconds"
     * @param duration
     */
    private String getTimeString(long duration) {

        final long secLength = 1000;
        final long minLength = secLength * 60;
        final long hourLength = minLength * 60;
        final long dayLength = hourLength * 24;

        String timeString = "";
        int count = 0;

        long numDays = duration / dayLength;

        if(numDays != 0) {
            duration -= dayLength * numDays;
            timeString += String.valueOf(numDays);

            if(numDays == 1) {
                timeString += " day, ";
            } else {
                timeString += " days, ";
            }
            ++count;
        }

        long numHours = duration / hourLength;

        if(numHours != 0) {
            duration -= hourLength * numHours;
            timeString += String.valueOf(numHours);

            if(numHours == 1) {
                timeString += " hour, ";
            } else {
                timeString += " hours, ";
            }

            ++count;
        }

        long numMins = duration / minLength;

        if(numMins != 0) {
            duration -= minLength * numMins;
            timeString += String.valueOf(numMins);

            if(numMins == 1) {
                timeString += " minute, ";
            } else {
                timeString += " minutes, ";
            }

            ++count;
        }

        long numSeconds = duration / secLength;


        timeString += String.valueOf(numSeconds);

        if(numSeconds == 1) {
            timeString += " second remaining";
        } else {
            timeString += " seconds remaining";
        }

        return timeString;

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
                String timeString = getTimeString(diff);

                timeToLockChange.setManaged(true);
                timeToLockChange.setText(timeString + " to unlock.");

            } else if (currentTimeMillis >= currentBox.getUnlockTimestamp()
                    && currentTimeMillis < currentBox.getRelockTimestamp()) {

                currentBox.setLocked(0);
                model.updateBox(currentBox);

                long diff = currentBox.getRelockTimestamp() - currentTimeMillis;
                String timeString = getTimeString(diff);

                timeToLockChange.setManaged(true);
                timeToLockChange.setText(timeString + " to relock.");

            } else if(currentTimeMillis > currentBox.getRelockTimestamp()) {

                lockBox();
                model.updateBox(currentBox);

                timeToLockChange.setManaged(false);
                timeToLockChange.setText("");
                currentlyUnlocking = false;
            }

        }

        boolean statusChanged = lastStatus == null || lastStatus != currentBox.getLocked();

        if(currentBox.getLocked() == 1 && statusChanged) {
            lockStatus.setText("Locked");
            lockStatus.setTextFill(Color.RED);
            contentsArea.setText("-- locked --");
            relockButton.setDisable(true);
            unlockButton.setDisable(false);
        } else if(currentBox.getLocked() == 0 && statusChanged) {
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

        if(currentBox.getUnlockTimestamp() != null) {
            currentlyUnlocking = true;
        }

        unlockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!currentlyUnlocking) {

                    currentBox.setUnlockTimestamp(System.currentTimeMillis() + currentBox.getUnlockDelay());
                    currentBox.setRelockTimestamp(currentBox.getUnlockTimestamp() + currentBox.getRelockDelay());
                    model.updateBox(currentBox);
                    updateUI();
                    currentlyUnlocking = true;

                }

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
                currentlyUnlocking = false;

            }
        });

        updateUI();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                updateUI();
                lastStatus = currentBox.getLocked();

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

}
