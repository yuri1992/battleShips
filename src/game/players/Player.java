package game.players;

import game.engine.GameTurn;
import game.engine.TurnType;
import game.exceptions.BoardBuilderException;
import game.ships.Ship;
import game.ships.ShipPoint;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int playerId;
    private List<Ship> ships;
    private List<GameTurn> turns;

    public GameTurn getCurrentTurn() {
        return currentTurn;
    }

    private GameTurn currentTurn;
    private AttackBoard attackBoard;
    private ShipsBoard shipsBoard;
    private int score = 0;

    public Player(List<Ship> ships, int boardSize, int playerId) throws BoardBuilderException {
        this.playerId = playerId;
        this.ships = ships;
        shipsBoard = new ShipsBoard(this.ships, boardSize + 1);
        attackBoard = new AttackBoard(boardSize + 1, boardSize + 1);
        turns = new ArrayList<>();
    }

    /*
        Checking if player has ship on @pt position, if do will mark this point as hitted.
     */
    public boolean hit(ShipPoint pt) {
        return this.getShipsBoard().hit(pt);
    }

    /*
        Return true when all of player ships been defeated
     */
    public boolean isLost() {
        for (Ship ship : ships) {
            if (!ship.isDrowned())
                return false;
        }
        return true;
    }

    /*
        Marking @pt as been attacked by the player.
     */
    public void logAttack(ShipPoint pt, boolean b) {
        attackBoard.setShoot(pt, b);
        currentTurn.setHit(b);
    }

    public PlayerStatistics getStatistics() {
        return new PlayerStatistics(this);
    }

    public AttackBoard getAttackBoard() {
        return attackBoard;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateScore(int points) {
        this.score += points;
    }

    public void setAttackBoard(AttackBoard attackBoard) {
        this.attackBoard = attackBoard;
    }

    public ShipsBoard getShipsBoard() {
        return shipsBoard;
    }

    public void setShipsBoard(ShipsBoard shipsBoard) {
        this.shipsBoard = shipsBoard;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<GameTurn> getTurns() {
        return turns;
    }

    public void startTurn() {
        if (this.currentTurn == null) {
            this.currentTurn = new GameTurn();
        }
    }

    public void endTurn() {
        this.currentTurn.setEndAt();
        this.turns.add(this.currentTurn);
        this.currentTurn = null;
    }

    @Override
    public String toString() {
        return "Player " + playerId;
    }
}
