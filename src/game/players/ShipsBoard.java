package game.players;

import game.ships.ShipPoint;
import game.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class ShipsBoard implements Board {
    Ship[][] board;

    public ShipsBoard(List<Ship> ships, int boardSize) throws ShipsLocatedTooClose {
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
    public void setShip(ShipPoint pt, Ship ship) throws ShipsLocatedTooClose {
        ArrayList<ShipPoint> l1 = getProhibitedShipPoints(pt);

        for (ShipPoint p : l1) {
            Ship shipByPoint = getShipByPoint(p);
            if (shipByPoint != null && shipByPoint != ship) {
                throw new ShipsLocatedTooClose("Ships " + ship.getType() + " and  " + shipByPoint.getType() + " located too close to each other at point " + p);
            }
        }

        board[pt.y][pt.x] = ship;
    }

    /*
     Return list of Prohibited ShipPoints near to @pt.
     */
    private ArrayList<ShipPoint> getProhibitedShipPoints(ShipPoint pt) {
        ArrayList<ShipPoint> l = new ArrayList<>();
        l.add(new ShipPoint(pt.x, pt.y + 1));
        l.add(new ShipPoint(pt.x, pt.y));
        l.add(new ShipPoint(pt.x, pt.y - 1));
        l.add(new ShipPoint(pt.x + 1, pt.y + 1));
        l.add(new ShipPoint(pt.x + 1, pt.y));
        l.add(new ShipPoint(pt.x + 1, pt.y - 1));
        l.add(new ShipPoint(pt.x - 1, pt.y + 1));
        l.add(new ShipPoint(pt.x - 1, pt.y));
        l.add(new ShipPoint(pt.x - 1, pt.y - 1));
        return l;
    }


    public Ship getShipByPoint(ShipPoint pt) {
        if (pt.y < 0 || pt.x < 0 || pt.x > board.length || pt.y > board.length)
            return null;

        return board[pt.y][pt.x];
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
