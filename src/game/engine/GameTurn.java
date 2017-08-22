package game.engine;

import java.util.Date;

public class GameTurn {
    private Date startAt;
    private Date endAt = null;
    private boolean isHit = false;

    public GameTurn() {
        startAt = new Date();
    }

    public long getTurnTime() {
        return endAt.getTime() - startAt.getTime();
    }

    public void setEndAt() {
        this.endAt = new Date();
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }
}
