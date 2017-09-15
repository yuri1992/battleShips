package game.players;

import java.util.ArrayList;

public interface GridEntity {
    ArrayList<GridPoint> positions = null;
    ArrayList<GridPoint> getPositions();

    boolean hit(GridPoint gridPoint);
    boolean isHit(GridPoint gridPoint);
    int getPoints();

    GridPoint getPosition();
    String toString();
}
