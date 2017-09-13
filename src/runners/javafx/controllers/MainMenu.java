package runners.javafx.controllers;

import descriptor.BattleShipGame;
import game.engine.GameManager;
import game.engine.JAXBGameParser;
import game.exceptions.GameSettingsInitializationException;
import game.exceptions.InvalidFileFormatException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by AmirShavit on 08/09/2017.
 */
public class MainMenu extends BaseController {

    private static final String XML_FILTER_DESC = "XML Document (*.xml)";
    private static final String XML_FILTER_EXT = "*.xml";

    @FXML private AnchorPane anchorPane;
    @FXML private Button btLoadXml;
    @FXML private Button btStartGame;
    @FXML private Button btQuit;

    private final FileChooser fileChooser = new FileChooser();
    private File selectedXml = null;

    private GameManager game = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        // Setup dependencies
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(XML_FILTER_DESC, XML_FILTER_EXT);
        fileChooser.getExtensionFilters().add(extFilter);
    }

    @FXML private void handleLoadXmlButtonPressed(MouseEvent event) {
        System.out.println("Pressed Load XML!");
        if (isGameInProgress()) {
            System.out.println("Game already in progress, you can't load another.");
            return;
        }

        Stage stage = (Stage) anchorPane.getScene().getWindow();

        File xml = fileChooser.showOpenDialog(stage);
        if (xml != null) {
            System.out.println("selected " + xml.toString());
            loadGame(xml);
        }
    }

    @FXML private void handleStartGameButtonPressed(MouseEvent event) {
        System.out.println("Pressed Start!");
        if (isGameInProgress()) {
            System.out.println("Game already started, you can't start it again.");
            return;
        }
        if (this.game == null) {
            System.out.println("You should load XML settings file first.");
            btStartGame.setDisable(true);
            return;
        }

        this.game.start();
    }

    @FXML private void handleQuitGameButtonPressed(MouseEvent event) {
        closeProgram();
    }

    private void loadGame(File xml) {
        enableAllButton(false);

        try {
            BattleShipGame gameDescriptor = JAXBGameParser.loadGameFromFile(xml);
            this.game = new GameManager(gameDescriptor);
            this.selectedXml = xml;
            System.out.println("XML File loaded successfully.");
            enableAllButton(true);

        } catch (FileNotFoundException e) {
            System.out.println("Please verify that the file is exists.");

        } catch (InvalidFileFormatException fileNotXmlFormat) {
            System.out.println("File must be a valid XML format.");

        } catch (JAXBException e) {
            System.out.println("XML file is not valid, please make sure your xml file meet the xsd file.");

        } catch (GameSettingsInitializationException e) {
            System.out.println("ERROR PARSING XML FILE: " + e.getMessage());
        } finally {
            System.out.println();
        }

    }

    private void enableAllButton(boolean isEnabled) {
        System.out.println("All button " + (isEnabled ? "enabled" : "disabled"));
        btLoadXml.setDisable(!isEnabled);
        btQuit.setDisable(!isEnabled);
        btStartGame.setDisable(!isEnabled || game == null);
    }

    private boolean isGameInProgress() {
        return (this.game != null && this.game.isRunning());
    }

    /// TODO: Amir: Decide where to hold this exit + handler for stage.setOnCloseRequest
    protected void closeProgram() {
        System.out.println("close program called");
        if (this.game != null && this.game.isRunning())
            this.game.finishGame();

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
