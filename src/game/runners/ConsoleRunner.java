package game.runners;

import game.engine.FileNotXmlFormat;
import game.engine.GameManager;
import game.engine.JAXBGameParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConsoleRunner {

    private boolean isGameRunning;
    private ConsoleMenu currentChoose;
    private GameManager game;
    private static final Scanner in = new Scanner(System.in);

    public ConsoleRunner() {
        this.isGameRunning = false;
        this.currentChoose = null;
    }

    public void start() {
        this.isGameRunning = true;
        while (this.isGameRunning) {
            this.printGameMenu();
            this.handleMenuInput();
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
        if (this.game != null && this.game.isRunning) {
            System.out.println("Game is already running, we can't load xml settings during a game.");
            return;
        }

        // Todo: Move this code to more generic function.
        String fileName = ConsoleUtils.getString();

        try {
            this.game = JAXBGameParser.loadGameFromXML(fileName);
            System.out.println("Xml File loaded successfully.");
            return;
        } catch (FileNotFoundException e) {
            System.out.println("Please verify that the file is exists.");
        } catch (FileNotXmlFormat fileNotXmlFormat) {
            System.out.println("File must be a valid xml format.");
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        loadGame();
    }

    private void startGame() {
    }

    private void showGameStatus() {
    }

    private void playTurn() {
    }

    private void showStatistics() {
    }

    private void resignGame() {
    }

    private void printGameMenu() {
        System.out.println("Please Select Option by Pressing Option Number");
        for (ConsoleMenu option : ConsoleMenu.values()) {
            System.out.println(option.ordinal() + 1 + "-" + option);
        }
    }


}
