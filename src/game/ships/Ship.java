package game.ships;

import descriptor.Position;
import descriptor.ShipType;

import java.util.ArrayList;

public class Ship {

    private String shipId;
    private String direction;
    private ArrayList<ShipPoint> positions;
    private ShipType meta;


    public Ship(String shipId, String direction, Position position, ShipType meta) {
        this.meta = meta;
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
                    positions.add(new ShipPoint(position.x, position.y + i));
                }
                break;
            case "COLUMN":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x + i, position.y));
                }
                break;
            case "RIGHT_DOWN":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x, position.y - i)); // go left
                    positions.add(new ShipPoint(position.x + i, position.y)); // go down
                }
                break;
            case "RIGHT_UP":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x, position.y - i)); // go left
                    positions.add(new ShipPoint(position.x - i, position.y)); // go up
                }
                break;
            case "UP_RIGHT":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x + i, position.y)); // go down
                    positions.add(new ShipPoint(position.x, position.y + i)); // go right
                }
                break;
            case "DOWN_RIGHT":
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new ShipPoint(position.x - i, position.y)); // go up
                    positions.add(new ShipPoint(position.x, position.y + i)); // go right
                }
                break;
        }

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

    public int getPoints() {
        return meta.getScore();
    }

    public ArrayList<ShipPoint> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<ShipPoint> positions) {
        this.positions = positions;
    }

}
