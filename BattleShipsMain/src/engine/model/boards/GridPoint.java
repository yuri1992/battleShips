package engine.model.boards;

import descriptor.Position;

public class GridPoint extends java.awt.Point {
    private boolean isHit = false;

    public GridPoint(int x, int y) {
        super(x, y);
    }

    public GridPoint(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    public GridPoint(GridPoint pos) {
        this.x = (int) pos.getX();
        this.y = (int) pos.getY();
    }

    public GridPoint() {
        x = 0;
        y = 0;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHit() {
        isHit = true;
    }

    public void setUnHit() {
        isHit = false;
    }

    public boolean isHit() {
        return isHit;
    }

    @Override
    public String toString() {
        return "Point[x=" + this.x + ", y=" + this.y + "]";
    }
}
