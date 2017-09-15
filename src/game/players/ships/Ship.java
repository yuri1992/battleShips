package game.players.ships;

import descriptor.Position;
import game.players.GridEntity;
import game.players.GridPoint;
import game.players.ships.ShipDirection;
import game.players.ships.ShipType;

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
            if (pt.x == p.getX() && pt.y == p.getY()) {
                pt.setHit();
                return true;
            }
        }
        return false;
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

    //endregion
}
