package game.players;

import game.exceptions.BoardBuilderException;
import game.exceptions.ShipsLocatedTooClose;
import game.exceptions.ShipsOffBoardException;
import game.players.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class ShipsBoard implements Board {
    private GridEntity[][] board;
    private int boardSize;
    private int minesAllowance;

    private List<Ship> ships;

    public ShipsBoard(List<Ship> ships, int boardSize, int mines) throws BoardBuilderException {
        this.board = new GridEntity[boardSize][boardSize];
        this.boardSize = boardSize;
        this.minesAllowance = mines;
        this.setShips(ships);
    }

    private void setShips(List<Ship> ships) throws ShipsOffBoardException, ShipsLocatedTooClose {
        for (Ship ship : ships) {
            validateShipLocation(ship.getPositions());
            for (GridPoint pt : ship.getPositions()) {
                setShip(pt, ship);
            }
        }
        this.ships = ships;
    }

    private void setShip(GridPoint pt, Ship ship) throws ShipsLocatedTooClose {
        ArrayList<GridPoint> l1 = getProhibitedShipPoints(pt);

        for (GridPoint p : l1) {
            Ship shipByPoint = getShipByPoint(p);
            if (shipByPoint != null && shipByPoint != ship) {
                throw new ShipsLocatedTooClose("Ships " + ship.getShipType() + " and  " + shipByPoint.getShipType() + " " +
                        "located too close to each other at point " + ship.getPositions().get(0));
            }
        }

        board[pt.x][pt.y] = ship;
    }

    /*
        Verify ship is not off board
     */
    private void validateShipLocation(ArrayList<GridPoint> positions) throws ShipsOffBoardException {
        for (GridPoint pt : positions) {
            if (pt.x < 1 || pt.x >= boardSize || pt.y < 1 || pt.y >= boardSize) {
                throw new ShipsOffBoardException("Ship is positioned out of board boundaries at point " + pt);
            }
        }
    }

    public boolean isMineLocationValid(GridPoint pt) {
        for (GridPoint prohibitedPoint : this.getProhibitedShipPoints(pt)) {
            GridEntity shipByPoint = getShipByPoint(prohibitedPoint);
            if (shipByPoint != null) {
                return false;
            }
        }
        return true;
    }

    /*
     Return list of Prohibited ShipPoints near to @pt.
     */
    private ArrayList<GridPoint> getProhibitedShipPoints(GridPoint pt) {
        ArrayList<GridPoint> l = new ArrayList<>();
        l.add(new GridPoint(pt.x, pt.y + 1));
        l.add(new GridPoint(pt.x, pt.y));
        l.add(new GridPoint(pt.x, pt.y - 1));
        l.add(new GridPoint(pt.x + 1, pt.y + 1));
        l.add(new GridPoint(pt.x + 1, pt.y));
        l.add(new GridPoint(pt.x + 1, pt.y - 1));
        l.add(new GridPoint(pt.x - 1, pt.y + 1));
        l.add(new GridPoint(pt.x - 1, pt.y));
        l.add(new GridPoint(pt.x - 1, pt.y - 1));
        return l;
    }


    public boolean hit(GridPoint pt) {
        GridEntity ship = board[pt.x][pt.y];
        if (ship != null) {
            ship.hit(pt);
            return true;
        }
        return false;
    }

    public Ship getShipByPoint(GridPoint pt) {
        if (pt.y < 0 || pt.x < 0 || pt.x >= board.length || pt.y >= board.length)
            return null;

        if (board[pt.x][pt.y] != null && board[pt.x][pt.y] instanceof Ship)
            return (Ship) board[pt.x][pt.y];

        return null;
    }

    @Override
    public BoardType[][] printBoard() {
        BoardType[][] res = new BoardType[board.length][board.length];
        for (int y = 1; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                if (board[y][x] == null)
                    res[y][x] = BoardType.EMPTY;
                else if (board[y][x] instanceof Mine) {
                    res[y][x] = BoardType.MINE;
                    if (board[y][x].isHit(new GridPoint(x, y)))
                        res[y][x] = BoardType.MINE_HIT;
                } else {
                    res[y][x] = BoardType.SHIP;
                    if (board[y][x].isHit(new GridPoint(x, y)))
                        res[y][x] = BoardType.SHIP_HIT;
                }

            }
        }

        return res;
    }

    public boolean allShipsGotHit() {
        for (Ship ship : ships) {
            if (!ship.isDrowned())
                return false;
        }
        return true;
    }

    public GridEntity[][] getBoard() {
        return board;
    }

    public boolean placeMine(GridPoint gridPoint) {
        return false;
    }
}
