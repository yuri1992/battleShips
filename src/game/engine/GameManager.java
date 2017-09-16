package game.engine;

import game.players.GridPoint;
import game.players.Player;
import game.players.PlayerStatistics;
import game.players.ships.Ship;
import game.players.ships.ShipType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class GameManager {

    private GameMode mode = GameMode.BASIC;
    private int boardSize;
    private List<ShipType> shipTypes;
    private List<Player> playerList;
    private List<GameTurn> turnList;
    private GameTurn currentTurn;

    private boolean isRunning;
    private Player currentPlayer = null;
    private Player winner;
    private Date startTime = null;
    private boolean allowMines = false;

    public GameManager(GameMode mode, int boardSize, List<ShipType> shipTypes, List<Player> players, boolean
            allowMines) {
        this.mode = mode;
        this.boardSize = boardSize;
        this.shipTypes = shipTypes;
        this.playerList = players;
        this.allowMines = allowMines;
        this.turnList = new ArrayList<>();
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

    public List<GameTurn> getTurnList() {
        return turnList;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void setRunning(boolean running) {
        isRunning = running;
    }

    private void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return winner;
    }

    public Date getStartTime() {
        return startTime;
    }

    public boolean isAllowMines() {
        return allowMines;
    }

    //endregion

    public void startGame() {
        this.isRunning = true;
        this.startTime = new Date();
        this.setCurrentPlayer(playerList.get(0));
        startTurn();
    }

    public void resignGame() {
        this.isRunning = false;
        this.setWinner(this.getNextPlayer());
    }

    public void finishGame() {
        this.isRunning = false;
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
                setRunning(false);
                return true;
            }

        }
        return false;
    }

    public void startTurn() {
        if (currentTurn == null) {
            currentTurn = new GameTurn(getMovesCount() + 1, getCurrentPlayer());
        }
    }

    private void updateTurnResults(GridPoint pt, HitType hitType) {
        // Todo: Figure out if hitting a mine is equaling to hitting a ship
        currentTurn.setHitType(hitType);
        currentTurn.setPoint(pt);
        getCurrentPlayer().markAttack(pt, hitType);
    }

    public void endTurn() {
        if (currentTurn != null) {
            currentTurn.end();
            turnList.add(currentTurn);
        }
        currentTurn = null;
    }

    private void switchTurns() {
        endTurn();
        this.setCurrentPlayer(this.getNextPlayer());
        startTurn();
    }

    private void prepareNextTurn() {
        endTurn();
        startTurn();
    }

    public Player getNextPlayer() {
        // Todo: make this function more fancy and flexible to support multi user game.
        return playerList.get(0) != this.getCurrentPlayer() ? playerList.get(0) : playerList.get(1);
    }

    /*
        Shooting the the @pt requests by the current player, if player did hit a ship, will have another turn,
        otherwise will switch the turn.
    */
    public HitType playAttack(GridPoint pt) {
        Player currPlayer = this.getCurrentPlayer();
        Player nextPlayer = this.getNextPlayer();

        if (currPlayer.getAttackBoard().isAttacked(pt))
            return HitType.NOT_EMPTY;

        // Did player hit a ship
        HitType hitType = nextPlayer.getShipsBoard().hit(pt);


        if (hitType == HitType.MISS) {
            updateTurnResults(pt, hitType);
            this.switchTurns();
            return hitType;
        }

        Ship s = null;
        Player player = null;

        if (hitType == HitType.HIT) {
            player = currPlayer;
            s = nextPlayer.getShipsBoard().getShipByPoint(pt);
            updateTurnResults(pt, hitType);
            this.prepareNextTurn();
        } else {
            player = nextPlayer;
            s = currPlayer.getShipsBoard().getShipByPoint(pt);

            // Marking attack of the mine
            updateTurnResults(pt, hitType);
            // Marking attack of the mine
            nextPlayer.getAttackBoard().setShoot(pt, s != null);
            this.switchTurns();
        }

        if (s != null && s.isDrowned()) {
            player.updateScore(s.getPoints());
        }

        return hitType;
    }

    public boolean placeMine(GridPoint gridPoint) {
        if (this.currentPlayer.getShipsBoard().setMine(gridPoint) && currentTurn != null) {
            this.currentTurn.setPoint(gridPoint);
            this.currentTurn.setHitType(HitType.PLACE_MINE);
            this.switchTurns();
            return true;
        }
        return false;
    }

    public GameStatistics getGameStatistics() {
        return new GameStatistics(this);
    }

    public PlayerStatistics getPlayerStatistics(Player player) {
        return new PlayerStatistics(player, getPlayerMoves(player));
    }

    public int getMovesCount() {
        return getTurnList().size();
    }

    public List<GameTurn> getPlayerMoves(Player player) {
        return getTurnList().stream().filter(t -> player.equals(t.getPlayer())).collect(Collectors.toList());
    }

}
