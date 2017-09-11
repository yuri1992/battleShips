package game.players;

import game.exceptions.BoardBuilderException;
import game.exceptions.ShipsLocatedTooClose;
import game.exceptions.ShipsOffBoardException;
import game.ships.Ship;
import game.ships.ShipPoint;

import java.util.ArrayList;
import java.util.List;

public class ShipsBoard implements Board {
    private Ship[][] board;
    private int boardSize;

    public Ship[][] getBoard() {
        return board;
    }

    public ShipsBoard(List<Ship> ships, int boardSize) throws BoardBuilderException {
        this.board = new Ship[boardSize][boardSize];
        this.boardSize = boardSize;
        for (Ship ship : ships) {
            validateShipLocation(ship.getPositions());
            for (ShipPoint pt : ship.getPositions()) {
                setShip(pt, ship);
            }
        }
    }

    /*
        Adding submarine to the board
     */
    private void setShip(ShipPoint pt, Ship ship) throws ShipsLocatedTooClose {
        ArrayList<ShipPoint> l1 = getProhibitedShipPoints(pt);

        for (ShipPoint p : l1) {
            Ship shipByPoint = getShipByPoint(p);
            if (shipByPoint != null && shipByPoint != ship) {
                throw new ShipsLocatedTooClose("Ships " + ship.getType() + " and  " + shipByPoint.getType() + " located too close to each other at point " + ship.getPositions().get(0));
            }
        }

        board[pt.x][pt.y] = ship;
    }

    /*
        Verify ship is not off board
     */
    private boolean validateShipLocation(ArrayList<ShipPoint> positions) throws ShipsOffBoardException {
        for (ShipPoint pt : positions) {
            if (pt.x < 1 || pt.x >= boardSize || pt.y < 1 || pt.y >= boardSize) {
                throw new ShipsOffBoardException("Ship is positioned out of board boandaries at point " + pt);
            }
        }
        return true;
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
        if (pt.y < 0 || pt.x < 0 || pt.x >= board.length || pt.y >= board.length)
            return null;

        return board[pt.x][pt.y];
    }

    @Override
    public String[][] printBoard() {
        String[][] res = new String[board.length][board.length];

        for (int y = 1; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                if (board[x][y] == null)
                    res[x][y] = "~";
                else if (board[x][y].isHit(new ShipPoint(x, y)))
                    res[x][y] = "%";
                else
                    res[x][y] = "@";
            }
        }

        return res;
    }

    public boolean hit(ShipPoint pt) {
        Ship ship = board[pt.x][pt.y];
        if (ship != null) {
            ship.hit(pt.x, pt.y);
            return true;
        }
        return false;
    }
}
