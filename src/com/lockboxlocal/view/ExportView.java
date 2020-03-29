package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class ExportView extends Scene {

    Model model;
    Stage enclosingStage;

    Label fieldLabel = new Label("Name of export file:");
    Label filenameLabel = new Label("");
    TextField filenameField = new TextField();
    HBox buttonContainer = new HBox();
    Button exportButton = new Button("Export");

    public ExportView(VBox parent, Model model, Stage stage) {
        super(parent, 300, 150);

        this.model = model;
        this.enclosingStage = stage;

        parent.setPadding(new Insets(20, 20, 20, 20));

        parent.getChildren().addAll(fieldLabel, filenameLabel, filenameField, buttonContainer);

        filenameLabel.setManaged(false);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().add(exportButton);


        filenameField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(!filenameField.getText().equals("")) {

                    filenameLabel.setText(filenameField.getText() + ".lbf");
                    filenameLabel.setManaged(true);

                } else {

                    filenameLabel.setText("");
                    filenameLabel.setManaged(false);

                }

            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select export location");
                File directory = directoryChooser.showDialog(enclosingStage);

                if(directory != null) {

                    String outputPath = directory.toString() + "/" + filenameField.getText() + ".lbf";

                    File file = new File(outputPath);

                    if(!file.isFile()) {
                        boolean successful = model.exportDB(outputPath);

                        if(!successful) {
                            Alert exportError = new Alert(Alert.AlertType.ERROR);
                            exportError.setTitle("Error");
                            exportError.setHeaderText(null);
                            exportError.setContentText("Something went wrong while exporting.");
                            exportError.showAndWait();
                        } else {

                            Alert expSuccess = new Alert(Alert.AlertType.INFORMATION);
                            expSuccess.setTitle("Export Boxes");
                            expSuccess.setHeaderText(null);
                            expSuccess.setContentText("Export was successful.");
                            expSuccess.showAndWait();
                            enclosingStage.close();

                        }

                    } else {
                        Alert exportError = new Alert(Alert.AlertType.ERROR);
                        exportError.setTitle("File already exists");
                        exportError.setHeaderText(null);
                        exportError.setContentText("A file with that name already exists in this directory.");
                        exportError.showAndWait();
                    }

                }

            }
        });

    }

}
