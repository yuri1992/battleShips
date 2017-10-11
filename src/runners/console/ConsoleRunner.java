package runners.console;

import engine.exceptions.InvalidFileFormatException;
import engine.exceptions.GameSettingsInitializationException;
import engine.managers.game.*;
import engine.model.boards.BoardType;
import engine.model.boards.Player;
import engine.model.boards.GridPoint;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class ConsoleRunner {

    private boolean isGameRunning;
    private ConsoleMenu currentChoose;
    private GameManager game;

    public ConsoleRunner() {
        this.isGameRunning = false;
        this.currentChoose = null;
    }

    public void start() {
        this.isGameRunning = true;
        while (this.isGameRunning) {
            if (this.game != null && game.getState() == GameState.IN_PROGRESS && this.game.isGameOver()) {
                this.gameOver();
            } else {
                this.printGameMenu();
                this.handleMenuInput();
            }
        }
    }

    private void handleMenuInput() {
        ConsoleMenu[] values = ConsoleMenu.values();
        int menuOption = ConsoleUtils.getIntegerByRange(1, values.length + 1);
        this.currentChoose = values[menuOption - 1];
        this.handleMenuChoose();
    }

    private void handleMenuChoose() {
        switch (this.currentChoose) {
            case LOAD_GAME:
                this.loadGame();
                break;

            case START_GAME:
                this.startGame();
                break;

            case SHOW_GAME_STATUS:
                this.showGameStatus();
                break;

            case PLAY_TURN:
                this.playTurn();
                break;

            case SHOW_STATISTICS:
                this.showStatistics();
                break;

            case RESIGN_GAME:
                this.resignGame();
                break;

            case END_GAME:
                this.endGame();
                break;

        }
    }

    private void loadGame() {
        if (this.game != null && game.getState() == GameState.IN_PROGRESS) {
            System.out.println("Game is already running, we can't load xml settings during a managers.");
            return;
        }

        System.out.println("Please enter the path of the XML file.");
        String fileName = ConsoleUtils.getString();

        try {
            this.game = GameManagerFactory.loadGameManager(fileName);
            System.out.println("XML File loaded successfully.");
            return;
        } catch (FileNotFoundException e) {
            System.out.println("Please verify that the file is exists.");
        } catch (InvalidFileFormatException fileNotXmlFormat) {
            System.out.println("File must be a valid XML format.");
        } catch (JAXBException e) {
            System.out.println("XML file is not valid, please make sure your xml file meet the xsd file.");
            //            e.printStackTrace();
        } catch (GameSettingsInitializationException e) {
            System.out.println("ERROR PARSING XML FILE: " + e.getMessage());
        } finally {
            System.out.println();
        }

        loadGame();
    }

    private void startGame() {
        if (this.game != null && game.getState() == GameState.IN_PROGRESS) {
            System.out.println("Game already started, you can't start it again.");
            return;
        }

        if (this.game == null) {
            System.out.println("You should load XML settings file first.");
            return;
        }

        this.game.startGame();
        this.showGameStatus();

    }

    private void showGameStatus() {
        if (game == null) {
            System.out.println("Game status is not available before starting the managers");
            System.out.println();
            return;
        }

        Player currentPlayer = this.game.getCurrentPlayer();
        if (currentPlayer != null) {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("Hello Player " + currentPlayer.toString() + " Please play your turn.");
            System.out.println("Your current score is " + currentPlayer.getScore());
            this.printPlayerBoards(currentPlayer);
        }
    }

    private void printPlayerBoards(Player currentPlayer) {
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Your Ships board.");
        System.out.println();
        this.printBoard(currentPlayer.getShipsBoard().printBoard());
        System.out.println();
        System.out.println("Your Attacking board.");
        System.out.println();
        this.printBoard(currentPlayer.getAttackBoard().printBoard());
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------");
    }

    private void printBoard(BoardType[][] board) {
        System.out.format("%-3s", "\\");
        for (int col = 1; col < board.length; col++) {
            System.out.format("%-3s", col);
        }
        System.out.println();

        for (int row = 1; row < board.length; row++) {
            System.out.format("%-3s", row);
            for (int col = 1; col < board.length; col++) {
                System.out.format("%-3s", board[row][col]);
            }
            System.out.println();
        }
    }

    private void playTurn() {
        if (game == null) {
            System.out.println("You must to initialize the managers by providing XML file.");
            System.out.println();
            return;
        }
        if (game.getState() != GameState.IN_PROGRESS) {
            System.out.println("Game is not running.");
            System.out.println();
            return;
        }

        game.startTurn();
        System.out.println("Attack! Please select the cell location you want to attack.");

        System.out.println("Please Enter the Row:");
        int x = ConsoleUtils.getIntegerByRange(1, game.getBoardSize());
        System.out.println("Please Enter the Column:");
        int y = ConsoleUtils.getIntegerByRange(1, game.getBoardSize());
        GridPoint fireToPoint = new GridPoint(x, y);

        // Making attack to the request point.
        if (this.game.playAttack(fireToPoint) == HitType.HIT) {
            System.out.println("NICE JOB! you successfully hit a ship.");
            System.out.println();
        }

        this.showGameStatus();
    }

    private void showStatistics() {
        if (this.game == null) {
            System.out.println("Statistics are available when managers started.");
            System.out.println();
            return;
        }
        GameStatistics statistics = this.game.getGameStatistics();
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Total Turns Played " + statistics.getTurns());
        System.out.println("Total Time " + ConsoleUtils.formatDateHM(statistics.getTotalTime()));
        statistics.getPlayerStatistics().forEach(playerStatistic -> {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println(playerStatistic.getName() + " Statistics:");
            System.out.println(playerStatistic.getScore() + " points");
            System.out.println("Number of Turns: " + playerStatistic.getTurns());
            System.out.println("Number of Hits: " + playerStatistic.getHits());
            System.out.println("Number of Misses: " + playerStatistic.getMisses());
            System.out.println("Avg Turn Time: " + ConsoleUtils.formatDateHM(playerStatistic.getAvgTurnTime()));
            System.out.println("-----------------------------------------------------------------------------------");
        });
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------");
    }

    private void resignGame() {
        if (game == null || !this.isGameRunning) {
            System.out.println("Player can't resign if a managers is not in progress...");
            System.out.println();
            return;
        }

        this.game.resignGame();

        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Player " + this.game.getCurrentPlayer() + " resigned from the managers.");
        System.out.println("The Winner is " + this.game.getWinner() + " Congratulations");

        for (Player p : game.getPlayerList()) {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println(p.toString() + "'s board:");
            System.out.println();
            this.printBoard(p.getShipsBoard().printBoard());
            System.out.println();
        }

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Hope to see you soon.");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
        System.out.println();
        this.showStatistics();

        this.game = null;
    }

    private void gameOver() {
        this.game.finishGame();

        this.printPlayerBoards(game.getCurrentPlayer());
        this.printPlayerBoards(game.getNextPlayer());
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("The Winner is " + this.game.getWinner() + " Congratulations");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
        this.showStatistics();

        this.game = null;
    }

    private void endGame() {
        this.isGameRunning = false;
        if (this.game != null && game.getState() == GameState.IN_PROGRESS)
            this.game.finishGame();
        System.out.println("You decided to exited the managers, hope to see you soon.");
    }

    private void printGameMenu() {
        System.out.println("Please Select Option by Pressing Option Number");
        for (ConsoleMenu option : ConsoleMenu.values()) {
            System.out.println(option.ordinal() + 1 + "-" + option);
        }
    }


}
