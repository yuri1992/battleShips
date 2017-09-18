package runners.javafx.controllers;

import game.engine.GameManager;
import game.engine.GameState;
import game.engine.GameTurn;
import game.engine.HitType;
import game.players.BoardType;
import game.players.GridPoint;
import game.players.Player;
import game.players.PlayerStatistics;
import game.players.ships.Ship;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import runners.console.ConsoleUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PlayerScreenController extends BaseController {

    //region FXML Properties

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
    private Label stat_score; // Value injected by FXMLLoader

    @FXML // fx:id="list_moves"
    private ListView<String> list_moves; // Value injected by FXMLLoader

    @FXML // fx:id="player_name"
    private Label player_name; // Value injected by FXMLLoader

    @FXML
    private Button navigatePrevTurn;

    @FXML
    private Button navigateNextTurn;

    @FXML
    private VBox remain_container;

    @FXML
    private HBox mine_container;

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
    private RadioMenuItem theme_regular;

    @FXML
    private RadioMenuItem theme_dracula;

    @FXML
    private RadioMenuItem theme_white;

    @FXML
    private RadioMenuItem theme_games;

    @FXML
    private AnchorPane stats_container;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuHelp_About;

    @FXML
    private Label stat_score_opp;

    @FXML
    private Label stats_total_turn;

    @FXML
    private AnchorPane main_container;

    //endregion

    private MainMenuController menuController;
    private Stage window;
    private GameManager game;

    public void init(Stage window) {
        this.window = window;
        this.menuController = new MainMenuController(window);
    }

    //region Setters / Getters

    public void setGame(GameManager game) {
        this.game = game;
        enableGameRelatedMenuItems(game != null);
        setVisibility(false);
    }

    //endregion

    public void setVisibility(boolean visible) {
        this.list_moves.setVisible(visible);
        this.stats_container.setVisible(visible);
        this.ships_board.setVisible(visible);
        this.attack_board.setVisible(visible);
        this.stat_score.setVisible(visible);
        this.mine_container.setVisible(visible);

        boolean isPreviewMode = (game != null && game.getState() == GameState.REPLAY);
        navigatePrevTurn.setVisible(visible && isPreviewMode);
        navigateNextTurn.setVisible(visible && isPreviewMode);
        enableReplayRelatedMenuItems();
    }

    private void enableGameRelatedMenuItems(boolean isEnabled) {
        menuFile_StartGame.setDisable(!isEnabled || game == null);
        menuFile_ResignGame.setDisable(!isEnabled || game == null);
    }

    private void enableReplayRelatedMenuItems() {
        if (game == null) return;
        navigatePrevTurn.setDisable(!game.isUndoTurnAvailable());
        navigateNextTurn.setDisable(!game.isRedoTurnAvailable());
    }

    //region Handle User Events

    @FXML
    private void handleFileMenuItemPressed(ActionEvent event) {
        if (event.getSource() == menuFile_StartGame) {
            menuController.handleStartGameButtonPressed();
            this.setVisibility(true);
            if (game != null) {
                this.render();
            }
        } else if (event.getSource() == menuFile_LoadXML) {
            GameManager newGame = menuController.handleLoadXmlButtonPressed();
            if (newGame != null)
                setGame(newGame);
        } else if (event.getSource() == menuFile_ResignGame) {
            if (menuController.handleResignGamePressed()) {
                handleGameOver(true);
            }
        } else if (event.getSource() == menuFile_Quit) {
            menuController.handleQuitGamePressed();
        } else // TODO: Amir: throw exception??
            System.out.println("unknown");

    }

    @FXML
    void handleStyleThemeChange(ActionEvent event) {
        this.main_container.getStyleClass().clear();
        if (event.getSource() == theme_dracula) {
            this.main_container.getStyleClass().add("theme-dracula");
        } else if (event.getSource() == theme_games) {
            this.main_container.getStyleClass().add("theme-games");
        } else if (event.getSource() == theme_regular) {
            this.main_container.getStyleClass().add("theme-regular");
        } else if (event.getSource() == theme_white) {
            this.main_container.getStyleClass().add("theme-white");
        }
    }


    @FXML
    private void handleHelpMenuItemPressed(ActionEvent event) {
        if (event.getSource() == menuHelp_About) {
            menuController.handleAboutButtonPressed();
        }
    }

    @FXML
    private void handlePreviewNavigationPressed(ActionEvent event) {
        if (event.getSource() == navigatePrevTurn) {
            game.undoTurn();
            render();
        } else if (event.getSource() == navigateNextTurn) {
            game.redoTurn();
            render();
        }
        enableReplayRelatedMenuItems();
    }

    //endregion

    //region Alert Messages

    private void renderDialogSuccessTurn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Attack");
        alert.setHeaderText(null);
        alert.setContentText("Nice Shoot, You successfully attacked opposite ship");
        alert.showAndWait();
    }

    private void renderDialogFailedTurn() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("You Miss");
        alert.setHeaderText(null);
        alert.setContentText("You Miss, Switching Players, Please try again in the next turn");
        alert.showAndWait();
    }

    private void renderDialogAttckedMine() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stepping on a mine");
        alert.setHeaderText(null);
        alert.setContentText("You attcked a mine, the attack was reflacted to you.");
        alert.showAndWait();
    }

    private void renderDialogAlreadyAttacked() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Point Already Attacked");
        alert.setHeaderText(null);
        alert.setContentText("You already attcked this Point");
        alert.showAndWait();
    }

    private void renderPlaceMineFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("You can not put mine in that place");
        alert.setHeaderText(null);
        alert.setContentText("Mine, can't be located near to ship.");
        alert.showAndWait();
    }

    private void renderPlaceMineSuccessfully() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mine Placed Successfully.");
        alert.setHeaderText(null);
        alert.setContentText("Mine Placed Successfully, Skiping Turn.");
        alert.showAndWait();
    }

    //endregion

    private void render() {
        if (game == null) {
            this.setVisibility(false);
            return;
        }

        if (game.getState() == GameState.IN_PROGRESS) {
            if (game.isGameOver())
                handleGameOver(false);

            this.mine_container.setVisible(true);
            this.player_name.setText(game.getCurrentPlayer().toString());
            this.renderShipsBoard(null);
            this.renderAttackBoard(null);
            this.renderStatistics();
            this.renderHistoryMoves(null);
            this.renderMinesStack();
            this.renderRemainShips();
        } else if (game.getState() == GameState.REPLAY) {
            // It is possible that navigation get rto point where current player is null
            if (game.getCurrentPlayer() != null) {
                GridPoint pt = (game.getCurrentTurn() != null ? game.getCurrentTurn().getPoint() : null);
                GridPoint hitMine = (game.getCurrentTurn() != null && game.getCurrentTurn().getHitType() == HitType
                        .HIT_MINE ? pt : null);
                GameTurn turn = game.getCurrentTurn() != null ? game.getCurrentTurn() : null;
                this.player_name.setText(game.getCurrentPlayer().toString());
                this.renderShipsBoard(hitMine);
                this.renderAttackBoard(pt);
                this.renderStatistics(turn);
                this.renderHistoryMoves(turn);
                this.renderRemainShips();
                this.mine_container.setVisible(false);
            }
        }
    }

    private void renderShipsBoard(GridPoint highlightPoint) {
        BoardType[][] board = game.getCurrentPlayer().getShipsBoard().printBoard();
        this.ships_board.getChildren().clear();
        for (int row = 1; row < board.length; row++) {
            for (int col = 1; col < board.length; col++) {
                Button n = new Button();
                n.minWidth(15);
                n.minHeight(15);
                n.setLayoutX(col * 35 - 35);
                n.setLayoutY(row * 35 - 35);
                n.setText(" ");
                //n.setDisable(true);

                switch (board[row][col]) {
                    case MINE:
                        n.getStyleClass().add("mine");
                        break;
                    case SHIP:
                        n.getStyleClass().add("ship");
                        break;
                    case EMPTY:
                        n.getStyleClass().add("empty");
                        break;
                    case MISS:
                        n.getStyleClass().add("miss");
                        break;
                    case MINE_HIT:
                        n.getStyleClass().add("ship-hit");
                        break;
                    case SHIP_HIT:
                        n.getStyleClass().add("ship-hit");
                        break;
                }

                if (highlightPoint != null && highlightPoint.getX() == row && highlightPoint.getY() == col) {
                    n.getStyleClass().add("highlight-border");
                }

                int finalRow = row;
                int finalCol = col;
                if (game.isAllowMines()) {
                    n.setOnDragOver((event) -> {
                        event.acceptTransferModes(TransferMode.ANY);
                        event.consume();
                    });

                    n.setOnDragDropped((event) -> {
                        Dragboard db = event.getDragboard();
                        db.clear();
                        event.setDropCompleted(true);
                        event.consume();


                        // Avoiding getting drag gesture been played while Alert window is pop outed.
                        Task<Void> delayed = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                Thread.sleep(100);
                                return null;
                            }
                        };
                        delayed.setOnSucceeded(event1 -> {
                            if (game.placeMine(new GridPoint(finalRow, finalCol))) {
                                this.renderPlaceMineSuccessfully();
                                this.render();
                            } else {
                                this.renderPlaceMineFailed();
                            }
                        });
                        new Thread(delayed).start();
                    });
                }
                this.ships_board.getChildren().add(n);
            }
        }
    }

    private void renderAttackBoard(GridPoint highlightPoint) {
        this.attack_board.getChildren().clear();
        BoardType[][] board = game.getCurrentPlayer().getAttackBoard().printBoard();
        for (int row = 1; row < board.length; row++) {
            for (int col = 1; col < board.length; col++) {
                Button n = new Button();
                n.minWidth(15);
                n.minHeight(15);
                n.setLayoutX(col * 35 - 35);
                n.setLayoutY(row * 35 - 35);
                n.setText(" ");

                switch (board[row][col]) {
                    case MISS:
                        n.getStyleClass().add("miss");
                        break;
                    case EMPTY:
                        n.getStyleClass().add("empty");
                        break;
                    case SHIP_HIT:
                        n.getStyleClass().add("ship-hit");
                        break;
                    case MINE_HIT:
                        n.getStyleClass().add("ship-hit");
                        break;
                }

                if (highlightPoint != null && highlightPoint.getX() == row && highlightPoint.getY() == col) {
                    n.getStyleClass().add("highlight-border");
                }

                int finalCol = col;
                int finalRow = row;
                n.setOnAction((event) -> {
                    HitType turnType = game.playAttack(new GridPoint(finalRow, finalCol));
                    if (turnType == HitType.HIT) {
                        this.renderDialogSuccessTurn();
                    } else if (turnType == HitType.MISS) {
                        this.renderDialogFailedTurn();
                    } else if (turnType == HitType.HIT_MINE) {
                        this.renderDialogAttckedMine();
                    } else {
                        this.renderDialogAlreadyAttacked();
                    }
                    this.render();
                });
                this.attack_board.getChildren().add(n);
            }
        }
    }

    private void renderHistoryMoves(GameTurn untilTurn) {
        this.list_moves.getItems().clear();
        for (GameTurn turn : game.getPlayerMoves(game.getCurrentPlayer(), untilTurn)) {
            this.list_moves.getItems().add(turn.toString());
        }
    }

    private void renderRemainShips() {
        List<Ship> ships = this.game.getNextPlayer().getShipsBoard().getRemainShips();
        this.remain_container.getChildren().clear();
        HashMap<String, Integer> counter = new HashMap<String, Integer>();

        for (Ship ship : ships) {
            Integer count = counter.get(ship.getShipType());
            if (count == null) {
                count = 0;
            }
            count++;
            counter.put(ship.getShipType(), count);
        }

        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            Label text = new Label();
            text.setText(entry.getKey() + " remains " + entry.getValue() + " ships");
            text.getStyleClass().add("text-remaining");
            remain_container.getChildren().add(text);
        }
    }

    private void renderStatistics() {
        PlayerStatistics p = game.getPlayerStatistics(game.getCurrentPlayer(), null);
        displayStatisticsField(p);

        stats_total_turn.setText("Playing Turn No: " + (game.getMovesCount() + 1));
    }

    private void renderStatistics(GameTurn untilTurn) {
        PlayerStatistics p = game.getPlayerStatistics(game.getCurrentPlayer(), untilTurn);
        displayStatisticsField(p);

        if (untilTurn != null)
            stats_total_turn.setText("Playing Turn No: " + untilTurn.getIndex());
    }

    private void displayStatisticsField(PlayerStatistics p) {
        stat_score.setText("Score: " + String.valueOf(p.getScore()));
        stat_score_opp.setText("Opponent Score: " + game.getNextPlayer().getScore());
        stat_turns.setText(String.valueOf(p.getTurns()));
        stat_turns.setEditable(false);
        stat_hits.setText(String.valueOf(p.getHits()));
        stat_hits.setEditable(false);
        stat_miss.setText(String.valueOf(p.getMisses()));
        stat_miss.setEditable(false);
        stat_time.setText(ConsoleUtils.formatDateHM(p.getAvgTurnTime()));
        stat_time.setEditable(false);
    }

    private void renderMinesStack() {
        int mines = this.game.getCurrentPlayer().getShipsBoard().getAvailableMines();
        mine_container.getChildren().clear();
        if (mines == 0) {
            Label text = new Label();
            text.setText("No Mine Available");
            mine_container.getChildren().add(text);
        } else {
            for (int i = 0; i < mines; i++) {
                Button mine = new Button();
                mine.minWidth(15);
                mine.minHeight(15);
                mine.setText(" ");
                mine.getStyleClass().add("mine");

                mine.setOnDragDetected((event -> {
                    Dragboard db = mine.startDragAndDrop(TransferMode.ANY);
                    WritableImage snapshot = mine.snapshot(new SnapshotParameters(), null);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("");
                    db.setContent(content);
                    db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);
                    event.consume();
                }));

                mine.setOnDragDone((event -> {
                    event.acceptTransferModes(TransferMode.ANY);
                    event.consume();
                }));
                mine_container.getChildren().add(mine);
            }
        }
    }

    private void handleGameOver(boolean byResign) {

        String reason = (byResign ? this.game.getCurrentPlayer() + " resigned from the game.\n" : "");
        String winner = "The Winner is " + this.game.getWinner() + "!\n";
        StringBuilder scores = new StringBuilder();
        for (Player p : this.game.getPlayerList()) {
            scores.append(p.toString() + ":     " + p.getScore() + " points\n");
        }
        String congrats = "Congratulations!!";
        AlertBoxController.displayAlert("Game Over!", reason + winner + scores.toString() + congrats);

        enableGameRelatedMenuItems(false);
        setVisibility(true); // start preview mode
    }
}
