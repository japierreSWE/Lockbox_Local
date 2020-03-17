package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    public CreateBoxView(VBox parent, Model model, Stage enclosingStage) {

        super(parent, 450, 400);
        this.model = model;
        this.enclosingStage = enclosingStage;

        //All UI managed below.
        nameErrorMsg.setManaged(false);
        relockErrorMsg.setManaged(false);

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

        parent.getChildren().add(confirmContainer);
        confirmContainer.getChildren().add(confirmButton);
        confirmContainer.setAlignment(Pos.CENTER);

    }

}
