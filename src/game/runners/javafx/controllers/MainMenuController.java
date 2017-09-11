package game.runners.javafx.controllers;

import game.engine.GameManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by amirshavit on 9/11/17.
 */
public class MainMenuController {

    static private final String XML_FILTER_DESC = "XML Document (*.xml)";
    static private final String XML_FILTER_EXT = "*.xml";

    static private FileChooser fileChooser;

    private Stage window;
    private File selectedXml = null;

    private GameManager game = null;

    public MainMenuController(Stage window) {
        this.window = window;
    }

    public void handleLoadXmlButtonPressed() {
        System.out.println("here");

    }

    public void handleStartGameButtonPressed() {

    }

    public void handleResignGamePressed() {

    }

    public void handleQuitGamePressed() {

    }


    private static FileChooser getFileChooser() {
        if (fileChooser == null) {
            synchronized (MainMenuController.class) {
                if (fileChooser == null) {
                    fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Resource File");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(XML_FILTER_DESC, XML_FILTER_EXT);
                    fileChooser.getExtensionFilters().add(extFilter);
                }
            }
        }
        return fileChooser;
    }

//    @FXML private void handleLoadXmlButtonPressed(MouseEvent event) {
//        System.out.println("Pressed Load XML!");
//        if (isGameInProgress()) {
//            System.out.println("Game already in progress, you can't load another.");
//            return;
//        }
//
//        Stage stage = (Stage) anchorPane.getScene().getWindow();
//
//        File xml = fileChooser.showOpenDialog(stage);
//        if (xml != null) {
//            System.out.println("selected " + xml.toString());
//            loadGame(xml);
//        }
//    }
//
//    @FXML private void handleStartGameButtonPressed(MouseEvent event) {
//        System.out.println("Pressed Start!");
//        if (isGameInProgress()) {
//            System.out.println("Game already started, you can't start it again.");
//            return;
//        }
//        if (this.game == null) {
//            System.out.println("You should load XML settings file first.");
//            btStartGame.setDisable(true);
//            return;
//        }
//
//        this.game.start();
//    }
//
//    @FXML private void handleQuitGameButtonPressed(MouseEvent event) {
//        closeProgram();
//    }
//
//    private void loadGame(File xml) {
//        enableAllButton(false);
//
//        try {
//            BattleShipGame gameDescriptor = JAXBGameParser.loadGameFromFile(xml);
//            this.game = new GameManager(gameDescriptor);
//            this.selectedXml = xml;
//            System.out.println("XML File loaded successfully.");
//            enableAllButton(true);
//
//        } catch (FileNotFoundException e) {
//            System.out.println("Please verify that the file is exists.");
//
//        } catch (FileNotXmlFormat fileNotXmlFormat) {
//            System.out.println("File must be a valid XML format.");
//
//        } catch (JAXBException e) {
//            System.out.println("XML file is not valid, please make sure your xml file meet the xsd file.");
//        } catch (NotEnoughShipsLocated | BoardSizeIsTooBig | ShipsLocatedTooClose e) {
//            System.out.println("ERROR PARSING XML FILE: " + e.getMessage());
//        } finally {
//            System.out.println();
//        }
//
//    }
//
//    private void enableAllButton(boolean isEnabled) {
//        System.out.println("All button " + (isEnabled ? "enabled" : "disabled"));
//        btLoadXml.setDisable(!isEnabled);
//        btQuit.setDisable(!isEnabled);
//        btStartGame.setDisable(!isEnabled || game == null);
//    }
//
//    private boolean isGameInProgress() {
//        return (this.game != null && this.game.isRunning());
//    }
//
//    /// TODO: Amir: Decide where to hold this exit + handler for stage.setOnCloseRequest
//    protected void closeProgram() {
//        System.out.println("close program called");
//        if (this.game != null && this.game.isRunning())
//            this.game.finishGame();
//
//        Stage stage = (Stage) anchorPane.getScene().getWindow();
//        stage.close();
//    }
}
