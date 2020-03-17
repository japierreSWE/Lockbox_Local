package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CreateBoxView extends Scene {

    Model model;
    Stage enclosingStage;

    Label nameLabel = new Label("Name");
    TextField nameField = new TextField();
    Label nameErrorMsg = new Label("A box with this name already exists.");
    Label contentsLabel = new Label("Contents");
    TextArea contentsArea = new TextArea();

    HBox unlockDelayContainer = new HBox(8);
    Label unlockDelayLabel = new Label("Unlock delay:");
    TextField unlockNumField = new TextField();
    ChoiceBox<String> unlockDurationChoice = new ChoiceBox<String>();

    HBox relockDelayContainer = new HBox(8);
    Label relockDelayLabel = new Label("Relock delay:");
    TextField relockNumField = new TextField();
    ChoiceBox<String> relockDurationChoice = new ChoiceBox<String>();

    Label relockErrorMsg = new Label("A lockbox can't have a relock delay greater than 7 days.");

    HBox confirmContainer = new HBox();
    Button confirmButton = new Button("Confirm");

    /**
     * Attempts to create a lockbox, displays
     * an error if necessary.
     */
    private void createLockBox() {

        String name = nameField.getText();
        String contents = contentsArea.getText();
        String unlockUnitsStr = unlockNumField.getText();
        String relockUnitsStr = relockNumField.getText();

        int unlockUnits;
        int relockUnits;

        try {

            unlockUnits = Integer.parseInt(unlockUnitsStr);
            relockUnits = Integer.parseInt(relockUnitsStr);

        } catch(Exception e) {
            relockErrorMsg.setText("Please input valid numbers for the unlock and relock delays.");
            relockErrorMsg.setManaged(true);
            return;
        }

        if(relockUnits > 7 && relockDurationChoice.getValue().equals("days")) {
            relockErrorMsg.setText("A lockbox can't have a relock delay greater than 7 days.");
            relockErrorMsg.setManaged(true);
            return;
        }

        //start off w/ seconds
        int relockMultiplier = 1000;
        int unlockMultiplier = 1000;

        switch(unlockDurationChoice.getValue()) {

            case "seconds":
                break;

            case "minutes":
                unlockMultiplier *= 60;
                break;

            case "hours":
                unlockMultiplier *= 60 * 60;
                break;

            case "days":
                unlockMultiplier *= 60 * 60 * 24;
                break;

        }

        switch(relockDurationChoice.getValue()) {

            case "seconds":
                break;

            case "minutes":
                relockMultiplier *= 60;
                break;

            case "hours":
                relockMultiplier *= 60 * 60;
                break;

            case "days":
                relockMultiplier *= 60 * 60 * 24;
                break;

        }

        int relockDelayMillis = relockUnits * relockMultiplier;
        int unlockDelayMillis = unlockUnits * unlockMultiplier;

        if(model.boxExists(name)) {

            relockErrorMsg.setText("A box with this name already exists.");
            relockErrorMsg.setManaged(true);
            return;

        }

        model.createBox(name, contents, unlockDelayMillis, relockDelayMillis);
        enclosingStage.fireEvent(new WindowEvent(enclosingStage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }

    public CreateBoxView(VBox parent, Model model, Stage enclosingStage) {

        super(parent, 450, 400);
        this.model = model;
        this.enclosingStage = enclosingStage;

        //All UI managed below.
        nameErrorMsg.setManaged(false);
        relockErrorMsg.setManaged(false);

        nameErrorMsg.setFont(new Font(10));
        nameErrorMsg.setTextFill(Color.RED);
        relockErrorMsg.setFont(new Font(10));
        relockErrorMsg.setTextFill(Color.RED);

        parent.setPadding(new Insets(20, 20, 20, 20));

        contentsArea.setMaxHeight(100);
        unlockDelayContainer.setPadding(new Insets(30,0,0,0));
        relockDelayContainer.setPadding(new Insets(0,0,30,0));

        relockDurationChoice.setValue("seconds");
        unlockDurationChoice.setValue("seconds");

        parent.getChildren().add(nameLabel);
        parent.getChildren().add(nameErrorMsg);
        parent.getChildren().add(nameField);

        parent.getChildren().add(contentsLabel);
        parent.getChildren().add(contentsArea);

        parent.getChildren().add(unlockDelayContainer);
        unlockDelayContainer.getChildren().add(unlockDelayLabel);
        unlockDelayContainer.getChildren().add(unlockNumField);
        unlockDelayContainer.getChildren().add(unlockDurationChoice);
        unlockDurationChoice.getItems().addAll("seconds", "minutes", "hours", "days");

        parent.getChildren().add(relockDelayContainer);
        relockDelayContainer.getChildren().addAll(relockDelayLabel, relockNumField, relockDurationChoice);
        relockDurationChoice.getItems().addAll("seconds", "minutes", "hours", "days");

        parent.getChildren().add(relockErrorMsg);

        //we do this so that the confirm button is in the center.
        parent.getChildren().add(confirmContainer);
        confirmContainer.getChildren().add(confirmButton);
        confirmContainer.setAlignment(Pos.CENTER);

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                createLockBox();

            }
        });

    }

}
