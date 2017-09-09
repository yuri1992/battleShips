package game.engine;

import descriptor.BattleShipGame;
import descriptor.Board;
import descriptor.ShipType;
import game.exceptions.BoardBuilderException;
import game.exceptions.BoardSizeIsTooBig;
import game.exceptions.DuplicatedShipTypesDecleared;
import game.exceptions.GameSettingsInitializationException;
import game.players.Player;
import game.ships.Ship;
import game.ships.ShipPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class GameManager {

    private GameMode mode = GameMode.BASIC;
    private int boardSize;
    private HashMap<String, ShipType> shipTypeHashMap;
    private List<Player> playerList;
    private boolean isRunning;
    private Player currentPlayer = null;
    private Player winner;
    private Date startAt = null;

    public GameManager(BattleShipGame gameDescriptor) throws GameSettingsInitializationException {
        mode = GameMode.valueOf(gameDescriptor.getGameType());
        validateConfiguraionProperties(gameDescriptor);

        mode = GameMode.valueOf(gameDescriptor.getGameType());
        boardSize = boardSize;


        this.setShipTypeHashMap(gameDescriptor.getShipTypes().getShipType());
        this.setPlayerList(gameDescriptor.getBoards().getBoard());
    }

    public void start() {
        this.isRunning = true;
        this.startAt = new Date();
        this.setCurrentPlayer(playerList.get(0));
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

    private void setShipTypeHashMap(List<ShipType> shipTypes) throws DuplicatedShipTypesDecleared {
        shipTypeHashMap = new HashMap<>();
        for (ShipType shipType : shipTypes) {
            if (shipTypeHashMap.containsKey(shipType.getId()))
                throw new DuplicatedShipTypesDecleared(shipType.getId() + " Already Declared.");
            shipTypeHashMap.put(shipType.getId(), shipType);
        }
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
    public boolean playAttack(ShipPoint pt) {
        Player currentPlayer = this.getCurrentPlayer();
        Player nextPlayer = this.getNextPlayer();

        if (currentPlayer.getAttackBoard().isAttacked(pt))
            return false;

        // Did player hit a ship
        boolean didHit = nextPlayer.hit(pt);
        currentPlayer.logAttack(pt, didHit);
        currentPlayer.endTurn();

        // Check if ship is hit and drowned
        if (didHit) {
            Ship s = nextPlayer.getShipsBoard().getShipByPoint(pt);
            if (s.isDrowned()) {
                currentPlayer.updateScore(s.getPoints());
            }
        }

        this.setCurrentPlayer(didHit ? currentPlayer : nextPlayer);
        return didHit;
    }

    public Player getNextPlayer() {
        // Todo: make this function more fancy and flexible to support multi user game.
        return playerList.get(0) != this.getCurrentPlayer() ? playerList.get(0) : playerList.get(1);
    }

    private void setPlayerList(List<Board> playerList) throws GameSettingsInitializationException {
        this.playerList = new ArrayList<>();
        for (Board board : playerList) {
            this.playerList.add(new Player(board.getShip(), this.getBoardSize(), this.getShipTypeHashMap()));
        }
    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentPlayer.startTurn();
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

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

    private void validateConfiguraionProperties(BattleShipGame gameDescriptor) throws BoardSizeIsTooBig, BoardBuilderException {

        try {
            GameMode.valueOf(gameDescriptor.getGameType());
        } catch (IllegalArgumentException e) {
            throw new BoardBuilderException("Illegal game type provided");
        }

        int boardSize = gameDescriptor.getBoardSize();
        if (boardSize > 20 || boardSize < 5) {
            throw new BoardSizeIsTooBig("Board size must be between 5 to 20");
        }


    }
}
