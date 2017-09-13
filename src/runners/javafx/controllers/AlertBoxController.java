package runners.javafx.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by amirshavit on 9/11/17.
 */
public class AlertBoxController implements Initializable {

    Stage window;

    @FXML
    private Label message;

    @FXML
    private Button close;

    @FXML
    private void handleCloseButtonPressed(ActionEvent event) {
        window.close();
    }

    public AlertBoxController() {

    }

    public static void displayAlert(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = AlertBoxController.class.getResource("/runners/javafx/fxml/AlertBox.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());

            AlertBoxController alertController = fxmlLoader.getController();
            alertController.message.setText(message);
            alertController.window = window;

            Scene scene = new Scene(root);
            window.setScene(scene);
            window.showAndWait();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
