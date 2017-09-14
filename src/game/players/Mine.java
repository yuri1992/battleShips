package game.players;

import java.util.ArrayList;

public class Mine implements GridEntity {
    private ArrayList<GridPoint> positions;

    public Mine(GridPoint p) {
        this.positions = new ArrayList<>();
        this.positions.add(p);
    }

    @Override
    public ArrayList<GridPoint> getPositions() {
        return this.positions;
    }

    @Override
    public boolean hit(GridPoint gridPoint) {
        if (gridPoint.equals(this.positions.get(0))) {
            this.positions.get(0).setHit();
            return true;
        }
        return false;
    }

    @Override
    public boolean isHit(GridPoint gridPoint) {
        if (gridPoint.equals(this.positions.get(0))) {
            return this.positions.get(0).isHit();
        }
        return false;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public GridPoint getPosition() {
        return this.positions.get(0);
    }

    @Override
    public String toString() {
        return "Mine[x=" + this.getPosition().getX() + ", y=" + this.getPosition().getY() + "]";
    }
}