package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class HomeView extends Scene {

    Button createButton = new Button("Create");
    Button deleteButton = new Button("Delete");
    Button openButton = new Button("Open");
    Button exportButton = new Button("Export Boxes");
    Button importButton = new Button("Import Boxes");

    Label listLabel = new Label("Lockboxes:");
    ListView<String> boxList = new ListView<String>();

    Label errorMsg = new Label("hi");

    HBox buttonContainer = new HBox(7);

    Model model;

    private void retrieveBoxes() {

        boxList.getItems().removeAll();
        ArrayList<String> boxes = model.getBoxes();
        boxList.getItems().addAll(boxes);

    }

    public HomeView(VBox parent, Model model) {
        super(parent, 450, 350);

        this.model = model;

        errorMsg.setManaged(false);
        errorMsg.setFont(new Font(10));
        errorMsg.setTextFill(Color.RED);

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

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String selection = boxList.getSelectionModel().getSelectedItem();

                if(selection == null) {
                    errorMsg.setText("No lockbox is selected.");
                    errorMsg.setManaged(true);
                } else {
                    errorMsg.setManaged(false);
                }

            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String selection = boxList.getSelectionModel().getSelectedItem();

                if(selection == null) {
                    errorMsg.setText("No lockbox is selected.");
                    errorMsg.setManaged(true);
                } else {
                    errorMsg.setManaged(false);
                }

            }
        });

        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String selection = boxList.getSelectionModel().getSelectedItem();

                if(selection == null) {
                    errorMsg.setText("No lockbox is selected.");
                    errorMsg.setManaged(true);
                } else {
                    errorMsg.setManaged(false);
                }

            }
        });


        //Managing list area.
        parent.getChildren().add(listLabel);
        parent.getChildren().add(errorMsg);
        parent.getChildren().add(boxList);

        //Managing list population.
        retrieveBoxes();
    }
}
