package game.engine;

import game.players.GridPoint;
import game.players.Player;
import runners.console.ConsoleUtils;

import java.util.Date;

public class GameTurn {
    private int index = 0;
    private Player player = null;
    private Date startAt;
    private Date endAt = null;
    private HitType hitType = HitType.MISS;
    private GridPoint point;

    public GameTurn(int index, Player player) {
        this.index = index;
        this.player = player;
        this.startAt = new Date();
    }

    public long getTurnTime() {
        return endAt.getTime() - startAt.getTime();
    }

    public void end() {
        this.endAt = new Date();
    }

    public boolean isHitType() {
        return hitType == HitType.HIT;
    }

    public void setHitType(HitType hitType) {
        this.hitType = hitType;
    }

    public void setPoint(GridPoint point) {
        this.point = point;
    }

    public String toString() {
        if (point == null)
            return "Not Used Turn";
        return hitType.toString() + " on " + point + " took " + ConsoleUtils.formatDateHM(getTurnTime());
    }
}
