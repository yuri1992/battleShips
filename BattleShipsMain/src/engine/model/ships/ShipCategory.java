package engine.model.ships;

import engine.managers.game.GameMode;

/**
 * Created by amirshavit on 9/13/17.
 */
public enum ShipCategory {

    REGULAR("REGULAR", GameMode.BASIC),
    L_SHAPE("L_SHAPE", GameMode.ADVANCE);

    private final String category;
    private final GameMode mode;

    ShipCategory(String category, GameMode gameMode) {
        this.category = category;
        this.mode = gameMode;
    }

    @Override
    public String toString() {
        return category;
    }

    public GameMode getGameMode() {
        return mode;
    }

}
