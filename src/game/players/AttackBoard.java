package game.players;

import game.engine.HitType;

import java.util.Arrays;

public class AttackBoard implements Board {
    BoardType[][] board;

    @Override
    public BoardType[][] printBoard() {
        return board;
    }

    public void setShoot(GridPoint pt, HitType hit) {
        if (hit == HitType.HIT)
            board[pt.x][pt.y] = BoardType.SHIP_HIT;
        else if (hit == HitType.HIT_MINE)
            board[pt.x][pt.y] = BoardType.MINE_HIT;
        else
            board[pt.x][pt.y] = BoardType.MISS;
    }

    public void setUnShoot(GridPoint pt) {
        board[pt.x][pt.y] = BoardType.EMPTY;
    }

    public AttackBoard(int boardSize) {
        this.board = new BoardType[boardSize][boardSize];
        for (int y = 1; y < board.length; y++) {
            Arrays.fill(board[y], BoardType.EMPTY);
        }
    }

    public boolean isAttacked(GridPoint pt) {
        return board[pt.x][pt.y] != BoardType.EMPTY;
    }
}


