package game.engine;

import descriptor.BattleShipGame;
import descriptor.Board;
import descriptor.ShipType;
import game.players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GameManager {

    private GameMode mode = GameMode.BASIC;
    private int boardSize;
    private HashMap<String, ShipType> shipTypeHashMap;
    private List<Player> playerList;
    private boolean isRunning;
    private Player currentPlayer = null;

    public GameManager(BattleShipGame gameDescriptor) {
        mode = GameMode.valueOf(gameDescriptor.getGameType());
        boardSize = gameDescriptor.getBoardSize();
        this.setShipTypeHashMap(gameDescriptor.getShipTypes().getShipType());
        this.setPlayerList(gameDescriptor.getBoards().getBoard());
    }


    public void start() {
        this.isRunning = true;
        this.currentPlayer = playerList.get(0);
    }

    private void setShipTypeHashMap(List<ShipType> shipTypes) {
        shipTypeHashMap = new HashMap<String, ShipType>();
        for (ShipType shipType : shipTypes) {
            shipTypeHashMap.put(shipType.getId(), shipType);
        }
    }

    /*
        Shooting the the @pt requests by the current player, if player did hit a ship, will have another turn,
        otherwise will switch the turn.
     */
    public boolean playAttack(ShipPoint pt) {
        Player currentPlayer = this.getCurrentPlayer();
        Player nextPlayer = this.getNextPlayer();

        if (currentPlayer.getAttackBoard().isAttacked(pt))
            return false;


        if (nextPlayer.hit(pt)) {
            // Player hit a ship.
            currentPlayer.logAttack(pt, true);
            return true;
        }

        currentPlayer.logAttack(pt, false);
        this.setCurrentPlayer(nextPlayer);
        return false;

    }

    public Player getNextPlayer() {
        // Todo: make this function more fancy and flexible to support multi user game.
        return playerList.get(0) != this.getCurrentPlayer() ? playerList.get(0) : playerList.get(1);
    }

    public void setPlayerList(List<Board> playerList) {
        this.playerList = new ArrayList<Player>();
        playerList.forEach((Board board) -> {
            this.playerList.add(new Player(board.getShip(), this.getBoardSize(), this.getShipTypeHashMap()));
        });
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setShipTypeHashMap(HashMap<String, ShipType> shipTypeHashMap) {
        this.shipTypeHashMap = shipTypeHashMap;
    }

    public GameMode getMode() {
        return mode;
    }

    public HashMap<String, ShipType> getShipTypeHashMap() {
        return shipTypeHashMap;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
