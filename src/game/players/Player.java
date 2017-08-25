package game.players;

import descriptor.ShipType;
import game.engine.GameTurn;
import game.ships.Ship;
import game.ships.ShipPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {

    private List<Ship> ships;
    private List<GameTurn> turns;
    private GameTurn currentTurn;
    private AttackBoard attackBoard;
    private ShipsBoard shipsBoard;
    private int score = 0;

    public Player(List<descriptor.Ship> ships, int boardSize, HashMap<String, ShipType> shipTypeHashMap) throws ShipsLocatedTooClose, NotEnoughShipsLocated {
        this.setShips(ships, shipTypeHashMap);
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

    public void setShips(List<descriptor.Ship> ships, HashMap<String, ShipType> shipTypeHashMap) throws NotEnoughShipsLocated {
        this.ships = new ArrayList<>();
        HashMap<String, Integer> shipsByType = new HashMap<String, Integer>();

        ships.forEach(item -> {
            String shipTypeId = item.getShipTypeId();
            shipsByType.put(shipTypeId, shipsByType.getOrDefault(shipTypeId, 0) + 1);
            this.ships.add(new Ship(shipTypeId, item.getDirection(), item.getPosition(), shipTypeHashMap.get(shipTypeId)));
        });

        // Validation of number of ships per type.
        for (HashMap.Entry<String, Integer> entry : shipsByType.entrySet()) {
            int amount = shipTypeHashMap.get(entry.getKey()).getAmount();
            if (amount != entry.getValue())
                throw new NotEnoughShipsLocated("Ship typeId " + entry.getKey() + " must be located " + amount + " times only.");
        }
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
}
