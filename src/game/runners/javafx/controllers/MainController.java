package game.runners.javafx.controllers;

import game.runners.javafx.models.MainModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Text actiontarget;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private MainModel mainModel;

    public void setModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        GridPane
    }

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        String result = mainModel.getLoginResult(username, password);
//        String result = "User Logged In, Bitch!";
        actiontarget.setText(result);
    }
}