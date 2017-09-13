package game.engine;

import game.ships.ShipPoint;
import runners.console.ConsoleUtils;

import java.util.Date;

public class GameTurn {
    private Date startAt;
    private Date endAt = null;
    private boolean isHit = false;
    private ShipPoint point;

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

    public void setPoint(ShipPoint point) {
        this.point = point;
    }

    public String toString() {
        if (point == null)
            return "Not Used Turn";
        return (isHit ? "Hit" : "Miss") + " on " + point + " took " + ConsoleUtils.formatDateHM(getTurnTime());
    }
}
