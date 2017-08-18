package game.runners;

import game.engine.GameManager;

import java.io.File;
import java.util.Scanner;

public class ConsoleRunner {

    boolean isGameRunning;
    ConsoleMenu currentChoose;
    GameManager game;
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
        int menuOption = ConsoleUtils.getIntegerByRange();
        this.currentChoose = ConsoleMenu.values()[menuOption - 1];
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
        if (this.game.isRunning) {
            System.out.println("Game is already running, we can't load xml settings during a game.");
            return;
        }


        // Todo: Move this code to more generic function.
        String fileName = ConsoleUtils.getString();
        File f = new File(fileName);

        if(!f.exists()) {
            System.out.println("Please verify that the file is exists.");
            return;
        }

        if (!f.isFile()) {
            System.out.println("Please verify that the path you provide is a file.");
            return;
        }

        if (!f.getPath().endsWith(".xml")) {
            System.out.println("We are support only xml files.");
        }



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
            System.out.println(option.ordinal() + "-" + option);
        }
    }


}
