package game.players;

import java.util.Arrays;

public class AttackBoard implements Board {
    BoardType[][] board;

    @Override
    public BoardType[][] printBoard() {
        return board;
    }

    public void setShoot(GridPoint pt, boolean attack) {
        board[pt.x][pt.y] = attack ? BoardType.SHIP_HIT : BoardType.MISS;
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


