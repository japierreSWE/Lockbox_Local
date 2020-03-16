package com.lockboxlocal.view;

import com.lockboxlocal.entity.Model;
import javafx.scene.layout.VBox;

public class View {

    Model model;
    HomeView homeView;
    VBox hvRoot;

    public View(Model model) {
        this.model = model;
        hvRoot = new VBox(7);
        homeView = new HomeView(hvRoot, model);
    }

    public HomeView getHomeView() {
        return homeView;
    }

}
