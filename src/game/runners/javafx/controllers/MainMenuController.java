package game.runners.javafx.controllers;

import descriptor.BattleShipGame;
import game.engine.GameManager;
import game.engine.JAXBGameParser;
import game.exceptions.GameSettingsInitializationException;
import game.exceptions.InvalidFileFormatException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by amirshavit on 9/11/17.
 */
public class MainMenuController {

    static private final String XML_FILTER_DESC = "XML Document (*.xml)";
    static private final String XML_FILTER_EXT = "*.xml";

    static private FileChooser fileChooser;

    private Stage window;
    private File selectedXml = null;

    protected GameManager game = null;

    public MainMenuController(Stage window) {
        this.window = window;
    }

    public GameManager handleLoadXmlButtonPressed() {
        if (isGameInProgress()) {
            AlertBoxController.displayAlert("Illegal Action", "Game already in progress, you can't load another.");
            return game;
        }

        if (window != null) {
            File xml = getFileChooser().showOpenDialog(window);
            if (xml != null) {
                System.out.println("selected " + xml.toString());
                loadGame(xml);
            }
        }
        return game;
    }

    public void handleStartGameButtonPressed() {
        if (isGameInProgress()) {
            AlertBoxController.displayAlert("Illegal Action", "Game already started, you can't start it again.");
            return;
        }
        if (this.game == null) {
            AlertBoxController.displayAlert("Illegal Action", "You should load XML settings file first.");
            return;
        }

        this.game.start();
    }

    public void handleResignGamePressed() {
        System.out.println("handleResignGamePressed");

    }

    public void handleQuitGamePressed() {
        System.out.println("handleQuitGamePressed");
//        closeProgram();
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


    private void loadGame(File xml) {
        try {
            BattleShipGame gameDescriptor = JAXBGameParser.loadGameFromFile(xml);
            this.game = new GameManager(gameDescriptor);
            this.selectedXml = xml;
            System.out.println("XML File loaded successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("Please verify that the file is exists.");

        } catch (InvalidFileFormatException fileNotXmlFormat) {
            System.out.println("File must be a valid XML format.");

        } catch (JAXBException e) {
            System.out.println("XML file is not valid, please make sure your xml file meet the xsd file.");
        } catch (GameSettingsInitializationException e) {
            System.out.println("ERROR PARSING XML FILE: " + e.getMessage());
        }

    }

    private boolean isGameInProgress() {
        return (this.game != null && this.game.isRunning());
    }

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
