package game.ships;

import descriptor.Position;
import descriptor.ShipType;
import game.engine.GameManager;
import game.engine.ShipPoint;

import java.util.ArrayList;

public class Ship {

    private String shipId;
    private String direction;
    private ArrayList<ShipPoint> positions;


    public Ship(String shipId, String direction, Position position, ShipType meta) {
        this.shipId = shipId;
        this.direction = direction;
        this.setPositions(new ShipPoint(position));
    }

    private void setPositions(ShipPoint position) {
        positions = new ArrayList<>();
        positions.add(position);

        switch (direction) {
            case "ROW":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x + i, position.y));
                }
                break;
            case "COLUMN":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x, position.y + i));
                }
                break;
            case "RIGHT_DOWN":
                break;
            case "RIGHT_UP":
                break;
            case "UP_RIGHT":
                break;
            case "DOWN_RIGHT":
                break;
        }

    }

    public void setMeta(GameManager.ShipType meta) {
        this.meta = meta;
    }

    public boolean isHit(ShipPoint shipPoint) {
        for (ShipPoint pt : positions) {
            if (pt.equals(shipPoint) && pt.isHit()) {
                return true;
            }
        }
        return false;
    }

    /*
        return true, if all ship been hitted
     */
    public boolean isDrowned() {
        for (ShipPoint pt : positions) {
            if (!pt.isHit())
                return false;
        }
        return true;
    }

    /*
        if ship located on @pt will mark as hit and return True.
     */
    public boolean hit(int x, int y) {
        for (ShipPoint pt : positions) {
            if (pt.x == x && pt.y == y) {
                pt.setHit();
                return true;
            }
        }
        return false;
    }

    public String getType() {
        return shipId;
    }

    public ArrayList<ShipPoint> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<ShipPoint> positions) {
        this.positions = positions;
    }

}
