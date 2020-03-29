package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View {

    Model model;
    HomeView homeView;
    VBox hvRoot;

    public View(Model model, Stage stage) {
        this.model = model;
        hvRoot = new VBox(9);
        homeView = new HomeView(hvRoot, model, stage);
    }

    public HomeView getHomeView() {
        return homeView;
    }

}
