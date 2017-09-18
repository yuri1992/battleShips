package runners.javafx.controllers;

import game.engine.GameManager;
import game.engine.GameManagerFactory;
import game.engine.GameState;
import game.exceptions.GameSettingsInitializationException;
import game.exceptions.InvalidFileFormatException;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        this.window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                closeProgram();
            }
        });
    }

    public GameManager handleLoadXmlButtonPressed() {
        if (isGameInProgress()) {
            AlertBoxController.displayAlert("Illegal Action", "Game already in progress, you can't load another.");
            return null;
        }

        if (window != null) {
            File xml = getFileChooser().showOpenDialog(window);
            if (xml != null) {
                loadGame(xml);
            }
        }
        return game;
    }

    public boolean handleStartGameButtonPressed() {
        if (isGameInProgress()) {
            AlertBoxController.displayAlert("Illegal Action", "Game already started, you can't start it again.");
            return false;
        }
        if (this.game == null) {
            AlertBoxController.displayAlert("Illegal Action", "You should load XML settings file first.");
            return false;
        }

        this.game.startGame();
        return true;
    }

    public boolean handleResignGamePressed() {
        if (!isGameInProgress()) {
            AlertBoxController.displayAlert("Illegal Action", "Player can't resign if a game is not in progress...");
            return false;
        }

        boolean res = false;
        res = ConfirmBoxController.displayAlert("Are you sure?", "Are you sure you want to\n" +
                        "resign from the game?");
        if (res) {
            this.game.resignGame();
        }
        return res;
    }

    public void handleQuitGamePressed() {
        closeProgram();
    }

    public void handleAboutButtonPressed() {
        AlertBoxController.displayAlert("About this game", "This game was developed for\n" +
                "MTA's Java class by:\n" +
                "Yuri Ritvin and Amir Shavit");
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
            this.game = GameManagerFactory.loadGameManager(xml);
            this.selectedXml = xml;
            AlertBoxController.displayAlert("Game loaded", "Configuration file loaded successfully.");

        } catch (FileNotFoundException e) {
            AlertBoxController.displayAlert("Could not load file", "Please verify that the file is exists.");

        } catch (InvalidFileFormatException fileNotXmlFormat) {
            AlertBoxController.displayAlert("Could not load file", "File must be a valid XML format.");

        } catch (JAXBException | GameSettingsInitializationException e) {
            AlertBoxController.displayAlert("Could not load file", "XML file is not valid: " + e.toString());
        }
    }

    private boolean isGameInProgress() {
        return (game != null && game.getState() == GameState.IN_PROGRESS);
    }

    private void closeProgram() {
        boolean res = ConfirmBoxController.displayAlert("Are you sure?", "Are you sure you want to exit the " +
                "program?");
        if (res) {
            if (this.game != null)
                this.game.finishGame();

            window.close();
        }
    }

}
