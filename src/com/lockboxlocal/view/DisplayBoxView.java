package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DisplayBoxView extends Scene {

    Model model;

    Label title = new Label();
    Label lockStatus = new Label("Locked");
    Label timeToLockChange = new Label();

    HBox buttonContainer = new HBox(8);
    Button unlockButton = new Button("Unlock");
    Button relockButton = new Button("Relock");

    Label contentsLabel = new Label("Contents");
    TextArea contentsArea = new TextArea();

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

    }

}
