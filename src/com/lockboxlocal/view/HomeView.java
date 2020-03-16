package com.lockboxlocal.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomeView extends Scene {

    Button createButton = new Button("Create");
    Button deleteButton = new Button("Delete");
    Button openButton = new Button("Open");
    Button exportButton = new Button("Export Boxes");
    Button importButton = new Button("Import Boxes");

    Label listLabel = new Label("Lockboxes:");
    ListView boxList = new ListView();

    HBox buttonContainer = new HBox(7);

    public HomeView(VBox parent) {
        super(parent, 450, 350);

        //Set margin here.
        parent.setPadding(new Insets(20, 20, 20, 20));

        //Managing buttons area.
        parent.getChildren().add(buttonContainer);
        buttonContainer.getChildren().add(createButton);
        buttonContainer.getChildren().add(deleteButton);
        buttonContainer.getChildren().add(openButton);
        buttonContainer.getChildren().add(exportButton);
        buttonContainer.getChildren().add(importButton);
        buttonContainer.setPadding(new Insets(0,0,50,0));

        //Managing list area.
        parent.getChildren().add(listLabel);
        parent.getChildren().add(boxList);

    }
}
