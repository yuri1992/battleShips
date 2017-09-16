package game.players;

import game.engine.GameTurn;
import game.engine.HitType;
import game.exceptions.BoardBuilderException;
import game.players.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int playerId;
    private int score = 0;

    private List<Ship> ships;

    private AttackBoard attackBoard;
    private ShipsBoard shipsBoard;

    public Player(List<Ship> ships, int boardSize, int playerId, int mine) throws BoardBuilderException {
        this.playerId = playerId;
        this.ships = ships;
        shipsBoard = new ShipsBoard(ships, boardSize + 1, mine);
        attackBoard = new AttackBoard(boardSize + 1);
    }

    /*
        Marking @pt as been attacked by the player.
     */
    public void markAttack(GridPoint pt, HitType hitType) {
        // Todo: Figure out if hitting a mine is equaling to hitting a ship
        attackBoard.setShoot(pt, hitType == HitType.HIT || hitType == HitType.HIT_MINE);
    }

    /*
        Return true when all of player ships been defeated
    */
    public boolean isLost() {
        return this.shipsBoard.allShipsGotHit();
    }

    @Override
    public String toString() {
        return "Player " + playerId;
    }

    public void updateScore(int points) {
        this.score += points;
    }

    //region Getters / Setters

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public AttackBoard getAttackBoard() {
        return attackBoard;
    }

    public ShipsBoard getShipsBoard() {
        return shipsBoard;
    }


    //endregion

}
