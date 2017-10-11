package game.model.ships;

/**
 * Created by amirshavit on 9/13/17.
 */
public enum ShipDirection {
    ROW("ROW", ShipCategory.REGULAR),
    COLUMN("COLUMN", ShipCategory.REGULAR),
    UP_RIGHT("UP_RIGHT", ShipCategory.L_SHAPE),
    RIGHT_UP("RIGHT_UP", ShipCategory.L_SHAPE),
    DOWN_RIGHT("DOWN_RIGHT", ShipCategory.L_SHAPE),
    RIGHT_DOWN("RIGHT_DOWN", ShipCategory.L_SHAPE);

    private final String direction;
    private final ShipCategory category;

    ShipDirection(String direction, ShipCategory category) {
        this.direction = direction;
        this.category = category;
    }

    @Override
    public String toString() {
        return direction;
    }

    public ShipCategory getCategory() {
        return category;
    }
}
