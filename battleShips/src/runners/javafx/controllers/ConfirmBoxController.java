package runners.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Created by amirshavit on 9/12/17.
 */
public class ConfirmBoxController {

    private Stage window;
    private boolean answer;

    @FXML
    private Label message;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void handleCloseButtonPressed(ActionEvent event) {

        answer = (event.getSource() == okButton);
        window.close();
    }

    public ConfirmBoxController() {

    }

    public static boolean displayAlert(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = ConfirmBoxController.class.getResource("/runners/javafx/fxml/ConfirmBox.fxml");
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());

            ConfirmBoxController confirmController = fxmlLoader.getController();
            confirmController .message.setText(message);
            confirmController .window = window;

            Scene scene = new Scene(root);
            window.setScene(scene);
            window.showAndWait();

            return confirmController.answer;

        } catch (IOException e) {
            System.out.println(e);
        }

        return false;
    }

}
