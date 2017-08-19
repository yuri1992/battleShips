package game;

import game.engine.GameTurn;
import game.ships.Ship;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Player {

    @XmlElement(name = "ship")
    protected List<Ship> ships;

    NavyBoard navyBoard;
    GameTurn[] turns;

    public Player() {
        /*
            Build player  from xml file nmae
         */
        this.navyBoard = null;
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
            return true when all of player ships been defeated
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
