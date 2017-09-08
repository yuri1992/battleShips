package game.runners.javafx.controllers;

import game.engine.GameManager;
import game.engine.GameTurn;
import game.players.PlayerStatistics;
import game.runners.console.ConsoleUtils;
import game.ships.ShipPoint;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerScreenController extends BaseController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="quit"
    private Button quit; // Value injected by FXMLLoader

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
    void handleOnPlaceMine(ActionEvent event) {
        for (Node child : this.ships_board.getChildren()) {
            if (((Button) child).getText().equals("~"))
                child.setDisable(false);
        }
        place_mine.setDisable(true);
    }

    private GameManager game;


    public void init(GameManager game) {
        this.game = game;
        this.render();
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

    public void render() {
        this.player_name.setText(game.getCurrentPlayer().toString());

        String[][] board = game.getCurrentPlayer().getShipsBoard().printBoard();
        this.ships_board.getChildren().clear();
        for (int y = 1; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                Button n = new Button();
                n.minWidth(10);
                n.minHeight(10);
                n.setLayoutX(x * 35);
                n.setLayoutY(y * 35);
                n.setText(board[y][x]);
                n.setDisable(true);

                if (board[y][x] == "~")
                    n.setStyle("empty-cell");
                else if (board[y][x] == "@")
                    n.setStyle("ship-cell");
                else
                    n.setStyle("ship-hit");

                this.ships_board.getChildren().add(n);
            }
        }

        this.attack_board.getChildren().clear();
        board = game.getCurrentPlayer().getAttackBoard().printBoard();
        for (int y = 1; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                Button n = new Button();
                n.minWidth(10);
                n.minHeight(10);
                n.setLayoutX(x * 35);
                n.setLayoutY(y * 35);
                n.setText(board[y][x]);
                int finalX = x;
                int finalY = y;
                n.setOnAction((event) -> {
                    if (game.playAttack(new ShipPoint(finalX, finalY))) {
                        this.renderDialogSuccessTurn();
                    } else {
                        this.renderDialogFailedTurn();
                    }
                    this.render();
                });
                this.attack_board.getChildren().add(n);
            }
        }

        this.list_moves.getItems().clear();
        for (GameTurn turn : game.getCurrentPlayer().getTurns()) {
            this.list_moves.getItems().add(turn.toString());
        }

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

}
