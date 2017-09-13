package runners.javafx.controllers;

import runners.javafx.models.BaseModel;
import runners.javafx.models.MainModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    BaseModel model;

    public void setModel(MainModel model) {
        this.model = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        GridPane
    }
}
