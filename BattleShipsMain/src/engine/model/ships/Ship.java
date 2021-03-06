package engine.model.ships;

import descriptor.Position;
import engine.model.boards.GridEntity;
import engine.model.boards.GridPoint;

import java.util.ArrayList;

public class Ship implements GridEntity {

    private String shipTypeId;
    private ShipDirection direction;
    private GridPoint markerPosition;
    private ArrayList<GridPoint> positions;
    private ShipType meta;


    public Ship(String shipTypeId, String direction, Position position, ShipType meta) {
        this.shipTypeId = shipTypeId;
        this.direction = ShipDirection.valueOf(direction);
        this.markerPosition = new GridPoint(position);
        this.meta = meta;
        this.setPositions(markerPosition);
    }

    public Ship(Ship ship) {
        this.shipTypeId = ship.getShipType();
        this.direction = ship.getDirection();
        this.markerPosition = new GridPoint(ship.getPosition());
        this.meta = ship.getMeta();
        this.setPositions(ship.getPosition());
    }

    private void setPositions(GridPoint position) {
        positions = new ArrayList<>();
        positions.add(position);

        switch (direction) {
            case ROW:
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new GridPoint(position.x, position.y + i));
                }
                break;
            case COLUMN:
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new GridPoint(position.x + i, position.y));
                }
                break;
            case RIGHT_DOWN:
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new GridPoint(position.x, position.y - i)); // go left
                    positions.add(new GridPoint(position.x + i, position.y)); // go down
                }
                break;
            case RIGHT_UP:
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new GridPoint(position.x, position.y - i)); // go left
                    positions.add(new GridPoint(position.x - i, position.y)); // go up
                }
                break;
            case UP_RIGHT:
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new GridPoint(position.x + i, position.y)); // go down
                    positions.add(new GridPoint(position.x, position.y + i)); // go right
                }
                break;
            case DOWN_RIGHT:
                for (int i = 1; i < meta.getLength(); i++) {
                    positions.add(new GridPoint(position.x - i, position.y)); // go up
                    positions.add(new GridPoint(position.x, position.y + i)); // go right
                }
                break;
        }

    }

    @Override
    public boolean isHit(GridPoint gridPoint) {
        for (GridPoint pt : positions) {
            if (pt.equals(gridPoint) && pt.isHit()) {
                return true;
            }
        }
        return false;
    }

    /*
        return true, if all ship been hitted
     */
    public boolean isDrowned() {
        for (GridPoint pt : positions) {
            if (!pt.isHit())
                return false;
        }
        return true;
    }

    /*
        if ship located on @pt will mark as hit and return True.
     */
    @Override
    public boolean hit(GridPoint p) {
        for (GridPoint pt : positions) {
            if (pt.equals(p)) {
                pt.setHit();
                return true;
            }
        }
        return false;
    }

    @Override
    public void unHit(GridPoint p) {
        for (GridPoint pt : positions) {
            if (pt.equals(p)) {
                pt.setUnHit();
            }
        }
    }

    //region Setters / Getters

    public String getShipType() {
        return shipTypeId;
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public int getPoints() {
        return meta.getScore();
    }

    @Override
    public GridPoint getPosition() {
        return markerPosition;
    }

    public ArrayList<GridPoint> getPositions() {
        return positions;
    }

    public ShipType getMeta() {
        return meta;
    }
    @Override
    public String toString() {
        return "Ship type " + shipTypeId + " Remaining hits :" + this.getUnhit();
    }

    public int getUnhit() {
        int count = 0;
        for (GridPoint point : this.getPositions()) {
            if (!point.isHit()) {
                count++;
            }
        }
        return count;
    }

    //endregion
}
