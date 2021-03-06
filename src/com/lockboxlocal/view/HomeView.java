package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeView extends Scene {

    Button createButton = new Button("New");
    Button deleteButton = new Button("Delete");
    Button openButton = new Button("Open");
    Button exportButton = new Button("Export Boxes");
    Button importButton = new Button("Import Boxes");

    Label listLabel = new Label("Lockboxes:");
    ListView<String> boxList = new ListView<String>();

    Label errorMsg = new Label("hi");

    HBox buttonContainer = new HBox(7);

    Stage enclosingStage;

    Model model;

    /**
     * Retrieves all boxes from the SQLite database and
     * replaces the listView's contents with them.
     */
    private void retrieveBoxes() {

        boxList.getItems().clear();
        ArrayList<String> boxes = model.getBoxes();
        boxList.getItems().addAll(boxes);
        boxList.refresh();

    }

    public HomeView(VBox parent, Model model, Stage stage) {
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

        enclosingStage = stage;

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                VBox createRoot = new VBox(5);
                Stage secondaryStage = new Stage();
                CreateBoxView cbv = new CreateBoxView(createRoot, model, secondaryStage);

                secondaryStage.setScene(cbv);
                secondaryStage.setTitle("Create a New Lockbox");

                //we want to refresh the box list whenever we're done creating a new lockbox
                secondaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {

                        retrieveBoxes();

                    }
                });

                secondaryStage.show();

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

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirm Deletion");
                    confirm.setHeaderText("Confirm Deletion");
                    confirm.setContentText("Are you sure you want to delete this lockbox? You won't be able to recover it after deleting it.");
                    Optional<ButtonType> result = confirm.showAndWait();

                    if(result.get() == ButtonType.OK) {
                        model.deleteBox(selection);
                        retrieveBoxes();
                    }
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
                    VBox openRoot = new VBox(5);
                    Stage secondaryStage = new Stage();
                    DisplayBoxView dbv = new DisplayBoxView(openRoot, model, selection);

                    secondaryStage.setScene(dbv);
                    secondaryStage.setTitle("Lockbox Local");
                    secondaryStage.show();
                }

            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                VBox exportRoot = new VBox(7);
                Stage secondaryStage = new Stage();
                ExportView ev = new ExportView(exportRoot, model, secondaryStage);

                secondaryStage.setScene(ev);
                secondaryStage.setTitle("Export Boxes");
                secondaryStage.show();

            }
        });

        importButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Lockbox Files (.lbf)", "*.lbf"));
                fileChooser.setTitle("Select a file to import.");

                File selectedFile = fileChooser.showOpenDialog(enclosingStage);

                if(selectedFile != null) {

                    Alert importAlert = new Alert(Alert.AlertType.WARNING);
                    importAlert.setTitle("Import Alert");
                    importAlert.setHeaderText(null);
                    importAlert.setContentText("No two lockboxes can have the same name. Any lockbox with a name identical to one you've already made will not be imported. Press \"OK\" to continue.");

                    Optional<ButtonType> result =  importAlert.showAndWait();

                    if(result.get() == ButtonType.OK) {

                        Pair<Long, Long> importResult = model.importBoxes(selectedFile);

                        if(importResult.getKey() != -1) {

                            Alert importConfirm = new Alert(Alert.AlertType.INFORMATION);
                            importConfirm.setTitle("Import Complete");
                            importConfirm.setHeaderText(null);
                            importConfirm.setContentText(String.valueOf(importResult.getKey()) +
                                    " boxes found in import file. " + String.valueOf(importResult.getValue()) +
                                    " were successfully imported.");

                            importConfirm.showAndWait();
                            retrieveBoxes();

                        } else {

                            Alert importConfirm = new Alert(Alert.AlertType.ERROR);
                            importConfirm.setTitle("Error");
                            importConfirm.setHeaderText(null);
                            importConfirm.setContentText("Something went wrong while importing.");

                            importConfirm.showAndWait();
                            retrieveBoxes();

                        }

                    }

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
