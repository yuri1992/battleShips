package runners.javafx.controllers;

import game.engine.GameManager;
import game.engine.GameTurn;
import game.engine.TurnType;
import game.players.PlayerStatistics;
import game.ships.ShipPoint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import runners.console.ConsoleUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerScreenController extends BaseController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    private Button start_new; // Value injected by FXMLLoader

    @FXML // fx:id="ships_board"
    private AnchorPane ships_board; // Value injected by FXMLLoader

    @FXML // fx:id="attack_board"
    private AnchorPane attack_board; // Value injected by FXMLLoader

    @FXML // fx:id="stat_turns"
    private TextField stat_turns; // Value injected by FXMLLoader

    @FXML // fx:id="stat_hits"
    private TextField stat_hits; // Value injected by FXMLLoader

    @FXML // fx:id="stat_miss"
    private TextField stat_miss; // Value injected by FXMLLoader

    @FXML // fx:id="stat_time"
    private TextField stat_time; // Value injected by FXMLLoader

    @FXML // fx:id="stat_score"
    private TextField stat_score; // Value injected by FXMLLoader

    @FXML // fx:id="place_mine"
    private Button place_mine; // Value injected by FXMLLoader

    @FXML // fx:id="list_moves"
    private ListView<String> list_moves; // Value injected by FXMLLoader

    @FXML // fx:id="player_name"
    private Label player_name; // Value injected by FXMLLoader

    @FXML
    private Label msg;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuFile_StartGame;

    @FXML
    private MenuItem menuFile_LoadXML;

    @FXML
    private MenuItem menuFile_ResignGame;

    @FXML
    private MenuItem menuFile_Quit;

    @FXML
    private Menu menuGame;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuHelp_About;

    private MainMenuController menuController;
    private Stage window;
    private GameManager game;


    @FXML
    private void handleFileMenuItemPressed(ActionEvent event) {
        if (event.getSource() == menuFile_StartGame) {
            menuController.handleStartGameButtonPressed();
            if (game != null) {
                this.render();
            }
        } else if (event.getSource() == menuFile_LoadXML) {
            setGame(menuController.handleLoadXmlButtonPressed());
        } else if (event.getSource() == menuFile_ResignGame) {
            if (menuController.handleResignGamePressed()) {
                this.game = null;
            }
        } else if (event.getSource() == menuFile_Quit) {
            menuController.handleQuitGamePressed();
        } else // TODO: Amir: throw exception??
            System.out.println("unkonw");

    }

    @FXML
    private void handleGameMenuItemPressed(ActionEvent event) {

    }

    @FXML
    private void handleHelpMenuItemPressed(ActionEvent event) {
        menuController.handleAboutButtonPressed();
    }

    @FXML
    void handleOnPlaceMine(ActionEvent event) {
        for (Node child : this.ships_board.getChildren()) {
            if (((Button) child).getText().equals("~"))
                child.setDisable(false);
        }
        place_mine.setDisable(true);
    }

    public void init(Stage window) {
        this.window = window;
        this.menuController = new MainMenuController(window);
    }

    //region Setters / Getters

    public void setGame(GameManager game) {
        this.game = game;
        enableGameRelatedMenuItems(game != null);
    }

    //endregion

    private void enableGameRelatedMenuItems(boolean isEnabled) {
        menuFile_StartGame.setDisable(!isEnabled || game == null);
        menuFile_ResignGame.setDisable(!isEnabled || game == null);
    }


    private void renderDialogSuccessTurn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Attack");
        alert.setHeaderText("Nice Shoot, You successfully attacked opposite ship");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();
    }

    private void renderDialogFailedTurn() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Unsuccessful Attack");
        alert.setHeaderText("You did not shoot a ship");
        alert.setContentText("Please try again in the next turn");
        alert.showAndWait();
    }


    private void renderDialogAlreadyAttacked() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Point Already Attacked");
        alert.setHeaderText(null);
        alert.setContentText("You already attcked this Point");
        alert.showAndWait();
    }


    private void renderShipsBoard() {
        String[][] board = game.getCurrentPlayer().getShipsBoard().printBoard();
        this.ships_board.getChildren().clear();
        for (int x = 1; x < board.length; x++) {
            for (int y = 1; y < board.length; y++) {
                Button n = new Button();
                n.minWidth(15);
                n.minHeight(15);
                n.setLayoutX(y * 35 - 35);
                n.setLayoutY(x * 35 - 35);
                n.setText(board[x][y]);
                //n.setDisable(true);

                if (board[x][y] == "~")
                    n.getStyleClass().add("empty-cell");
                else if (board[x][y] == "@")
                    n.getStyleClass().add("ship-cell");
                else
                    n.getStyleClass().add("ship-hit");

                int finalX = x;
                int finalY = y;
                n.setOnAction((event) -> {
                    if (game.placeMine(new ShipPoint(finalX, finalY))) {
                        this.renderPlaceMineSeccsffully();
                    } else {
                        this.renderPlaceMineFailed();
                    }
                    this.render();
                });

                this.ships_board.getChildren().add(n);
            }
        }
    }

    private void renderPlaceMineFailed() {
    }

    private void renderPlaceMineSeccsffully() {
    }


    public void render() {
        this.player_name.setText(game.getCurrentPlayer().toString());
        this.renderShipsBoard();
        this.renderAttackBoard();
        this.renderHistoryMoves();
        this.renderStatistics();
    }

    private void renderStatistics() {
        PlayerStatistics p = game.getCurrentPlayer().getStatistics();
        stat_score.setText(String.valueOf(p.getScore()));
        stat_score.setEditable(false);
        stat_turns.setText(String.valueOf(p.getTurns()));
        stat_turns.setEditable(false);
        stat_hits.setText(String.valueOf(p.getHits()));
        stat_hits.setEditable(false);
        stat_miss.setText(String.valueOf(p.getMisses()));
        stat_miss.setEditable(false);
        stat_time.setText(ConsoleUtils.formatDateHM(p.getAvgTurnTime()));
        stat_time.setEditable(false);
    }

    private void renderHistoryMoves() {
        this.list_moves.getItems().clear();
        for (GameTurn turn : game.getCurrentPlayer().getTurns()) {
            this.list_moves.getItems().add(turn.toString());
        }
    }

    private void renderAttackBoard() {
        this.attack_board.getChildren().clear();
        String[][] board = game.getCurrentPlayer().getAttackBoard().printBoard();
        for (int x = 1; x < board.length; x++) {
            for (int y = 1; y < board.length; y++) {
                Button n = new Button();
                n.minWidth(15);
                n.minHeight(15);
                n.setLayoutX(y * 35 - 35);
                n.setLayoutY(x * 35 - 35);
                n.setText(board[x][y]);

                if (board[x][y] == "~")
                    n.getStyleClass().add("empty-cell");
                else if (board[x][y] == "*")
                    n.getStyleClass().add("ship-hit");
                else
                    n.getStyleClass().add("miss-cell");

                int finalX = x;
                int finalY = y;
                n.setOnAction((event) -> {
                    TurnType turnType = game.playAttack(new ShipPoint(finalX, finalY));
                    if (turnType == TurnType.HIT) {
                        this.renderDialogSuccessTurn();
                    } else if (turnType == TurnType.MISS) {
                        this.renderDialogFailedTurn();
                    } else {
                        this.renderDialogAlreadyAttacked();
                    }
                    this.render();
                });
                this.attack_board.getChildren().add(n);
            }
        }
    }


}
