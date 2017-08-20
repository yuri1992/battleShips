package game.players;

import game.engine.ShipPoint;

public class AttackBoard implements Board {
    AttackBoardMove[][] board;

    @Override
    public String[][] printBoard() {
        String[][] res = new String[board.length][board.length];

        for (int y = 1; y < board.length; y++) {
            for (int x = 1; x < board.length; x++) {
                if (board[y][x] == null)
                    res[y][x] = "~";
                else if (board[y][x].isHit())
                    res[y][x] = "*";
                else
                    res[y][x] = "^";
            }
        }

        return res;
    }

    public void setShoot(ShipPoint pt, boolean attack) {
        board[pt.y][pt.x].setHit(attack);
    }

    public AttackBoard(int rows, int cols) {
        this.board = new AttackBoardMove[rows][cols];
    }

    public AttackBoardMove[][] getBoard() {
        return board;
    }

    public void setBoard(AttackBoardMove[][] board) {
        this.board = board;
    }

    public boolean isAttacked(ShipPoint pt) {
        return board[pt.y][pt.x] != null;
    }
}


class AttackBoardMove {
    private boolean isHit;

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }
}
