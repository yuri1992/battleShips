package game;

import game.engine.GameTurn;
import game.submarines.Submarine;

public class Player {
    Submarine[] submarines;
    NavyBoard navyBoard;
    GameTurn[] turns;

    public Player(String xml) {
        /*
            Build player  from xml file nmae
         */
    }

    public PlayerStatistics getStatistics() {
        return new PlayerStatistics();
    }

    public void playTurn() {
        /*
            Playing next turn
         */
    }

    public boolean isLost() {
        /*
            return true when all of player submarines been defeated
         */
        return false;
    }

    public boolean isValid() {
        /*
            return true when player is valid and ready to play.
         */

        return false;
    }

}
