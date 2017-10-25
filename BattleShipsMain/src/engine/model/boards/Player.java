package engine.model.boards;

import engine.exceptions.BoardBuilderException;
import engine.managers.game.HitType;
import engine.model.ships.Ship;

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

    public Player(Player p) throws BoardBuilderException {
        this.playerId = p.playerId;
        this.ships = new ArrayList<>();
        for (Ship s : p.getShips()) {
            this.ships.add(new Ship(s));
        }
        shipsBoard = new ShipsBoard(ships, p.shipsBoard.getBoardSize(), p.getShipsBoard().getMinesAllowance());
        attackBoard = new AttackBoard(p.shipsBoard.getBoardSize());
    }

    /*
        Marking @pt as been attacked by the player.
     */
    public void markAttack(GridPoint pt, HitType hitType) {
        attackBoard.setShoot(pt, hitType);
    }

    public void unmarkAttack(GridPoint pt) {
        attackBoard.setUnShoot(pt);
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
