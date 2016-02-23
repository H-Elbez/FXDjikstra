package com.gluonapplication;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GluonApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
		AnchorPane root =  (AnchorPane) FXMLLoader.load(GluonApplication.class.getResource("App.fxml"));
       Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getMaxX(),Screen.getPrimary().getVisualBounds().getMaxY());
	//	Scene scene = new Scene(root,null);
		stage.getIcons().add(new Image(GluonApplication.class.getResourceAsStream("icon.png")));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(getClass().getResource("theme.css").toExternalForm());
		stage.setTitle("Djikstra Implementation");
        stage.setResizable(false);
        stage.show();
        
    }

  
}
