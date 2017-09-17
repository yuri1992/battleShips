package game.players;

import game.engine.HitType;
import game.exceptions.BoardBuilderException;
import game.exceptions.ShipsLocatedTooClose;
import game.exceptions.ShipsOffBoardException;
import game.players.ships.Ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShipsBoard implements Board {
    private BoardType[][] board;
    private int boardSize;
    private int minesAllowance = 0;

    private HashMap<GridPoint, GridEntity> map;
    private List<Ship> ships;
    private List<Mine> mines;

    public ShipsBoard(List<Ship> ships, int boardSize, int mines) throws BoardBuilderException {
        this.board = new BoardType[boardSize][boardSize];
        for (int row = 1; row < board.length; row++) {
            for (int col = 1; col < board.length; col++) {
                board[row][col] = BoardType.EMPTY;
            }
        }

        this.boardSize = boardSize;
        this.minesAllowance = mines;
        this.map = new HashMap<>();
        this.mines = new ArrayList<>();
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

        board[pt.x][pt.y] = BoardType.SHIP;
        map.put(pt, ship);
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


    public HitType hit(GridPoint pt) {
        GridEntity gridEntity = map.get(pt);

        if (gridEntity instanceof Ship) {
            gridEntity.hit(pt);
            board[pt.x][pt.y] = BoardType.SHIP_HIT;
            return HitType.HIT;
        } else if (gridEntity instanceof Mine) {
            gridEntity.hit(pt);
            board[pt.x][pt.y] = BoardType.MINE_HIT;
            return HitType.HIT_MINE;
        } else {
            // Todo : We should mark were the opposite player did attcked us.
            board[pt.x][pt.y] = BoardType.MISS;
        }
        return HitType.MISS;
    }

    /*
        To support undo
     */
    public void unHit(GridPoint pt) {
        GridEntity gridEntity = map.get(pt);
        if (gridEntity instanceof Ship) {
            gridEntity.unHit(pt);
            board[pt.x][pt.y] = BoardType.SHIP;
        } else if (gridEntity instanceof Mine) {
            gridEntity.unHit(pt);
            board[pt.x][pt.y] = BoardType.MINE;
        } else {
            board[pt.x][pt.y] = BoardType.EMPTY;
        }
    }

    public Ship getShipByPoint(GridPoint pt) {
        if (pt.y < 0 || pt.x < 0 || pt.x >= board.length || pt.y >= board.length)
            return null;

        GridEntity gridEntity = map.get(pt);
        if (gridEntity != null && gridEntity instanceof Ship)
            return (Ship) gridEntity;

        return null;
    }

    public GridEntity getPyPoint(GridPoint pt) {
        if (pt.y < 0 || pt.x < 0 || pt.x >= board.length || pt.y >= board.length)
            return null;

        return map.get(pt);
    }

    @Override
    public BoardType[][] printBoard() {
        return board;
    }

    public boolean allShipsGotHit() {
        for (Ship ship : ships) {
            if (!ship.isDrowned())
                return false;
        }
        return true;
    }

    public BoardType[][] getBoard() {
        return board;
    }


    public int getAvailableMines() {
        return minesAllowance - mines.size();
    }

    public boolean setMine(GridPoint gridPoint) {
        if (this.getPyPoint(gridPoint) != null) {
            return false;
        }

        for (GridPoint prohibitedPoint : getProhibitedShipPoints(gridPoint)) {
            if (this.getPyPoint(prohibitedPoint) != null) {
                return false;
            }
        }

        Mine mine = new Mine(gridPoint);
        this.mines.add(mine);
        board[gridPoint.x][gridPoint.y] = BoardType.MINE;
        map.put(gridPoint, mine);
        return true;
    }

    public List<Ship> getRemainShips() {
        List<Ship> remain = new ArrayList<>();
        for (Ship ship : ships) {
            if (!ship.isDrowned())
                remain.add(ship);
        }
        return remain;
    }
}
