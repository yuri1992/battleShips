package game.runners;

import descriptor.BattleShipGame;
import game.engine.*;
import game.players.NotEnoughShipsLocated;
import game.players.Player;
import game.players.ShipsLocatedTooClose;
import game.ships.ShipPoint;

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
            if (this.game != null && this.game.isRunning() && this.game.isGameOver()) {
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

        }
    }

    private void loadGame() {
        if (this.game != null && this.game.isRunning()) {
            System.out.println("Game is already running, we can't load xml settings during a game.");
            return;
        }

        System.out.println("Please enter the path of the XML file.");
        String fileName = ConsoleUtils.getString();

        try {
            BattleShipGame gameDescriptor = JAXBGameParser.loadGameFromXML(fileName);
            this.game = new GameManager(gameDescriptor);
            System.out.println("XML File loaded successfully.");
            return;
        } catch (FileNotFoundException e) {
            System.out.println("Please verify that the file is exists.");
        } catch (FileNotXmlFormat fileNotXmlFormat) {
            System.out.println("File must be a valid XML format.");
        } catch (JAXBException e) {
            System.out.println("XML file is not valid, please make sure your xml file meet the xsd file.");
            //            e.printStackTrace();
        } catch (NotEnoughShipsLocated | BoardSizeIsTooBig | ShipsLocatedTooClose e) {
            System.out.println("ERROR PARSING XML FILE: " + e.getMessage());
        }

        loadGame();
    }

    private void startGame() {
        if (this.game != null && this.game.isRunning()) {
            System.out.println("Game already started, you can't start it again.");
            return;
        }

        if (this.game == null) {
            System.out.println("You should load XML settings file first.");
            return;
        }

        this.game.start();
        this.showGameStatus();

    }

    private void showGameStatus() {
        Player currentPlayer = this.game.getCurrentPlayer();
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Hello Player " + currentPlayer.toString() + " Please play your turn.");

        System.out.println("Your current score is " + currentPlayer.getScore());
        this.printPlayerBoards(currentPlayer);
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

    private void printBoard(String[][] board) {
        System.out.format("%-3s", "\\");
        for (int y = 1; y < board.length; y++) {
            System.out.format("%-3s", y);
        }
        System.out.println();

        for (int i = 1; i < board.length; i++) {
            System.out.format("%-3s", i);
            for (int y = 1; y < board.length; y++) {
                System.out.format("%-3s", board[i][y]);
            }
            System.out.println();
        }
    }

    private void playTurn() {
        System.out.println("Attack! Please select the cell location you want to attack.");

        System.out.println("Please Enter the Column(X):");
        int x = ConsoleUtils.getIntegerByRange(1, game.getBoardSize());
        System.out.println("Please Enter the Row(Y):");
        int y = ConsoleUtils.getIntegerByRange(1, game.getBoardSize());
        ShipPoint fireToPoint = new ShipPoint(x, y);

        // Making attack to the request point.
        if (this.game.playAttack(fireToPoint)) {
            System.out.println("NICE JOB! you successfully hit a ship.");
        }

        this.showGameStatus();
    }

    private void showStatistics() {
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Statistics: ...");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("-----------------------------------------------------------------------------------");
    }

    private void resignGame() {
        this.isGameRunning = false;
        this.game.resignGame();

        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Player " + this.game.getCurrentPlayer() + " Resign form the game.");
        System.out.println("The Winner is " + this.game.getWinner() + " Congratulations");
        System.out.println("Hope to see you soon.");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");

        this.showStatistics();
    }

    private void gameOver() {
        this.isGameRunning = false;
        this.game.finishGame();

        this.printPlayerBoards(game.getCurrentPlayer());
        this.printPlayerBoards(game.getNextPlayer());
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("The Winner is " + this.game.getWinner() + " Congratulations");
        System.out.println("Hope to see you soon.");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-----");
    }

    private void printGameMenu() {
        System.out.println("Please Select Option by Pressing Option Number");
        for (ConsoleMenu option : ConsoleMenu.values()) {
            System.out.println(option.ordinal() + 1 + "-" + option);
        }
    }


}
