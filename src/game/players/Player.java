package game.players;

import game.engine.GameTurn;
import game.exceptions.BoardBuilderException;
import game.players.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int playerId;
    private int score = 0;

    private List<Ship> ships;
    private List<GameTurn> turns;
    private GameTurn currentTurn;

    private AttackBoard attackBoard;
    private ShipsBoard shipsBoard;

    public Player(List<Ship> ships, int boardSize, int playerId, int mine) throws BoardBuilderException {
        this.playerId = playerId;
        this.ships = ships;
        shipsBoard = new ShipsBoard(ships, boardSize + 1, mine);
        attackBoard = new AttackBoard(boardSize + 1);
        turns = new ArrayList<>();
    }

    /*
        Marking @pt as been attacked by the player.
     */
    public void logAttack(GridPoint pt, boolean b) {
        attackBoard.setShoot(pt, b);
        currentTurn.setHit(b);
        currentTurn.setPoint(pt);
    }

    public void startTurn() {
        if (this.currentTurn == null) {
            this.currentTurn = new GameTurn();
        }
    }

    public void endTurn() {
        if (this.currentTurn != null) {
            this.currentTurn.setEndAt();
            this.turns.add(this.currentTurn);
        }
        this.currentTurn = null;
    }

    public boolean placeMine(GridPoint gridPoint) {
        return this.shipsBoard.placeMine(gridPoint);
    }

    public boolean hit(GridPoint pt) {
        return this.getShipsBoard().hit(pt);
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

    public PlayerStatistics getStatistics() {
        return new PlayerStatistics(this);
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

    public List<GameTurn> getTurns() {
        return turns;
    }

    public GameTurn getCurrentTurn() {
        return currentTurn;
    }

    public AttackBoard getAttackBoard() {
        return attackBoard;
    }

    public ShipsBoard getShipsBoard() {
        return shipsBoard;
    }

    //endregion

}
