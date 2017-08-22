package game.players;

import game.engine.GameTurn;

/*
        - How many hits
        - How many misses.
        - Average Time of each turn.
 */
public class PlayerStatistics {
    private int hits;
    private int misses;
    private int turns;
    private long avgTurnTime;

    public PlayerStatistics(Player player) {
        turns = player.getTurns().size();
        avgTurnTime = 0;
        misses = 0;
        hits = 0;

        for (GameTurn gameTurn : player.getTurns()) {
            avgTurnTime += gameTurn.getTurnTime();
            if (gameTurn.isHit()) {
                hits++;
            } else {
                misses++;
            }
        }
        // calculate avg time of each turn.
        if (turns > 0)
            avgTurnTime /= turns;

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