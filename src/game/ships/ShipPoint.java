package game.ships;

import descriptor.Position;

public class ShipPoint extends java.awt.Point {
    private boolean isHit = false;

    public ShipPoint(int x, int y) {
        super(x, y);
    }

    public ShipPoint(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    public ShipPoint() {
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

    public boolean isHit() {
        return isHit;
    }

    @Override
    public String toString() {
        return "Point[x=" + this.x + ", y=" + this.y + "]";
    }
}
