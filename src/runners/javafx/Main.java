package runners.javafx;

import runners.javafx.controllers.PlayerScreenController;
import runners.javafx.models.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

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
        URL url = getClass().getResource("/runners/javafx/fxml/PlayerScreen.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        PlayerScreenController playerScreenController = fxmlLoader.getController();
        playerScreenController.init(primaryStage);
        MainModel model = new MainModel();
        playerScreenController.setModel(model);

        Scene scene = new Scene(root, 900, 800);

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
