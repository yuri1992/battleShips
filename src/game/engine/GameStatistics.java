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
    private Date startAt;
    private List<PlayerStatistics> playerStatistics;


    public GameStatistics(Date startAt, List<Player> playerList) {
        this.startAt = startAt;
        this.setPlayerStatistics(playerList);
        this.setTurns(this.playerStatistics.get(0).getTurns() + this.playerStatistics.get(1).getTurns());
    }

    public long getTotalTime() {
        return this.startAt == null ? 0 : new Date().getTime() - this.startAt.getTime();
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public void setPlayerStatistics(List<Player> playerList) {
        playerStatistics = new ArrayList<>();
        playerList.forEach(player -> {
            this.playerStatistics.add(player.getStatistics());
        });
    }

    public List<PlayerStatistics> getPlayerStatistics() {
        return playerStatistics;
    }

}