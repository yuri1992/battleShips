package game.engine;

import game.players.Player;
import game.players.PlayerStatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    Total Number of turns.
    Total Time of the game.
    For Each Player:
        - How many hits
        - How many misses.
        - Average Time of each turn.
 */
public class GameStatistics {
    private int turns;
    private Date startTime;
    private List<PlayerStatistics> playerStatistics;


    public GameStatistics(GameManager game) {
        startTime = game.getStartTime();
        turns = (int)game.getTurnList().stream().filter(t -> t.isOver()).count();

        playerStatistics = new ArrayList<>();
        game.getPlayerList().forEach(player -> {
            this.playerStatistics.add(new PlayerStatistics(player, game.getPlayerMoves(player)));
        });
    }

    public long getTotalTime() {
        return startTime == null ? 0 : new Date().getTime() - startTime.getTime();
    }

    public int getTurns() {
        return turns;
    }

    public List<PlayerStatistics> getPlayerStatistics() {
        return playerStatistics;
    }

}