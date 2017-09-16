package game.players;

import java.util.ArrayList;

public interface GridEntity {
    ArrayList<GridPoint> positions = null;
    ArrayList<GridPoint> getPositions();

    boolean hit(GridPoint gridPoint);
    boolean isHit(GridPoint gridPoint);
    int getPoints();

    /*
        Unhit point to support undo functionality
     */
    void unHit(GridPoint gridPoint);

    GridPoint getPosition();
    String toString();
}
