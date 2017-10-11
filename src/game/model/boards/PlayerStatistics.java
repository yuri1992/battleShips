package game.model.boards;

import game.engine.GameTurn;

import java.util.List;

/*
        - How many hits
        - How many misses.
        - Average Time of each turn.
 */
public class PlayerStatistics {
    private String name;
    private int score;
    private int hits;
    private int misses;
    private int turns;
    private long avgTurnTime;

    public PlayerStatistics(Player player, List<GameTurn> moves) {
        name = player.toString();
        score = player.getScore();
        turns = moves.size();
        avgTurnTime = 0;
        misses = 0;
        hits = 0;

        for (GameTurn gameTurn : moves) {
            avgTurnTime += gameTurn.getTurnTime();
            if (gameTurn.isHitType()) {
                hits++;
            } else {
                misses++;
            }
        }
        // calculate avg time of each turn.
        if (turns > 0)
            avgTurnTime /= turns;

    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public long getAvgTurnTime() {
        return avgTurnTime;
    }

    public void setAvgTurnTime(long avgTurnTime) {
        this.avgTurnTime = avgTurnTime;
    }

    public int getTurns() {
        return turns;
    }
}