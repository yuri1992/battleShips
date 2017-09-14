package game.players;

import descriptor.ShipType;
import game.engine.GameTurn;
import game.exceptions.GameSettingsInitializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {

    private int playerId;
    private int score = 0;

    private List<GameTurn> turns;
    private GameTurn currentTurn;

    private AttackBoard attackBoard;
    private ShipsBoard shipsBoard;


    public Player(List<descriptor.Ship> ships, int boardSize, HashMap<String, ShipType> shipTypeHashMap, int playerId, int mine) throws
            GameSettingsInitializationException {

        this.playerId = playerId;
        setShipsBoard(ships, shipTypeHashMap, boardSize + 1, mine);
        attackBoard = new AttackBoard(boardSize + 1);
        turns = new ArrayList<>();
    }

    /*
        Return true when all of player ships been defeated
     */
    public boolean isLost() {
        return this.shipsBoard.allShipsGotHit();
    }

    /*
        Marking @pt as been attacked by the player.
     */
    public void logAttack(GridPoint pt, boolean b) {
        attackBoard.setShoot(pt, b);
        currentTurn.setHit(b);
        currentTurn.setPoint(pt);
    }

    private void setShipsBoard(List<descriptor.Ship> descriptorShips, HashMap<String, ShipType> shipTypeHashMap, int boardSize, int minesAllowed) throws GameSettingsInitializationException {
        List<Ship> ships = new ArrayList<>();

        for (descriptor.Ship item : descriptorShips) {
            String shipTypeId = item.getShipTypeId();
            ships.add(new Ship(shipTypeId, item.getDirection(), item.getPosition(), shipTypeHashMap.get(shipTypeId)));
        }

        shipsBoard = new ShipsBoard(ships, boardSize, minesAllowed);
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

    @Override
    public String toString() {
        return "Player " + playerId;
    }

    public GameTurn getCurrentTurn() {
        return currentTurn;
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

    public List<GameTurn> getTurns() {
        return turns;
    }

    public boolean hit(GridPoint pt) {
        return this.getShipsBoard().hit(pt);
    }
}
