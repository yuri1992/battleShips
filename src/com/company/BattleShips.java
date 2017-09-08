package com.company;

import game.runners.javafx.controllers.MainController;
import game.runners.javafx.models.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BattleShips extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Welcome FXML");

        //if you just want to load the FXML
//        Parent root = FXMLLoader.load(WelcomeFXML.class.getResource("welcome.fxml"));

        //if you want to load the FXML and get access to its controller
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("BattleShips.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        MainController mainController = fxmlLoader.getController();
        MainModel model = new MainModel();
        mainController.setModel(model);

        Scene scene = new Scene(root, 300, 275);

        /*Button singInButton = (Button) scene.lookup("#signInButton");
        final Text actionTarget = (Text)scene.lookup("#actiontarget");
        singInButton.setOnAction(e -> {
            actionTarget.setText("Sign in button pressed!");
        });*/

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public FXMLLoader loadBattleShipFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("BattleShips.fxml");
        fxmlLoader.setLocation(url);
        return fxmlLoader;

    }
}
