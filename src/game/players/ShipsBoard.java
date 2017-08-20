package game.players;

import game.engine.ShipPoint;
import game.ships.Ship;

import java.util.List;

public class ShipsBoard implements Board {
    Ship[][] board;

    public ShipsBoard(List<Ship> ships, int boardSize) {
        board = new Ship[boardSize][boardSize];
        for (Ship ship : ships) {
            for (ShipPoint pt : ship.getPositions()) {
                setShip(pt, ship);
            }
        }
    }

    /*
        Adding submarine to the board
     */
    public void setShip(ShipPoint pt, Ship ship) {

        board[pt.y][pt.x] = ship;
    }

    /*
        return true when navy board is valid, mean no ships located near to each other.
     */
    public boolean isValid() {

        return true;
    }

    @Override
    public String[][] printBoard() {
        String[][] res = new String[board.length][board.length];

        for (int y = 1; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                if (board[y][x] == null)
                    res[y][x] = "~";
                else if (board[y][x].isHit(new ShipPoint(x, y)))
                    res[y][x] = "%";
                else
                    res[y][x] = "@";
            }
        }

        return res;
    }

    public boolean hit(ShipPoint pt) {
        Ship ship = board[pt.y][pt.x];
        if (ship != null) {
            ship.hit(pt.x, pt.y);
            return true;
        }
        return false;
    }
}
