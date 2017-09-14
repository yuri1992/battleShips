package game.engine;

import game.ships.ShipType;
import game.exceptions.*;
import game.players.Player;
import game.ships.Ship;
import game.ships.ShipPoint;

import java.util.*;


public class GameManager {

    private GameMode mode = GameMode.BASIC;
    private int boardSize;
    private List<ShipType> shipTypes;
    private List<Player> playerList;
    private boolean isRunning;
    private Player currentPlayer = null;
    private Player winner;
    private Date startAt = null;
    private boolean isInitialize = false;

    public GameManager(GameMode mode, int boardSize, List<ShipType> shipTypes, List<Player> players) {
        this.mode = mode;
        this.boardSize = boardSize;
        this.shipTypes = shipTypes;
        this.playerList = players;

        this.isInitialize = true;
    }

    //region Setters / Getters

    public GameMode getMode() {
        return mode;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public List<ShipType> getShipTypes() {
        return shipTypes;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

    //endregion

    public void start() {
        this.isRunning = true;
        this.startAt = new Date();
        this.setCurrentPlayer(playerList.get(0));
        this.prepareNextTurn();
    }

    public void finishGame() {
        this.isRunning = false;
    }

    public void resignGame() {
        this.isRunning = false;
        this.setWinner(this.getNextPlayer());
    }

    public GameStatistics getStatistics() {
        return new GameStatistics(this.startAt, this.playerList);
    }

    /*
        Return true, when game is over, meaning that one of the players was defeated.
     */
    public boolean isGameOver() {
        for (Player p : playerList) {
            if (p.isLost()) {
                if (p != getCurrentPlayer()) {
                    this.setWinner(getCurrentPlayer());
                } else {
                    this.setWinner(getNextPlayer());
                }
                return true;
            }

        }
        return false;
    }

    /*
        Shooting the the @pt requests by the current player, if player did hit a ship, will have another turn,
        otherwise will switch the turn.
     */
    public TurnType playAttack(ShipPoint pt) {
        Player currentPlayer = this.getCurrentPlayer();
        Player nextPlayer = this.getNextPlayer();

        if (currentPlayer.getAttackBoard().isAttacked(pt))
            return TurnType.NOT_EMPTY;

        // Did player hit a ship
        boolean didHit = nextPlayer.hit(pt);
        currentPlayer.logAttack(pt, didHit);
        currentPlayer.getCurrentTurn().setPoint(pt);
        currentPlayer.endTurn();

        // Check if ship is hit and drowned
        if (didHit) {
            Ship s = nextPlayer.getShipsBoard().getShipByPoint(pt);
            if (s.isDrowned()) {
                currentPlayer.updateScore(s.getPoints());
            }
        }

        this.setCurrentPlayer(didHit ? currentPlayer : nextPlayer);
        this.prepareNextTurn();
        return didHit ? TurnType.HIT : TurnType.MISS;
    }

    public void prepareNextTurn() {
        this.getCurrentPlayer().startTurn();
    }

    public Player getNextPlayer() {
        // Todo: make this function more fancy and flexible to support multi user game.
        return playerList.get(0) != this.getCurrentPlayer() ? playerList.get(0) : playerList.get(1);
    }

    public boolean placeMine(ShipPoint shipPoint) {
        return false;
    }
}
